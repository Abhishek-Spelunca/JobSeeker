package com.example.jobseeker;

public class DataClass {

    String dataTitle;
    String dataCompany;
    String dataType;
    String dataPay;
    String dataLocation;
    String dataDesc;

    String dataDate;
    String dataCompanyUrl;
    String logo;
    String key;

    public DataClass(String title, String company, String type, String payScale, String location, String desc, String date, String companyUrl, String imageUrl) {
        this.dataTitle = title;
        this.dataCompany = company;
        this.dataType = type;
        this.dataPay = payScale;
        this.dataLocation = location;
        this.dataDesc = desc;
        this.dataDate = date;
        this.dataCompanyUrl = companyUrl;
        this.logo = imageUrl;
        this.key = getKey();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDataTitle() {
        return dataTitle;
    }

    public String getDataCompany() {
        return dataCompany;
    }

    public String getDataType() {
        return dataType;
    }

    public String getDataPay() {
        return dataPay;
    }

    public String getDataLocation() {
        return dataLocation;
    }

    public String getDataDesc() {
        return dataDesc;
    }

    public String getDataCompanyUrl() {
        return dataCompanyUrl;
    }

    public String getLogo() {
        return logo;
    }

    public String getDataDate() {
        return dataDate;
    }


    public DataClass(){

    }
}
