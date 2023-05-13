package com.example.jobseeker;

public class HelperClass {




    String username, email, pass,uid;

    public HelperClass(String uid,String username, String email, String pass) {
        this.username = username;
        this.email = email;
        this.pass = pass;
        this.uid = uid;
    }
    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }

    public HelperClass(String uid) {
        this.uid = uid;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
