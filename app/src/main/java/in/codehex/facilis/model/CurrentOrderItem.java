package in.codehex.facilis.model;

/**
 * Created by Bobby on 19-05-2016
 */
public class CurrentOrderItem {

    private int detailsId, detailsOrderId, detailsTotalBids, detailsDurationStatus;
    private String detailsPostedDate, detailsBiddingTime, detailsLowestBidRate,
            detailsHighestBidRate, detailsAverageBidRate;
    private int statusColor;

    public CurrentOrderItem(int detailsId, int detailsOrderId, int detailsTotalBids,
                            int detailsDurationStatus, String detailsPostedDate,
                            String detailsBiddingTime, String detailsLowestBidRate,
                            String detailsHighestBidRate, String detailsAverageBidRate,
                            int statusColor) {
        this.detailsId = detailsId;
        this.detailsOrderId = detailsOrderId;
        this.detailsTotalBids = detailsTotalBids;
        this.detailsDurationStatus = detailsDurationStatus;
        this.detailsPostedDate = detailsPostedDate;
        this.detailsBiddingTime = detailsBiddingTime;
        this.detailsLowestBidRate = detailsLowestBidRate;
        this.detailsHighestBidRate = detailsHighestBidRate;
        this.detailsAverageBidRate = detailsAverageBidRate;
        this.statusColor = statusColor;
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

    public int getDetailsDurationStatus() {
        return detailsDurationStatus;
    }

    public void setDetailsDurationStatus(int detailsDurationStatus) {
        this.detailsDurationStatus = detailsDurationStatus;
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

    public int getStatusColor() {
        return statusColor;
    }

    public void setStatusColor(int statusColor) {
        this.statusColor = statusColor;
    }
}
