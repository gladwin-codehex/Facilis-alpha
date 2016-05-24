package in.codehex.facilis.model;

/**
 * Created by Bobby on 23-05-2016
 */
public class ViewOrderItem {

    private int id, orderId, postedById, rating, creditPeriod, itemCount, previousRecord, colorIndicator;
    private String postedByFirstName, postedByLastName, postedByUserImage, postedDate, biddingDuration;
    private boolean creditFacility;

    public ViewOrderItem(int id, int orderId, int postedById, int rating, int creditPeriod, int itemCount,
                         int previousRecord, int colorIndicator, String postedByFirstName,
                         String postedByLastName, String postedByUserImage, String postedDate,
                         String biddingDuration, boolean creditFacility) {
        this.id = id;
        this.orderId = orderId;
        this.postedById = postedById;
        this.rating = rating;
        this.creditPeriod = creditPeriod;
        this.itemCount = itemCount;
        this.previousRecord = previousRecord;
        this.colorIndicator = colorIndicator;
        this.postedByFirstName = postedByFirstName;
        this.postedByLastName = postedByLastName;
        this.postedByUserImage = postedByUserImage;
        this.postedDate = postedDate;
        this.biddingDuration = biddingDuration;
        this.creditFacility = creditFacility;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getCreditPeriod() {
        return creditPeriod;
    }

    public void setCreditPeriod(int creditPeriod) {
        this.creditPeriod = creditPeriod;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public int getPreviousRecord() {
        return previousRecord;
    }

    public void setPreviousRecord(int previousRecord) {
        this.previousRecord = previousRecord;
    }

    public int getColorIndicator() {
        return colorIndicator;
    }

    public void setColorIndicator(int colorIndicator) {
        this.colorIndicator = colorIndicator;
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

    public String getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    public String getBiddingDuration() {
        return biddingDuration;
    }

    public void setBiddingDuration(String biddingDuration) {
        this.biddingDuration = biddingDuration;
    }

    public boolean isCreditFacility() {
        return creditFacility;
    }

    public void setCreditFacility(boolean creditFacility) {
        this.creditFacility = creditFacility;
    }
}
