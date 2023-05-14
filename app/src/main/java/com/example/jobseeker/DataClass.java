package com.example.jobseeker;

public class DataClass {
    String dataTitle;
    String dataCompany;
    String dataType;
    String dataPay;
    String dataLocation;
    String dataDesc;
    String DataCompanyUrl;
    String logo;
    String key;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String date;

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
        return DataCompanyUrl;
    }

    public String getLogo() {
        return logo;
    }

    public DataClass(String dataTitle, String dataCompany, String dataType, String dataPay, String dataLocation, String dataDesc, String dataCompanyUrl, String logo) {
        this.dataTitle = dataTitle;
        this.dataCompany = dataCompany;
        this.dataType = dataType;
        this.dataPay = dataPay;
        this.dataLocation = dataLocation;
        this.dataDesc = dataDesc;
        DataCompanyUrl = dataCompanyUrl;
        this.logo = logo;
    }

    public DataClass(){

    }
}
