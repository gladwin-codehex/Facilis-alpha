package in.codehex.facilis.model;

/**
 * Created by Bobby on 27-05-2016
 */
public class AddressItem {

    private int id, company;
    private String address;

    public AddressItem(int id, int company, String address) {
        this.id = id;
        this.company = company;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCompany() {
        return company;
    }

    public void setCompany(int company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
