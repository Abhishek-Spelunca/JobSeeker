package com.example.jobseeker;

public class ProfileHelper {
    String name, phone, age, address, qualify, experience, imageUrl;


    public ProfileHelper(String name, String phone, String age, String address, String qualify, String experience, String imageUrl) {
        this.name = name;
        this.phone = phone;
        this.age = age;
        this.address = address;
        this.qualify = qualify;
        this.experience = experience;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public String getQualify() {
        return qualify;
    }

    public String getExperience() {
        return experience;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
