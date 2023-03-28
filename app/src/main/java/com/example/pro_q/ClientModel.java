package com.example.pro_q;

public class ClientModel {
    String address, id, fullName;

    public ClientModel(String address, String id, String fullName) {
        this.address = address;
        this.id = id;
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public String toString() {
        return "ClientModel{" +
                "address='" +address +
                ", id='" + id + '\'' +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}

