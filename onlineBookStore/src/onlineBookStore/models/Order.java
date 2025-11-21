package onlineBookStore.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order {
    public enum Status { PENDING, PROCESSING, COMPLETED, FAILED }
    
    private final String orderId;
    private final Customer customer;
    private final List<OrderItem> items;
    private Status status;
    
    public Order(Customer customer) {
        this.orderId = customer.getId() + "-ORD" + String.format("%03d", (int)(Math.random() * 1000)); // %03d là định dạng số với 3 chữ số, thêm số 0 ở đầu nếu cần
        this.customer = customer;
        this.items = new ArrayList<>();
        this.status = Status.PENDING;
    }
    
    //  Constructor for cloning
    private Order(String orderId, Customer customer, List<OrderItem> items, Status status) {
        this.orderId = orderId;
        this.customer = customer;
        this.items = new ArrayList<>(items);  // Deep copy
        this.status = status;
    }
    
    // Clone method for undo functionality
    public Order clone() {
        List<OrderItem> clonedItems = new ArrayList<>();
        for (OrderItem item : this.items) {
            clonedItems.add(new OrderItem(item.getBook(), item.getQuantity()));
        }
        return new Order(this.orderId, this.customer, clonedItems, this.status);
    }
    
    public void addItem(Book book, int quantity) {
        items.add(new OrderItem(book, quantity));
    }
    
    public String getOrderId() { return orderId; }
    public Customer getCustomer() { return customer; }
    public List<OrderItem> getItems() { return items; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    
    public double totalAmount() {
        return items.stream()
                .mapToDouble(i -> i.getBook().getPrice() * i.getQuantity()) // Calculate total price for each item
                .sum();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Order %s | %s%n", orderId, customer)); // Order ID | Customer info
        sb.append(String.format("Status: %s%n", status)); // Status
        
        for (OrderItem item : items) {
            sb.append(String.format("  - %s%n", item));
        }
        
        sb.append(String.format("Total: $%.2f", totalAmount()));
        return sb.toString();
    }
}