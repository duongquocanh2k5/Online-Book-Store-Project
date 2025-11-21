package onlineBookStore.managers;

import onlineBookStore.models.Customer;
import java.util.Scanner;

public class UIHelper {
    private final Scanner scanner;
    
    public UIHelper(Scanner scanner) {
        this.scanner = scanner;
    }
    
    public void showWelcome() {
        System.out.println("\n" + repeatString("=", 50)); 
        System.out.println("    WELCOME TO ONLINE BOOKSTORE");
        System.out.println(repeatString("=", 50) + "\n");
    }
    
    public Customer getCustomerInfo() {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter your address: ");
        String address = scanner.nextLine();
        
        String customerId = "C" + System.currentTimeMillis() % 10000;
        Customer customer = new Customer(customerId, name, address);
        
        System.out.println("\nHello " + name + "! Your customer ID is: " + customerId);
        return customer;
    }
    
    public void showMenu() {
        System.out.println("\n" + repeatString("-", 50));
        System.out.println("                 MAIN MENU");
        System.out.println(repeatString("-", 50));
        System.out.println("1. View book list");
        System.out.println("2. Add book to cart");
        System.out.println("3. View cart");
        System.out.println("4. Undo last cart action");
        System.out.println("5. Checkout");
        System.out.println("6. Add new book to inventory");
        System.out.println("7. View order history");
        System.out.println("8. Search order");
       
        System.out.println("9. Exit");  
        System.out.println(repeatString("-", 50));
    }
    
    public int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }
    
    public double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }
    
    public static String repeatString(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}