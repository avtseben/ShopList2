package ru.alexandertsebenko.shoplist2.datamodel;

public class People {

    String fullName;
    String number;

    public People(String fullName, String number) {

        this.fullName = fullName;
        this.number = number;
    }
    public String getFullName() {
        return fullName;
    }

    public String getNumber() {
        return number;
    }

}
