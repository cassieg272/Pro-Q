package com.example.pro_q;

public class ClientModel {
    String city, province, postalCode, street, address;
    String id;
    String firstName;
    String lastName;
    public ClientModel() {

    }

    public ClientModel(String address, String id, String firstName, String lastName) {
        this.address = address;
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

//    public ClientModel(String city, String province, String postalCode, String street, String id, String firstName, String lastName) {
//        this.city = city;
//        this.province = province;
//        this.postalCode = postalCode;
//        this.street = street;
//        this.id = id;
//        this.firstName = firstName;
//        this.lastName = lastName;
//    }

    public String getAddress() {
        return address;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return "ClientModel{" +
                "address='" +address +
                ", id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}

