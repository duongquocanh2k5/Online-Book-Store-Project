package onlineBookStore.managers;

import onlineBookStore.adt.BookQueue;
import onlineBookStore.adt.SimpleStack;
import onlineBookStore.models.Book;
import onlineBookStore.models.Customer;
import onlineBookStore.models.Order;
import onlineBookStore.models.OrderItem;
import onlineBookStore.service.OrderProcessor;

import java.util.Scanner;

public class CartManager {
    private Order currentOrder;
    private final Customer customer;
    private final InventoryManager inventoryManager;
    private final OrderHistoryManager orderHistoryManager;
    private final BookQueue orderQueue;
    private final SimpleStack<Order> cartHistory;
    private static final int MAX_UNDO_LEVELS = 10;
    
    public CartManager(Customer customer, InventoryManager inventoryManager, 
                      OrderHistoryManager orderHistoryManager) {
        this.customer = customer;
        this.inventoryManager = inventoryManager;
        this.orderHistoryManager = orderHistoryManager;
        this.currentOrder = new Order(customer);
        this.orderQueue = new BookQueue();
        this.cartHistory = new SimpleStack<>(); // ngăn xếp đơn giản để lưu lịch sử giỏ hàng vào stack
    }
    // Thêm sách vào giỏ hàng
    public void addBookToCart(Scanner scanner) {
        UIHelper ui = new UIHelper(scanner);
        
        if (inventoryManager.getInventory().isEmpty()) {
            System.out.println("\nInventory is empty!");
            return;
        }
        
        inventoryManager.viewInventory();
        
        int bookIndex = ui.getIntInput("\nEnter book number (1-" + 
            inventoryManager.getInventory().size() + ") or 0 to cancel: ");
        
        if (bookIndex == 0) {
            System.out.println("Cancelled!");
            return;
        }
        
        if (bookIndex < 1 || bookIndex > inventoryManager.getInventory().size()) {
            System.out.println("Invalid book number!");
            return;
        }
        
        Book selectedBook = inventoryManager.getInventory().get(bookIndex - 1);
        
        if (selectedBook.getStock() == 0) {
            System.out.println("This book is out of stock!");
            return;
        }
        
        int quantityInCart = getQuantityInCart(selectedBook); // algorithm linear search
        
        System.out.println("\nYou selected: " + selectedBook.getTitle());
        System.out.println("Available stock: " + selectedBook.getStock() + " copies");
        
        if (quantityInCart > 0) {
            System.out.println("Already in cart: " + quantityInCart + " copies");
            int remainingAvailable = selectedBook.getStock() - quantityInCart;
            
            if (remainingAvailable <= 0) {
                System.out.println("\nError: You already have the maximum available quantity in your cart.");
                System.out.println("Cannot add more copies of this book.");
                return;
            }
            
            System.out.println("Maximum you can add: " + remainingAvailable + " copies");
            
            int quantity = ui.getIntInput("Enter quantity to purchase (1-" + remainingAvailable + "): ");
            
            if (quantity < 1 || quantity > remainingAvailable) {
                System.out.println("Invalid quantity!");
                return;
            }
            
            saveCartState();
            currentOrder.addItem(selectedBook, quantity);
            System.out.println("\nAdded " + quantity + " copy/copies of \"" + selectedBook.getTitle() + "\" to cart!");
            System.out.println("Total in cart now: " + (quantityInCart + quantity) + " copies");
            
        } else {
            int quantity = ui.getIntInput("Enter quantity to purchase (1-" + selectedBook.getStock() + "): ");
            
            if (quantity < 1 || quantity > selectedBook.getStock()) {
                System.out.println("Invalid quantity!");
                return;
            }
            
            saveCartState();
            currentOrder.addItem(selectedBook, quantity);
            System.out.println("\nAdded " + quantity + " copy/copies of \"" + selectedBook.getTitle() + "\" to cart!");
        }
        
        showUndoInfo();
    }
    

