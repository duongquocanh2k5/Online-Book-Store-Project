// File: /OnlineBookstore/src/com/onlineBookStore/adt/SimpleStack.java
package onlineBookStore.adt;

import java.util.ArrayList;
import java.util.List;

/**
 * Minimal generic stack implementation.
 */
// đùng LIFO (Last In First Out) - phần tử được thêm vào cuối cùng sẽ được lấy ra đầu tiên
public class SimpleStack<T> {
    private final List<T> container = new ArrayList<>();

    public void push(T item) { container.add(item); }
    public T pop() {
        if (isEmpty()) return null;
        return container.remove(container.size() - 1); //  lấy phần tử cuối cùng trong danh sách và xóa nó khỏi danh sách
    }

    // xem phần tử trên cùng mà không xóa nó
    public T peek() {
        if (isEmpty()) return null;
        return container.get(container.size() - 1); //  lấy phần tử cuối cùng trong danh sách mà không xóa nó, -1 là chỉ số của phần tử cuối cùng
    }

    // kiểm tra xem stack có rỗng không
    public boolean isEmpty() { return container.isEmpty(); } // trả về true nếu stack rỗng
    public int size() { return container.size(); } // trả về kích thước của stack
}
