package com.example.doanmobile.Model;

public class LoginRespon {

    private int Id;
    private String Name ;
    private String Status;

    public String getStatus() {
        return Status;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
