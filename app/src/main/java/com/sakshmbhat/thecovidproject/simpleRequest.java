package com.sakshmbhat.thecovidproject;

public class simpleRequest {
    private String Department_Name,Item_Name,Item_Quantity;
    public simpleRequest()
    {

    }
    public simpleRequest(String department_Name, String item_Name, String item_Quantity) {
        Department_Name = department_Name;
        Item_Name = item_Name;
        Item_Quantity = item_Quantity;
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
