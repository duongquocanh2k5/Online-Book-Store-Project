package onlineBookStore.managers;

import onlineBookStore.models.Book;

import java.util.ArrayList;
import java.util.Scanner;

public class InventoryManager {
    private ArrayList<Book> inventory;
    
    public InventoryManager() {
        initializeInventory();
    }
    
    private void initializeInventory() {
        inventory = new ArrayList<Book>();
        inventory.add(new Book("B001", "Effective Java", "Joshua Bloch", 45.0, 5));
        inventory.add(new Book("B002", "Clean Code", "Robert C. Martin", 40.0, 3));
        inventory.add(new Book("B003", "Introduction to Algorithms", "Cormen", 80.0, 2));
        inventory.add(new Book("B004", "Design Patterns", "Gamma", 50.0, 4));
        inventory.add(new Book("B005", "Refactoring", "Martin Fowler", 47.5, 1));
    }
    
    public ArrayList<Book> getInventory() {
        return inventory;
    }
    
    public void viewInventory() {
        if (inventory.isEmpty()) {
            System.out.println("\nInventory is empty!");
            return;
        }
        
        System.out.println("\n" + UIHelper.repeatString("=", 80));
        System.out.println("BOOK INVENTORY");
        System.out.println(UIHelper.repeatString("=", 80));
        
        for (int i = 0; i < inventory.size(); i++) {
            Book b = inventory.get(i);
            System.out.printf("[%d] %s | %s | Author: %s | Price: $%.2f | Stock: %d%n", 
                i + 1, b.getId(), b.getTitle(), b.getAuthor(), b.getPrice(), b.getStock());
        }
        
        System.out.println(UIHelper.repeatString("=", 80));
        System.out.printf("Total books in inventory: %d types%n", inventory.size());
    }
    
    public void addNewBook(Scanner scanner) {
        UIHelper ui = new UIHelper(scanner);
        
        System.out.println("\n" + UIHelper.repeatString("=", 50));
        System.out.println("ADD NEW BOOK TO INVENTORY");
        System.out.println(UIHelper.repeatString("=", 50));
        
        String bookId = generateBookId();
        System.out.println("Auto-generated Book ID: " + bookId);
        
        System.out.print("\nEnter book title: ");
        String title = scanner.nextLine().trim();
        if (title.isEmpty()) {
            System.out.println("Book title cannot be empty!");
            return;
        }
        
        for (Book b : inventory) {
            if (b.getTitle().equalsIgnoreCase(title)) {
                System.out.println("Warning: Book \"" + title + "\" already exists in inventory!");
                System.out.print("Do you want to continue? (y/n): ");
                String confirm = scanner.nextLine().trim().toLowerCase();
                if (!confirm.equals("y")) {
                    System.out.println("Cancelled!");
                    return;
                }
                break;
            }
        }
        
        System.out.print("Enter author name: ");
        String author = scanner.nextLine().trim();
        if (author.isEmpty()) {
            System.out.println("Author name cannot be empty!");
            return;
        }
        
        double price = ui.getDoubleInput("Enter book price ($): ");
        if (price <= 0) {
            System.out.println("Price must be greater than 0!");
            return;
        }
        
        int stock = ui.getIntInput("Enter stock quantity: ");
        if (stock < 0) {
            System.out.println("Stock cannot be negative!");
            return;
        }
        
        Book newBook = new Book(bookId, title, author, price, stock);
        inventory.add(newBook);
        
        System.out.println("\n" + UIHelper.repeatString("=", 50));
        System.out.println("BOOK ADDED SUCCESSFULLY!");
        System.out.println(UIHelper.repeatString("=", 50));
        System.out.println(newBook);
        System.out.println(UIHelper.repeatString("=", 50));
    }
    
    
    // Generate a new unique book ID
    private String generateBookId() {
        int maxId = 0;
        for (Book b : inventory) {
            String id = b.getId();
            if (id.startsWith("B")) {
                try {
                    int num = Integer.parseInt(id.substring(1));
                    if (num > maxId) maxId = num;
                } catch (NumberFormatException e) {
                }
            }
        }
        return String.format("B%03d", maxId + 1);
    }
    
    public void updateInventoryFromArray(Book[] arr) {
        for (int i = 0; i < inventory.size(); i++) {
            Book original = inventory.get(i);
            for (Book updated : arr) {
                if (original.getId().equals(updated.getId())) {
                    inventory.set(i, updated);
                    break;
                }
            }
        }
    }
}