package onlineBookStore.managers;

import onlineBookStore.algorithms.Searching;
import onlineBookStore.models.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Manager class for order history and search operations (linear search).
 */
public class OrderHistoryManager {
    private final List<Order> orderHistory;
    
    public OrderHistoryManager() {
        this.orderHistory = new ArrayList<>();
    }
    
    public void addOrder(Order order) {
        orderHistory.add(order);
    }
    
    public void viewOrderHistory() {
        if (orderHistory.isEmpty()) {
            System.out.println("\nNo order history available.");
            System.out.println("You haven't placed any orders yet.");
            return;
        }
        
        System.out.println("\n" + UIHelper.repeatString("=", 80));
        System.out.println("ORDER HISTORY");
        System.out.println(UIHelper.repeatString("=", 80));
        System.out.printf("Total orders: %d%n", orderHistory.size());
        System.out.println(UIHelper.repeatString("-", 80));
        
        for (int i = 0; i < orderHistory.size(); i++) {
            Order order = orderHistory.get(i);
            System.out.printf("%n[%d] Order ID: %s%n", i + 1, order.getOrderId());
            System.out.printf("    Customer: %s%n", order.getCustomer().getName());
            System.out.printf("    Status: %s%n", order.getStatus());
            System.out.printf("    Items: %d%n", order.getItems().size());
            System.out.printf("    Total: $%.2f%n", order.totalAmount());
            System.out.println(UIHelper.repeatString("-", 80));
        }
        
        long completedCount = orderHistory.stream()
            .filter(o -> o.getStatus() == Order.Status.COMPLETED)
            .count();
        long failedCount = orderHistory.stream()
            .filter(o -> o.getStatus() == Order.Status.FAILED)
            .count();
        double totalRevenue = orderHistory.stream()
            .filter(o -> o.getStatus() == Order.Status.COMPLETED)
            .mapToDouble(Order::totalAmount)
            .sum();
        
        System.out.println("\nSTATISTICS:");
        System.out.printf("Completed orders: %d%n", completedCount);
        System.out.printf("Failed orders: %d%n", failedCount);
        System.out.printf("Total revenue: $%.2f%n", totalRevenue);
        System.out.println(UIHelper.repeatString("=", 80));
    }
    
    public void searchOrder(Scanner scanner) {
        UIHelper ui = new UIHelper(scanner);
        
        if (orderHistory.isEmpty()) {
            System.out.println("\nNo order history to search.");
            return;
        }
        
        System.out.println("\n" + UIHelper.repeatString("=", 50));
        System.out.println("SEARCH ORDER");
        System.out.println(UIHelper.repeatString("=", 50));
        System.out.println("1. Search by Order ID");
        System.out.println("2. Search by Customer Name");
        System.out.println("3. Filter by Status");
        System.out.println("0. Cancel");
        System.out.println(UIHelper.repeatString("-", 50));
        
        int choice = ui.getIntInput("Select search method: ");
        
        switch (choice) {
            case 1:
                searchByOrderId(scanner);
                break;
            case 2:
                searchByCustomerName(scanner);
                break;
            case 3:
                filterByStatus(scanner, ui);
                break;
            case 0:
                System.out.println("Cancelled!");
                break;
            default:
                System.out.println("Invalid option!");
        }
    }



    // Search by Order ID 
    private void searchByOrderId(Scanner scanner) {
        System.out.print("\nEnter Order ID: ");
        String orderId = scanner.nextLine().trim();
        
        Order found = Searching.linearSearchOrderById(orderHistory, orderId); // algorithm linear search
        
        if (found != null) {
            System.out.println("\n" + UIHelper.repeatString("=", 60));
            System.out.println("ORDER FOUND!");
            System.out.println(UIHelper.repeatString("=", 60));
            System.out.println(found);
            System.out.println(UIHelper.repeatString("=", 60));
        } else {
            System.out.println("\nOrder ID \"" + orderId + "\" not found.");
        }
    }



    // Search by customer name (partial match)
    private void searchByCustomerName(Scanner scanner) {
        System.out.print("\nEnter customer name (partial match allowed): ");
        String name = scanner.nextLine().trim().toLowerCase();
        
        List<Order> results = new ArrayList<>();
        for (Order order : orderHistory) {
            if (order.getCustomer().getName().toLowerCase().contains(name)) {
                results.add(order);
            }
        }
        
        if (results.isEmpty()) {
            System.out.println("\nNo orders found for customer name containing \"" + name + "\"");
        } else {
            System.out.println("\n" + UIHelper.repeatString("=", 80));
            System.out.printf("FOUND %d ORDER(S) FOR CUSTOMER NAME CONTAINING \"%s\"%n", results.size(), name);
            System.out.println(UIHelper.repeatString("=", 80));
            
            for (int i = 0; i < results.size(); i++) {
                Order order = results.get(i);
                System.out.printf("%n[%d] %s%n", i + 1, order.getOrderId());
                System.out.printf("    Customer: %s%n", order.getCustomer().getName());
                System.out.printf("    Status: %s%n", order.getStatus());
                System.out.printf("    Total: $%.2f%n", order.totalAmount());
                System.out.println(UIHelper.repeatString("-", 80));
            }
        }
    }
    

    // Filter by order status
    private void filterByStatus(Scanner scanner, UIHelper ui) {
        System.out.println("\nSelect status to filter:");
        System.out.println("1. COMPLETED");
        // System.out.println("2. FAILED");
        // System.out.println("3. PROCESSING");
        // System.out.println("4. PENDING");
        
        int choice = ui.getIntInput("Select status: ");
        
        Order.Status targetStatus;
        switch (choice) {
            case 1:
                targetStatus = Order.Status.COMPLETED;
                break;
            default:
                System.out.println("Invalid option!");
                return;
        }
        
        List<Order> results = new ArrayList<>();
        for (Order order : orderHistory) {
            if (order.getStatus() == targetStatus) {
                results.add(order);
            }
        }
        
        if (results.isEmpty()) {
            System.out.println("\nNo orders with status " + targetStatus);
        } else {
            System.out.println("\n" + UIHelper.repeatString("=", 80));
            System.out.printf("ORDERS WITH STATUS: %s (%d)%n", targetStatus, results.size());
            System.out.println(UIHelper.repeatString("=", 80));
            
            for (int i = 0; i < results.size(); i++) {
                Order order = results.get(i);
                System.out.printf("%n[%d] %s | %s | $%.2f%n", 
                    i + 1, order.getOrderId(), 
                    order.getCustomer().getName(), 
                    order.totalAmount());
            }
            System.out.println(UIHelper.repeatString("=", 80));
        }
    }
}