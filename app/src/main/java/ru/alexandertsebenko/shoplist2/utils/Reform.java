package ru.alexandertsebenko.shoplist2.utils;

/**
 * Created by avtseben on 06.11.2016.
 */
public class Reform {

    private static final String FIRST_PREFIX = "8";
    private static final int LENGTH_ELEVEN = 11;
    /**
     * Приоводим номера телефонов к единому формату
     * напрмер в списке контактов телефонной книге номера
     * могут быть:
     * +7(913)-504-33-44
     * 89033433256
     * 8-4215-345023
     *
     * метод приводит номера к единому формату
     * 89133166336
     * 11 цыфр, первая 8, нет пробелов дефисов и скобок
     * @param inNumber
     * @return
     */
    public static String normalizeNumber(String inNumber) {
        StringBuilder sb = new StringBuilder();
        sb.append(inNumber.replaceAll("\\D", ""));
        if(sb.length() == 10){
            sb.insert(0,FIRST_PREFIX);
        } else if (sb.length() == 11 && !(sb.substring(0,1).equals(FIRST_PREFIX))){
            sb.replace(0,1,FIRST_PREFIX);
        }
        return sb.toString();
    }
}
