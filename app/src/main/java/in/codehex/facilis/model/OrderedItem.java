package in.codehex.facilis.model;

/**
 * Created by Bobby on 20-05-2016
 */
public class OrderedItem {

    private int id;
    private String name, quantity, brand, description;

    public OrderedItem(int id, String name, String quantity, String brand, String description) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.brand = brand;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
