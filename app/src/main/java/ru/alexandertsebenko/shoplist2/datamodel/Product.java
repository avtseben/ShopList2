package ru.alexandertsebenko.shoplist2.datamodel;

public class Product{

    int id;
    String category;
    String name;
    String image;//TODO вынести в Класс ProdCategory

    public Product(String name) {
        this.name = name;
    }

    public Product(String category, String name, String image) {
        this.category = category;
        this.name = name;
        this.image = name;
    }
    public Product(int id, String category, String name, String image) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.image = image;
    }
    public String getCategory() {
        return category;
    }
    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
