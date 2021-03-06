package in.codehex.facilis.model;

/**
 * Created by Bobby on 21-05-2016
 */
public class SelectedOrderItem {

    private int detailsId, detailsOrderId, detailsTotalBids, detailsOrderStatus,
            paymentStatusColor, deliveryStatusColor, selectedId, selectedOrder,
            bidById, selectedBidCost;
    private String detailsPostedDate, detailsBiddingTime, detailsLowestBidRate,
            detailsHighestBidRate, detailsAverageBidRate, bidByFirstName, bidByLastName,
            bidByUserImage;
    private boolean detailsPaidStatus;

    public SelectedOrderItem(int detailsId, int detailsOrderId, int detailsTotalBids,
                             int detailsOrderStatus, int paymentStatusColor,
                             int deliveryStatusColor, int selectedId, int selectedOrder,
                             int bidById, int selectedBidCost, String detailsPostedDate,
                             String detailsBiddingTime, String detailsLowestBidRate,
                             String detailsHighestBidRate, String detailsAverageBidRate,
                             String bidByFirstName, String bidByLastName,
                             String bidByUserImage, boolean detailsPaidStatus) {
        this.detailsId = detailsId;
        this.detailsOrderId = detailsOrderId;
        this.detailsTotalBids = detailsTotalBids;
        this.detailsOrderStatus = detailsOrderStatus;
        this.paymentStatusColor = paymentStatusColor;
        this.deliveryStatusColor = deliveryStatusColor;
        this.selectedId = selectedId;
        this.selectedOrder = selectedOrder;
        this.bidById = bidById;
        this.selectedBidCost = selectedBidCost;
        this.detailsPostedDate = detailsPostedDate;
        this.detailsBiddingTime = detailsBiddingTime;
        this.detailsLowestBidRate = detailsLowestBidRate;
        this.detailsHighestBidRate = detailsHighestBidRate;
        this.detailsAverageBidRate = detailsAverageBidRate;
        this.bidByFirstName = bidByFirstName;
        this.bidByLastName = bidByLastName;
        this.bidByUserImage = bidByUserImage;
        this.detailsPaidStatus = detailsPaidStatus;
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

    public int getDetailsOrderStatus() {
        return detailsOrderStatus;
    }

    public void setDetailsOrderStatus(int detailsOrderStatus) {
        this.detailsOrderStatus = detailsOrderStatus;
    }

    public int getPaymentStatusColor() {
        return paymentStatusColor;
    }

    public void setPaymentStatusColor(int paymentStatusColor) {
        this.paymentStatusColor = paymentStatusColor;
    }

    public int getDeliveryStatusColor() {
        return deliveryStatusColor;
    }

    public void setDeliveryStatusColor(int deliveryStatusColor) {
        this.deliveryStatusColor = deliveryStatusColor;
    }

    public int getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(int selectedId) {
        this.selectedId = selectedId;
    }

    public int getSelectedOrder() {
        return selectedOrder;
    }

    public void setSelectedOrder(int selectedOrder) {
        this.selectedOrder = selectedOrder;
    }

    public int getBidById() {
        return bidById;
    }

    public void setBidById(int bidById) {
        this.bidById = bidById;
    }

    public int getSelectedBidCost() {
        return selectedBidCost;
    }

    public void setSelectedBidCost(int selectedBidCost) {
        this.selectedBidCost = selectedBidCost;
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

    public boolean isDetailsPaidStatus() {
        return detailsPaidStatus;
    }

    public void setDetailsPaidStatus(boolean detailsPaidStatus) {
        this.detailsPaidStatus = detailsPaidStatus;
    }
}
