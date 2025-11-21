package onlineBookStore;

import onlineBookStore.managers.CartManager;
import onlineBookStore.managers.InventoryManager;
import onlineBookStore.managers.OrderHistoryManager;
import onlineBookStore.managers.UIHelper;
import onlineBookStore.models.Customer;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        InventoryManager inventoryMgr = new InventoryManager();
        OrderHistoryManager orderMgr = new OrderHistoryManager();
        UIHelper ui = new UIHelper(scanner);
        
        ui.showWelcome();
        Customer customer = ui.getCustomerInfo();
        
        CartManager cartMgr = new CartManager(customer, inventoryMgr, orderMgr);
        
        boolean running = true;
        while (running) {
            ui.showMenu();
            int choice = ui.getIntInput("Select option (1-9): ");  
            
            switch (choice) {
                case 1:
                    inventoryMgr.viewInventory();
                    break;
                case 2:
                    cartMgr.addBookToCart(scanner);
                    break;
                case 3:
                    cartMgr.viewCart();
                    break;
                case 4:
                    cartMgr.undo();
                    break;
                case 5:
                    cartMgr.checkout();
                    break;
                case 6:
                    inventoryMgr.addNewBook(scanner);
                    break;
                case 7:
                    orderMgr.viewOrderHistory();
                    break;
                case 8:
                    orderMgr.searchOrder(scanner);
                    break;
                
                case 9:  
                    running = false;
                    System.out.println("\nThank you for shopping! Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option!");
            }
        }
        
        scanner.close();
    }
}