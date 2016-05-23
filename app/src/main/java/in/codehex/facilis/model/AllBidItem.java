package in.codehex.facilis.model;

/**
 * Created by Bobby on 23-05-2016
 */
public class AllBidItem {

    private int id, order, bidById, bidCost;
    private String bidByFirstName, bidByLastName, bidByUserImg, bidTime;

    public AllBidItem(int id, int order, int bidById, int bidCost,
                      String bidByFirstName, String bidByLastName,
                      String bidByUserImg, String bidTime) {
        this.id = id;
        this.order = order;
        this.bidById = bidById;
        this.bidCost = bidCost;
        this.bidByFirstName = bidByFirstName;
        this.bidByLastName = bidByLastName;
        this.bidByUserImg = bidByUserImg;
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

    public String getBidByUserImg() {
        return bidByUserImg;
    }

    public void setBidByUserImg(String bidByUserImg) {
        this.bidByUserImg = bidByUserImg;
    }

    public String getBidTime() {
        return bidTime;
    }

    public void setBidTime(String bidTime) {
        this.bidTime = bidTime;
    }
}
