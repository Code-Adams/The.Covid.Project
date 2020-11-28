package com.sakshmbhat.thecovidproject;

public class RequestData {

    private String Item_Name;
    private String Item_Quantity;
    private String Department_Name;
    private String Generator;
    private String Status;
    private String phoneNumber;
    private String creatorName;

    public String getUserImageurl() {
        return userImageurl;
    }

    public void setUserImageurl(String userImageurl) {
        this.userImageurl = userImageurl;
    }

    private String userImageurl;

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getItem_Name() {
        return Item_Name;
    }

    public void setItem_Name(String item_Name) {
        Item_Name = item_Name;
    }

    public String getItem_Quantity() {
        return Item_Quantity;
    }

    public void setItem_Quantity(String item_Quantity) {
        Item_Quantity = item_Quantity;
    }

    public String getDepartment_Name() {
        return Department_Name;
    }

    public void setDepartment_Name(String department_Name) {
        Department_Name = department_Name;
    }

    public String getGenerator() {
        return Generator;
    }

    public void setGenerator(String generator) {
        Generator = generator;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public RequestData(){}


    public RequestData(String item_Name, String item_Quantity, String department_Name, String generator, String status, String phoneNumber,String creatorName,String imageUrl) {
        Item_Name = item_Name;
        Item_Quantity = item_Quantity;
        Department_Name = department_Name;
        Generator = generator;
        Status = status;
        this.creatorName=creatorName;
        this.phoneNumber = phoneNumber;
        userImageurl=imageUrl;
    }




}
