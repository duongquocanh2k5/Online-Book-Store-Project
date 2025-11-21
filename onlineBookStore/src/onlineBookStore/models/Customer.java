
package onlineBookStore.models;

/**
 * Simple Customer model.
 */
public class Customer {
    private final String id;
    private final String name;
    private final String address;

    public Customer(String id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }

    @Override
    public String toString() {
        return String.format("%s (%s) - %s", name, id, address);
    }
}
