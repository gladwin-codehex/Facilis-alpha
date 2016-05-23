package in.codehex.facilis.model;

/**
 * Created by Bobby on 23-05-2016
 */
public class ViewBidItem {

    private int id, orderId, delCharge, itemAmount;
    private String name, quantity, brand, description;
    private double percentage;

    public ViewBidItem(double percentage, int id, int orderId, int delCharge, int itemAmount,
                       String name, String quantity, String brand, String description) {
        this.percentage = percentage;
        this.id = id;
        this.orderId = orderId;
        this.delCharge = delCharge;
        this.itemAmount = itemAmount;
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

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getDelCharge() {
        return delCharge;
    }

    public void setDelCharge(int delCharge) {
        this.delCharge = delCharge;
    }

    public int getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(int itemAmount) {
        this.itemAmount = itemAmount;
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

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}