    // Lưu trạng thái giỏ hàng hiện tại vào ngăn xếp lịch sử vào stack
    private void saveCartState() {
        if (cartHistory.size() >= MAX_UNDO_LEVELS) {
            System.out.println("(Note: Undo history limit reached)");
        }
        
        Order snapshot = currentOrder.clone();
        cartHistory.push(snapshot);
    }
    // Hoàn tác hành động giỏ hàng cuối cùng (lifo stack)
    public void undo() {
        if (cartHistory.isEmpty()) {
            System.out.println("\nNothing to undo! Cart history is empty.");
            return;
        }
        
        currentOrder = cartHistory.pop();
        
        System.out.println("\n" + UIHelper.repeatString("=", 50));
        System.out.println("UNDO SUCCESSFUL!");
        System.out.println(UIHelper.repeatString("=", 50));
        System.out.println("Cart restored to previous state.");
        
        if (currentOrder.getItems().isEmpty()) {
            System.out.println("Cart is now empty.");
        } else {
            System.out.println("\nCurrent cart:");
            viewCart();
        }
        
        showUndoInfo();
    }
    
    
    
    private void showUndoInfo() {
        int undoCount = cartHistory.size();
        if (undoCount > 0) {
            System.out.println("(You can undo " + undoCount + " action(s))");
        }
    }
    
    public void viewCart() {
        if (currentOrder.getItems().isEmpty()) {
            System.out.println("\nYour cart is empty!");
            return;
        }
        
        System.out.println("\n" + UIHelper.repeatString("=", 80));
        System.out.println("YOUR SHOPPING CART");
        System.out.println(UIHelper.repeatString("=", 80));
        
        int count = 1;
        for (OrderItem item : currentOrder.getItems()) {
            double subtotal = item.getBook().getPrice() * item.getQuantity();
            System.out.printf("%d. %s | Quantity: %d | Unit Price: $%.2f | Subtotal: $%.2f%n",
                count++, item.getBook().getTitle(), item.getQuantity(), 
                item.getBook().getPrice(), subtotal);
        }
        
        System.out.println(UIHelper.repeatString("-", 80));
        System.out.printf("TOTAL: $%.2f%n", currentOrder.totalAmount());
        System.out.println(UIHelper.repeatString("=", 80));
        
        showUndoInfo();
    }
    
    public void checkout() {
        if (currentOrder.getItems().isEmpty()) {
            System.out.println("\nCart is empty! Please add books first.");
            return;
        }
        
        System.out.println("\n" + UIHelper.repeatString("=", 50));
        System.out.println("CHECKOUT");
        System.out.println(UIHelper.repeatString("=", 50));
        viewCart();
        
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nConfirm checkout? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        
        if (!confirm.equals("y") && !confirm.equals("yes")) {
            System.out.println("Checkout cancelled!");
            return;
        }
        
        Book[] inventoryArray = inventoryManager.getInventory().toArray(new Book[0]);
        orderQueue.enqueue(currentOrder);
        OrderProcessor processor = new OrderProcessor(inventoryArray, orderQueue);
        processor.processNextOrder();
        
        inventoryManager.updateInventoryFromArray(inventoryArray);
        
        Order processedOrder = processor.getProcessedOrders().get(0);
        
        System.out.println("\n" + UIHelper.repeatString("=", 60));
        
        if (processedOrder.getStatus() == Order.Status.COMPLETED) {
            System.out.println("CHECKOUT SUCCESSFUL!");
            System.out.println(UIHelper.repeatString("=", 60));
            System.out.println(processedOrder);
            System.out.println(UIHelper.repeatString("=", 60));
            
            orderHistoryManager.addOrder(processedOrder);
            currentOrder = new Order(customer);
            clearCartHistory();
            
            System.out.println("\nYou can continue shopping!");
        } else {
            System.out.println("CHECKOUT FAILED!");
            System.out.println(UIHelper.repeatString("=", 60));
            System.out.println("Reason: Insufficient stock");
            System.out.println("Please check quantity!");
            
            orderHistoryManager.addOrder(processedOrder);
            currentOrder = new Order(customer);
            clearCartHistory();
        }
    }
    
    private void clearCartHistory() {
        while (!cartHistory.isEmpty()) {
            cartHistory.pop();
        }
    }
    
    // Tính tổng số lượng sách đã có trong giỏ hàng (linear search)
    private int getQuantityInCart(Book book) {
        int total = 0;
        for (OrderItem item : currentOrder.getItems()) {
            if (item.getBook().getId().equals(book.getId())) {
                total += item.getQuantity();
            }
        }
        return total;
    }
}