package ru.alexandertsebenko.shoplist2.datamodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс определяющий список, "поход в пятицу", "для нового года"
 * это набор продуктов и дату составления списка
 */
public class ShopList {

    private long id;
    private String name;
    private long dateMilis;
    List<ProductInstance> prodList = new ArrayList<>();

    public ShopList(long id, long date, String name){
        this.id = id;
        this.dateMilis = date;
        this.name = name;
    }
    public void addProductInstance(ProductInstance prodInstance){
        prodList.add(prodInstance);
    }

    public String getName() {
        return name;
    }

    public long getDateMilis() {
        return dateMilis;
    }

    public void setDateMilis(long dateMilis) {
        this.dateMilis = dateMilis;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
