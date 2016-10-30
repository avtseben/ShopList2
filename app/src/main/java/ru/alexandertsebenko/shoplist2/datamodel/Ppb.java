package ru.alexandertsebenko.shoplist2.datamodel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс описывающий передачу списка другим людям
 * От кого кому, что купить. Этот объект оптправляется ввиде json
 * на сервер
 */
public class Ppb {

    private String from;
    private List<String> to = new ArrayList<String>();
    private List<Pinstance> pil = new ArrayList<Pinstance>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     *
     */
    public Ppb() {
    }

    /**
     *
     * @param to
     * @param pil
     * @param from
     */
    public Ppb(String from, List<String> to, List<Pinstance> pil) {
        this.from = from;
        this.to = to;
        this.pil = pil;
    }

    /**
     *
     * @return
     * The from
     */
    public String getFrom() {
        return from;
    }

    /**
     *
     * @param from
     * The from
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     *
     * @return
     * The to
     */
    public List<String> getTo() {
        return to;
    }

    /**
     *
     * @param to
     * The to
     */
    public void setTo(List<String> to) {
        this.to = to;
    }

    /**
     *
     * @return
     * The pil
     */
    public List<Pinstance> getPinstance() {
        return pil;
    }

    /**
     *
     * @param pil
     * The pil
     */
    public void setPinstance(List<Pinstance> pil) {
        this.pil = pil;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

