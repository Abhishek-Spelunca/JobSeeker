package com.example.jobseeker;

public class ProfileHelper {
    String name,phone,age,address,qualify;


    public ProfileHelper(String name, String phone, String age, String address, String qualify) {
        this.name = name;
        this.phone = phone;
        this.age = age;
        this.address = address;
        this.qualify = qualify;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getQualify() {
        return qualify;
    }

    public void setQualify(String qualify) {
        this.qualify = qualify;
    }
}