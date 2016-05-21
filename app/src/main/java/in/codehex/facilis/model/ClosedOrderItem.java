package in.codehex.facilis.model;

/**
 * Created by Bobby on 21-05-2016
 */
public class ClosedOrderItem {

    private int detailsId, detailsOrderId, detailsTotalBids, closedId, closedOrder,
            bidById, closedBidCost;
    private String detailsPostedDate, detailsBiddingTime, detailsLowestBidRate,
            detailsHighestBidRate, detailsAverageBidRate, bidByFirstName, bidByLastName,
            bidByUserImage;

    public ClosedOrderItem(int detailsId, int detailsOrderId, int detailsTotalBids,
                           int closedId, int closedOrder, int bidById, int closedBidCost,
                           String detailsPostedDate, String detailsBiddingTime,
                           String detailsLowestBidRate, String detailsHighestBidRate,
                           String detailsAverageBidRate, String bidByFirstName,
                           String bidByLastName, String bidByUserImage) {
        this.detailsId = detailsId;
        this.detailsOrderId = detailsOrderId;
        this.detailsTotalBids = detailsTotalBids;
        this.closedId = closedId;
        this.closedOrder = closedOrder;
        this.bidById = bidById;
        this.closedBidCost = closedBidCost;
        this.detailsPostedDate = detailsPostedDate;
        this.detailsBiddingTime = detailsBiddingTime;
        this.detailsLowestBidRate = detailsLowestBidRate;
        this.detailsHighestBidRate = detailsHighestBidRate;
        this.detailsAverageBidRate = detailsAverageBidRate;
        this.bidByFirstName = bidByFirstName;
        this.bidByLastName = bidByLastName;
        this.bidByUserImage = bidByUserImage;
    }

    public int getDetailsId() {
        return detailsId;
    }

    public void setDetailsId(int detailsId) {
        this.detailsId = detailsId;
    }

    public int getDetailsOrderId() {
        return detailsOrderId;
    }

    public void setDetailsOrderId(int detailsOrderId) {
        this.detailsOrderId = detailsOrderId;
    }

    public int getDetailsTotalBids() {
        return detailsTotalBids;
    }

    public void setDetailsTotalBids(int detailsTotalBids) {
        this.detailsTotalBids = detailsTotalBids;
    }

    public int getClosedId() {
        return closedId;
    }

    public void setClosedId(int closedId) {
        this.closedId = closedId;
    }

    public int getClosedOrder() {
        return closedOrder;
    }

    public void setClosedOrder(int closedOrder) {
        this.closedOrder = closedOrder;
    }

    public int getBidById() {
        return bidById;
    }

    public void setBidById(int bidById) {
        this.bidById = bidById;
    }

    public int getClosedBidCost() {
        return closedBidCost;
    }

    public void setClosedBidCost(int closedBidCost) {
        this.closedBidCost = closedBidCost;
    }

    public String getDetailsPostedDate() {
        return detailsPostedDate;
    }

    public void setDetailsPostedDate(String detailsPostedDate) {
        this.detailsPostedDate = detailsPostedDate;
    }

    public String getDetailsBiddingTime() {
        return detailsBiddingTime;
    }

    public void setDetailsBiddingTime(String detailsBiddingTime) {
        this.detailsBiddingTime = detailsBiddingTime;
    }

    public String getDetailsLowestBidRate() {
        return detailsLowestBidRate;
    }

    public void setDetailsLowestBidRate(String detailsLowestBidRate) {
        this.detailsLowestBidRate = detailsLowestBidRate;
    }

    public String getDetailsHighestBidRate() {
        return detailsHighestBidRate;
    }

    public void setDetailsHighestBidRate(String detailsHighestBidRate) {
        this.detailsHighestBidRate = detailsHighestBidRate;
    }

    public String getDetailsAverageBidRate() {
        return detailsAverageBidRate;
    }

    public void setDetailsAverageBidRate(String detailsAverageBidRate) {
        this.detailsAverageBidRate = detailsAverageBidRate;
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
}
