package in.codehex.facilis.model;

/**
 * Created by Bobby on 23-05-2016
 */
public class CompanyInfoItem {

    private int companyId, companyZipCode;
    private String firstName, lastName, email, userImage, companyEmail, companyName, companyUrl,
            companyContact, companyStreet, companyCity, companyState, companyCountry;
    private boolean companyRole;

    public CompanyInfoItem(int companyId, int companyZipCode, String firstName,
                           String lastName, String email, String userImage,
                           String companyEmail, String companyName, String companyUrl,
                           String companyContact, String companyStreet, String companyCity,
                           String companyState, String companyCountry, boolean companyRole) {
        this.companyId = companyId;
        this.companyZipCode = companyZipCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userImage = userImage;
        this.companyEmail = companyEmail;
        this.companyName = companyName;
        this.companyUrl = companyUrl;
        this.companyContact = companyContact;
        this.companyStreet = companyStreet;
        this.companyCity = companyCity;
        this.companyState = companyState;
        this.companyCountry = companyCountry;
        this.companyRole = companyRole;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getCompanyZipCode() {
        return companyZipCode;
    }

    public void setCompanyZipCode(int companyZipCode) {
        this.companyZipCode = companyZipCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyUrl() {
        return companyUrl;
    }

    public void setCompanyUrl(String companyUrl) {
        this.companyUrl = companyUrl;
    }

    public String getCompanyContact() {
        return companyContact;
    }

    public void setCompanyContact(String companyContact) {
        this.companyContact = companyContact;
    }

    public String getCompanyStreet() {
        return companyStreet;
    }

    public void setCompanyStreet(String companyStreet) {
        this.companyStreet = companyStreet;
    }

    public String getCompanyCity() {
        return companyCity;
    }

    public void setCompanyCity(String companyCity) {
        this.companyCity = companyCity;
    }

    public String getCompanyState() {
        return companyState;
    }

    public void setCompanyState(String companyState) {
        this.companyState = companyState;
    }

    public String getCompanyCountry() {
        return companyCountry;
    }

    public void setCompanyCountry(String companyCountry) {
        this.companyCountry = companyCountry;
    }

    public boolean isCompanyRole() {
        return companyRole;
    }

    public void setCompanyRole(boolean companyRole) {
        this.companyRole = companyRole;
    }
}
