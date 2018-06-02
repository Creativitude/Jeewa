package com.creativitude.jeewa.models;

/**
 * Created by naveen on 29/05/2018.
 */

public class Post {

    private String name;
    private String area;
    private String gender;
    private String bloodGroup;
    private String age;
    private String contactPerson;
    private String contactNumber;
    private String relationship;
    private String optionalMessage;
    private String priority;
    private String date;


    public Post() {
    }

    public Post(String name, String area, String gender, String bloodGroup, String age, String contactPerson, String contactNumber, String relationship, String optionalMessage, String priority,String date) {
        this.name = name;
        this.area = area;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.age = age;
        this.contactPerson = contactPerson;
        this.contactNumber = contactNumber;
        this.relationship = relationship;
        this.optionalMessage = optionalMessage;
        this.priority = priority;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getOptionalMessage() {
        return optionalMessage;
    }

    public void setOptionalMessage(String optionalMessage) {
        this.optionalMessage = optionalMessage;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
