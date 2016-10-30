package ru.alexandertsebenko.shoplist2.datamodel;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс экземпляра покупки для передачи json
 *
 */
public class Pinstance {

    private String globalId;
    private String product;
    private float quantity;
    private String measure;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Pinstance() {
    }

    /**
     *
     * @param product
     * @param measure
     * @param quantity
     * @param globalId
     */
    public Pinstance(String globalId, String product, float quantity, String measure) {
        this.globalId = globalId;
        this.product = product;
        this.quantity = quantity;
        this.measure = measure;
    }

    /**
     *
     * @return
     * The globalId
     */
    public String getGlobalId() {
        return globalId;
    }

    /**
     *
     * @param globalId
     * The globalId
     */
    public void setGlobalId(String globalId) {
        this.globalId = globalId;
    }

    /**
     *
     * @return
     * The product
     */
    public String getProduct() {
        return product;
    }

    /**
     *
     * @param product
     * The product
     */
    public void setProduct(String product) {
        this.product = product;
    }

    /**
     *
     * @return
     * The quantity
     */
    public float getQuantity() {
        return quantity;
    }

    /**
     *
     * @param quantity
     * The quantity
     */
    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    /**
     *
     * @return
     * The measure
     */
    public String getMeasure() {
        return measure;
    }

    /**
     *
     * @param measure
     * The measure
     */
    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
