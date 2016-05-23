package in.codehex.facilis.model;

/**
 * Created by Bobby on 23-05-2016
 */
public class AllBidItem {

    private int id, order, bidById, bidCost;
    private String bidByFirstName, bidByLastName, bidByUserImage, bidTime;

    public AllBidItem(int id, int order, int bidById, int bidCost,
                      String bidByFirstName, String bidByLastName,
                      String bidByUserImage, String bidTime) {
        this.id = id;
        this.order = order;
        this.bidById = bidById;
        this.bidCost = bidCost;
        this.bidByFirstName = bidByFirstName;
        this.bidByLastName = bidByLastName;
        this.bidByUserImage = bidByUserImage;
        this.bidTime = bidTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getBidById() {
        return bidById;
    }

    public void setBidById(int bidById) {
        this.bidById = bidById;
    }

    public int getBidCost() {
        return bidCost;
    }

    public void setBidCost(int bidCost) {
        this.bidCost = bidCost;
    }

    public String getBidByFirstName() {
        return bidByFirstName;
    }

    public void setBidByFirstName(String bidByFirstName) {
        this.bidByFirstName = bidByFirstName;
    }

    public String getBidByLastName() {
        return bidByLastName;
    }

    public void setBidByLastName(String bidByLastName) {
        this.bidByLastName = bidByLastName;
    }

    public String getBidByUserImage() {
        return bidByUserImage;
    }

    public void setBidByUserImage(String bidByUserImage) {
        this.bidByUserImage = bidByUserImage;
    }

    public String getBidTime() {
        return bidTime;
    }

    public void setBidTime(String bidTime) {
        this.bidTime = bidTime;
    }
}
