package com.sakshmbhat.thecovidproject;

public class userDataDuplicate {

    private String imageUrl,firstName,lastName,DOB,department,phoneNumber,address,apartment;

    private String UID,Requests;

    //Empty Constructor
    public userDataDuplicate(){}
    //Parameterised Constructor

    public userDataDuplicate( String firstName, String lastName,String imageUrl,String uID,String DOB, String department, String phoneNumber, String address, String apartment,String noOfRequests) {
        this.imageUrl = imageUrl;
        this.firstName = firstName;
        this.lastName = lastName;
        this.DOB = DOB;
        this.department = department;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.apartment = apartment;
        this.Requests=noOfRequests;
        this.UID=uID;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getRequests() {
        return Requests;
    }

    public void setRequests(String requests) {
        Requests = requests;
    }
}
