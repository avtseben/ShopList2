package ru.alexandertsebenko.shoplist2.datamodel;

import java.util.List;

/**
 * Класс описывающий передачу списка другим людям
 * От кого кому, что купить. Этот объект оптправляется ввиде json
 * на сервер
 */
public class PeoplePleaseBuy {

    People fromWho;
    List<People> toWho;
    List<ProductInstance> pil;

    public PeoplePleaseBuy(People fromWho, List<People> toWho, List<ProductInstance> pil) {
        this.fromWho = fromWho;
        this.toWho = toWho;
        this.pil = pil;
    }

    public People getFromWho() {
        return fromWho;
    }

    public List<People> getToWho() {
        return toWho;
    }

    public List<ProductInstance> getPil() {
        return pil;
    }
}
