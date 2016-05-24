package in.codehex.facilis.model;

/**
 * Created by Bobby on 24-05-2016
 */
public class SelectedBidItem {

    private int id, bidCost, orderId, orderOrderId, postedById, orderRating, orderCreditPeriod,
            orderPreviousRecord, orderItemCount, paymentStatusColor;
    private String postedByFirstName, postedByLastName, postedByUserImage, orderPostedDate,
            creditStatus;
    private boolean orderCreditFacility, orderPaymentStatus;

    public SelectedBidItem(int id, int bidCost, int orderId, int orderOrderId, int postedById,
                           int orderRating, int orderCreditPeriod, int orderPreviousRecord,
                           int orderItemCount, int paymentStatusColor, String postedByFirstName,
                           String postedByLastName, String postedByUserImage,
                           String orderPostedDate, String creditStatus,
                           boolean orderCreditFacility, boolean orderPaymentStatus) {
        this.id = id;
        this.bidCost = bidCost;
        this.orderId = orderId;
        this.orderOrderId = orderOrderId;
        this.postedById = postedById;
        this.orderRating = orderRating;
        this.orderCreditPeriod = orderCreditPeriod;
        this.orderPreviousRecord = orderPreviousRecord;
        this.orderItemCount = orderItemCount;
        this.paymentStatusColor = paymentStatusColor;
        this.postedByFirstName = postedByFirstName;
        this.postedByLastName = postedByLastName;
        this.postedByUserImage = postedByUserImage;
        this.orderPostedDate = orderPostedDate;
        this.creditStatus = creditStatus;
        this.orderCreditFacility = orderCreditFacility;
        this.orderPaymentStatus = orderPaymentStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBidCost() {
        return bidCost;
    }

    public void setBidCost(int bidCost) {
        this.bidCost = bidCost;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderOrderId() {
        return orderOrderId;
    }

    public void setOrderOrderId(int orderOrderId) {
        this.orderOrderId = orderOrderId;
    }

    public int getPostedById() {
        return postedById;
    }

    public void setPostedById(int postedById) {
        this.postedById = postedById;
    }

    public int getOrderRating() {
        return orderRating;
    }

    public void setOrderRating(int orderRating) {
        this.orderRating = orderRating;
    }

    public int getOrderCreditPeriod() {
        return orderCreditPeriod;
    }

    public void setOrderCreditPeriod(int orderCreditPeriod) {
        this.orderCreditPeriod = orderCreditPeriod;
    }

    public int getOrderPreviousRecord() {
        return orderPreviousRecord;
    }

    public void setOrderPreviousRecord(int orderPreviousRecord) {
        this.orderPreviousRecord = orderPreviousRecord;
    }

    public int getOrderItemCount() {
        return orderItemCount;
    }

    public void setOrderItemCount(int orderItemCount) {
        this.orderItemCount = orderItemCount;
    }

    public int getPaymentStatusColor() {
        return paymentStatusColor;
    }

    public void setPaymentStatusColor(int paymentStatusColor) {
        this.paymentStatusColor = paymentStatusColor;
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

    public String getPostedByUserImage() {
        return postedByUserImage;
    }

    public void setPostedByUserImage(String postedByUserImage) {
        this.postedByUserImage = postedByUserImage;
    }

    public String getOrderPostedDate() {
        return orderPostedDate;
    }

    public void setOrderPostedDate(String orderPostedDate) {
        this.orderPostedDate = orderPostedDate;
    }

    public String getCreditStatus() {
        return creditStatus;
    }

    public void setCreditStatus(String creditStatus) {
        this.creditStatus = creditStatus;
    }

    public boolean isOrderCreditFacility() {
        return orderCreditFacility;
    }

    public void setOrderCreditFacility(boolean orderCreditFacility) {
        this.orderCreditFacility = orderCreditFacility;
    }

    public boolean isOrderPaymentStatus() {
        return orderPaymentStatus;
    }

    public void setOrderPaymentStatus(boolean orderPaymentStatus) {
        this.orderPaymentStatus = orderPaymentStatus;
    }
}
