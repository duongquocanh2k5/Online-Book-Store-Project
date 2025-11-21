// File: /OnlineBookstore/src/com/onlinebookstore/adt/BookQueue.java
package onlineBookStore.adt;

import onlineBookStore.models.Order;

import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Simple FIFO queue for Orders.
 */
public class BookQueue {
    private final LinkedList<Order> list = new LinkedList<>(); 

    // thêm đơn hàng vào cuối hàng đợi
    public void enqueue(Order order) {
        if (order == null) throw new IllegalArgumentException("order null"); 
        list.addLast(order);
    }
// loại bỏ và trả về đơn hàng ở đầu hàng đợi
    public Order dequeue() {
        if (isEmpty()) throw new NoSuchElementException("queue empty");
        return list.removeFirst();
    }

    public Order peek() {
        if (isEmpty()) return null;
        return list.getFirst();
    }

    public boolean isEmpty() { return list.isEmpty(); }

    public int size() { return list.size(); }
}
