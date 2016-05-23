package in.codehex.facilis.model;

/**
 * Created by Bobby on 23-05-2016
 */
public class OrderItem {

    private int id, orderId, postedById, leastCost, average, counter;
    private String postedByFirstName, postedByLastName, postedDate, days, userImg;

    public OrderItem(int id, int orderId, int postedById,
                     int leastCost, int average, int counter,
                     String postedByFirstName, String postedByLastName,
                     String postedDate, String days, String userImg) {
        this.id = id;
        this.orderId = orderId;
        this.postedById = postedById;
        this.leastCost = leastCost;
        this.average = average;
        this.counter = counter;
        this.postedByFirstName = postedByFirstName;
        this.postedByLastName = postedByLastName;
        this.postedDate = postedDate;
        this.days = days;
        this.userImg = userImg;
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

    public int getPostedById() {
        return postedById;
    }

    public void setPostedById(int postedById) {
        this.postedById = postedById;
    }

    public int getLeastCost() {
        return leastCost;
    }

    public void setLeastCost(int leastCost) {
        this.leastCost = leastCost;
    }

    public int getAverage() {
        return average;
    }

    public void setAverage(int average) {
        this.average = average;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public String getPostedByFirstName() {
        return postedByFirstName;
    }

    public void setPostedByFirstName(String postedByFirstName) {
        this.postedByFirstName = postedByFirstName;
    }

    public String getPostedByLastName() {
        return postedByLastName;
    }

    public void setPostedByLastName(String postedByLastName) {
        this.postedByLastName = postedByLastName;
    }

    public String getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }
}
