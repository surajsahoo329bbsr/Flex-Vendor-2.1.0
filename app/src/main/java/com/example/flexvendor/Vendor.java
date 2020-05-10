package com.example.flexvendor;

public class Vendor {

    private String vendorId;
    private String vendorName;
    private String vendorMail;
    private String vendorPhone;
    private int companyId;
    private int vendorPhotoFlag;

    //Constructor
    //Function Overloading(Look it up)

    //Another constructor to initialize our varibales
    Vendor(String vendorId, String userName, String userMail, String userPhone, int companyId, int vendorPhotoFlag) {
        this.vendorId=vendorId;
        this.vendorName= userName;
        this.vendorMail= userMail;
        this.vendorPhone= userPhone;
        this.companyId=companyId;
        this.vendorPhotoFlag=vendorPhotoFlag;
    }

    public String getVendorId() {
        return vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public String getVendorMail() {
        return vendorMail;
    }

    public String getVendorPhone() {
        return vendorPhone;
    }

    public int getCompanyId() {
        return companyId;
    }

    public int getVendorPhotoFlag() { return vendorPhotoFlag; }

    public void setCompanyId(int companyId) {
        this.companyId=companyId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId=vendorId;
    }

    public void setVendorName(String vendorName) {
        this.vendorName=vendorName;
    }

    public void setVendorMail(String vendorMail) {
        this.vendorMail=vendorMail;
    }

    public void setVendorPhone(String vendorPhone) {
        this.vendorPhone=vendorPhone;
    }

    public void setVendorPhotoFlag(int vendorPhotoFlag) {
        this.vendorPhotoFlag=vendorPhotoFlag;
    }

}
