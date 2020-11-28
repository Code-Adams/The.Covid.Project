package com.sakshmbhat.thecovidproject;

public class simpleRequest {
    private String Department_Name,Item_Name,Item_Quantity;
    private String Generator;

    public simpleRequest(String department_Name, String item_Name, String item_Quantity, String generator, String phoneNumber) {
        Department_Name = department_Name;
        Item_Name = item_Name;
        Item_Quantity = item_Quantity;
        Generator = generator;
        this.phoneNumber = phoneNumber;
    }

    private String phoneNumber;
    public simpleRequest()
    {

    }

    public String getGenerator() {
        return Generator;
    }

    public void setGenerator(String generator) {
        Generator = generator;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDepartment_Name() {
        return Department_Name;
    }

    public void setDepartment_Name(String department_Name) {
        Department_Name = department_Name;
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
}
