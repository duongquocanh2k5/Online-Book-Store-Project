
package onlineBookStore.service;

import onlineBookStore.adt.BookQueue;
import onlineBookStore.models.Book;
import onlineBookStore.models.Order;
import onlineBookStore.models.OrderItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Processes orders from a BookQueue: checks stock, reduces stock, sorts items.
 */
public class OrderProcessor {

    private final Book[] inventory; // reference to inventory
    private final BookQueue queue;
    private final List<Order> processedOrders = new ArrayList<>();

    public OrderProcessor(Book[] inventory, BookQueue queue) {
        this.inventory = inventory;
        this.queue = queue;
    }

    // Try to process next order in queue
    public void processNextOrder() {
        if (queue.isEmpty()) return;
        Order order = queue.dequeue();
        order.setStatus(Order.Status.PROCESSING);
        boolean ok = checkAndReserve(order);
        if (ok) {
            // sort items by book title for shipping optimization
            sortOrderItemsByTitle(order);
            order.setStatus(Order.Status.COMPLETED);
        } else {
            order.setStatus(Order.Status.FAILED);
        }
        processedOrders.add(order);
        System.out.println("Processed: " + order.getOrderId() + " -> " + order.getStatus());
    }

    private boolean checkAndReserve(Order order) {
        // For each item, find book in inventory and reduce stock if available
        for (OrderItem oi : order.getItems()) {
            Book book = findBookById(oi.getBook().getId());
            if (book == null || book.getStock() < oi.getQuantity()) {
                return false;
            }
        }
        // If all ok, reduce stock
        for (OrderItem oi : order.getItems()) {
            Book book = findBookById(oi.getBook().getId());
            book.reduceStock(oi.getQuantity());
        }
        return true;
    }

    private Book findBookById(String id) {
        for (Book b : inventory) if (b.getId().equals(id)) return b;
        return null;
    }

    private void sortOrderItemsByTitle(Order order) {
        order.getItems().sort((a, b) -> a.getBook().getTitle().compareToIgnoreCase(b.getBook().getTitle()));
    }

    public List<Order> getProcessedOrders() { return processedOrders; }
}
