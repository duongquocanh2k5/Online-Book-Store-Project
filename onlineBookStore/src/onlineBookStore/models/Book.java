
    package onlineBookStore.models; 

// book model class
public class Book {
    private final String id;
    private final String title;
    private final String author;
    private double price;
    private int stock;

    public Book(String id, String title, String author, double price, int stock) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.stock = stock;
    }
//
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }

    public void setPrice(double price) { this.price = price; }


    // reduce stock by qty if possible
    public boolean reduceStock(int qty) {
        if (qty <= 0) return false;
        if (stock >= qty) {
            stock -= qty;
            return true;
        }
        return false;
    }
// increase stock by qty 
    public void increaseStock(int qty) {
        if (qty > 0) stock += qty;
    }

    // 
    @Override
    public String toString() {
        return String.format("%s | %s | %s | $%.2f | stock=%d", id, title, author, price, stock);
    }
}
