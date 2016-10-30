package ru.alexandertsebenko.shoplist2.datamodel;

public class People {

    String fullName;
    String number;
    boolean selected;
    boolean accounted;

    public People(String fullName, String number) {
        this.fullName = fullName;
        this.number = number;
        accounted = false;//TODO заглушка
    }
    public String getFullName() {
        return fullName;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }
    public boolean isAccounted() {
        return accounted;
    }

    public String getNumber() {
        return number;
    }

}
