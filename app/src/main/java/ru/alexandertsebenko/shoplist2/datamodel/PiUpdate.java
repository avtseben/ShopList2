package ru.alexandertsebenko.shoplist2.datamodel;

import java.util.HashMap;

/**
 * Класс запрос на обновление покупок. Содержит информацию о том
 * кто что купил или удалил из списка
 * piUpdatesMap; - Мар в котором ключ - GlobalId покупки а значение - новый статус
 */
public class PiUpdate {

    People whoUpdate;
    HashMap<String,String> piUpdatesMap;

    public PiUpdate(People whoUpdate, HashMap<String, String> piUpdatesMap) {
        this.whoUpdate = whoUpdate;
        this.piUpdatesMap = piUpdatesMap;
    }
}
