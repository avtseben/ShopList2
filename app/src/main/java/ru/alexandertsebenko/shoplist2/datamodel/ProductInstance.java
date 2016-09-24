package ru.alexandertsebenko.shoplist2.datamodel;

/**
 * Класс описывает одну покупку, например:
 *  - Молоко 2 пачки в Ленте
 *  - Килограмм рыбы на рынке
 */
public class ProductInstance {

    private long id;
    private Product product;
    private float quantity;
    private String measure;

    public ProductInstance(long id, Product product, float quantity, String measure) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.measure = measure;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }
}
