package ru.alexandertsebenko.shoplist2.datamodel;

public class Product{

    int id;
    String category;
    String name;

    public Product(String name) {
        this.name = name;
    }

    public Product(String category, String name) {
        this.category = category;
        this.name = name;
    }
    public Product(int id, String category, String name) {
        this.id = id;
        this.category = category;
        this.name = name;
    }
    public String getCategory() {
        return category;
    }
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
