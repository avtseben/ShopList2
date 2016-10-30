package ru.alexandertsebenko.shoplist2.datamodel;

/**
 * Класс описывает одну покупку, например:
 *  - Молоко 2 пачки в Ленте
 *  - Килограмм рыбы на рынке
 *  TODO заменить поле Product на поле String
 */
public class ProductInstance {

    public static final int DELETED = 0;
    public static final int IN_LIST = 1;
    public static final int IN_BASKET = 2;
    public static final int BOUGHT = 3;
    private long id;
    private String globalId;
    private Product product;
    private float quantity;
    private String measure;
    private int state;

    public String getGlobalId() {
        return globalId;
    }

    public void setGlobalId(String globalId) {
        this.globalId = globalId;
    }

    public ProductInstance(long id, Product product, float quantity, String measure, int state) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.measure = measure;
        this.state = state;
    }
    public ProductInstance(String globalId, Product product, float quantity, String measure, int state) {
        this.globalId = globalId;
        this.product = product;
        this.quantity = quantity;
        this.measure = measure;
        this.state = state;
    }
    public ProductInstance(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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
