package ru.alexandertsebenko.shoplist2.datamodel;

import java.util.List;

public class ProdCategory  {

    String name;
    String image;
    List<String> productNames;

    public ProdCategory (String name, String image, List<String> productNames) {

        this.name = name;
        this.image = image;
        this.productNames = productNames;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public List<String> getProductNames() {
        return productNames;
    }
}

