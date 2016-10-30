package ru.alexandertsebenko.shoplist2.datamodel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Класс описывающий передачу списка другим людям
 * От кого кому, что купить. Этот объект оптправляется ввиде json
 * на сервер
 */
public class PeoplePleaseBuy {

    @SerializedName("from")
//    People fromWho;
    String fromWho;
    @SerializedName("to")
    List<String> toWho;

    List<Pinstance> pil;

    public PeoplePleaseBuy(String fromWho, List<String> toWho, List<Pinstance> pil) {
        this.fromWho = fromWho;
        this.toWho = toWho;
        this.pil = pil;
    }

    public String getFromWho() {
        return fromWho;
    }

    public List<String> getToWho() {
        return toWho;
    }

    public List<Pinstance> getPil() {
        return pil;
    }
}
