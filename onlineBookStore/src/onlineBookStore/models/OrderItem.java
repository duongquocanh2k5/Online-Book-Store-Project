
package onlineBookStore.models;

/**
 * Represents one book + quantity in an order.
 */
public class OrderItem {
    private final Book book;
    private final int quantity;

    public OrderItem(Book book, int quantity) {
        this.book = book;
        this.quantity = quantity;
    }

    public Book getBook() { return book; }
    public int getQuantity() { return quantity; }

    @Override
    public String toString() {
        return String.format("%s x%d", book.getTitle(), quantity); // Book title x quantity
    }
}
