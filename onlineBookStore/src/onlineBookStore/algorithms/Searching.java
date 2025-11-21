// File: /OnlineBookstore/src/com/onlinebookstore/algorithms/Searching.java
package onlineBookStore.algorithms;

import onlineBookStore.models.Book;
import onlineBookStore.models.Order;

import java.util.List;

/**
 * Searching utilities.
 */
public class Searching {

    // Linear search order by orderId in a list
    public static Order linearSearchOrderById(List<Order> orders, String orderId) {
        if (orders == null || orderId == null) return null;
        for (Order o : orders) {
            if (orderId.equals(o.getOrderId())) return o;
        }
        return null; 
    }

    // Binary search for book title in sorted Book[] (sorted by title)
    public static int binarySearchBookByTitle(Book[] arr, String title) {
        int lo = 0, hi = arr.length - 1;// Khởi tạo hai biến lo và hi để đại diện cho phạm vi tìm kiếm trong mảng arr. 
        while (lo <= hi) {
            int mid = (lo + hi) / 2; // Tính chỉ số giữa mid của phạm vi hiện tại.
            int cmp = arr[mid].getTitle().compareToIgnoreCase(title);
            if (cmp == 0) return mid; // Nếu tiêu đề sách tại vị trí mid bằng với tiêu đề cần tìm, trả về chỉ số mid.
            if (cmp < 0) lo = mid + 1;// nhỏ hơn, điều chỉnh phạm vi tìm kiếm sang bên phải bằng cách đặt lo thành mid + 1.
            else hi = mid - 1;
        }
        return -1;
        // Nếu tìm thấy tiêu đề sách, nó trả về chỉ số của sách trong mảng; nếu không, nó trả về -1.
    }
}
