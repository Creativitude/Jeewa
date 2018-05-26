package com.creativitude.jeewa.models;

/**
 * Created by naveen on 26/05/2018.
 */

public class User {

    private String name;
    private String age;
    private String contact_no;
    private String district;
    private String bg;
    private String gender;
    private String email;
    private String password;
    private String confirm_password;

    public User(){
        // default constructor
    }

    public User (String name, String age, String contact_no, String district, String bg, String gender, String email, String password, String confirm_password) {
        this.name = name;
        this.age = age;
        this.contact_no = contact_no;
        this.district = district;
        this.bg = bg;
        this.gender = gender;
        this.email = email;
        this.password = password;
        this.confirm_password = confirm_password;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getBg() {
        return bg;
    }

    public void setBg(String bg) {
        this.bg = bg;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirm_password() {
        return confirm_password;
    }

    public void setConfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
    }
}
