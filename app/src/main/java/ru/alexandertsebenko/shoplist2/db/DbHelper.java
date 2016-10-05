package ru.alexandertsebenko.shoplist2.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ru.alexandertsebenko.shoplist2.R;
import ru.alexandertsebenko.shoplist2.datamodel.ProdCategory;
import ru.alexandertsebenko.shoplist2.datamodel.Product;
import ru.alexandertsebenko.shoplist2.utils.ProductXmlParser;

public class DbHelper extends SQLiteOpenHelper{

    Context mContext;
    public static final String TABLE_PRODUCTS = "products";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_NAME = "name";
    public static final String TABLE_SHOP_LISTS = "shop_lists";
    public static final String COLUMN_DATE = "date";
    public static final String TABLE_PRODUCT_INSTANCES = "product_instances";
    public static final String COLUMN_SHOPLIST_ID = "showlist_id";
    public static final String COLUMN_PRODUCT_ID = "product_id";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_MEASURE_ID = "measure_id";
    public static final String COLUMN_CAT_IMAGE = "category_image";
    public static final String COLUMN_STATE = "product_state";

    private static final String DATABASE_NAME = "shoplist.db";
    private static final int DATABASE_VERSION = 7;

    //TODO рефакторить БД вынести категории в отдельную таблицу
    private static final String PRODUCT_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_PRODUCTS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_CATEGORY
            + " text, " + COLUMN_NAME
            + " text, " + COLUMN_CAT_IMAGE
            + " text);";
    private static final String SHOPLIST_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_SHOP_LISTS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_DATE
            + " text, " + COLUMN_NAME
            + " text);";
    private static final String PRODUCT_INSTANCES_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_PRODUCT_INSTANCES + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_SHOPLIST_ID
            + " integer, " + COLUMN_PRODUCT_ID
            + " integer, " + COLUMN_QUANTITY
            + " integer, " + COLUMN_MEASURE_ID
            + " integer, " + COLUMN_STATE
            + " integer);";



    public DbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(PRODUCT_TABLE_CREATE);
        database.execSQL(PRODUCT_INSTANCES_TABLE_CREATE);
        database.execSQL(SHOPLIST_TABLE_CREATE);

        //Load data
        ArrayList<String> inserts = makeArrayOfInserts();
        for (String s : inserts) {
            Log.d(getClass().getSimpleName(),s);
            database.execSQL(s);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_INSTANCES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOP_LISTS);
        onCreate(db);
    }
    private String makeSQLInsert(String category, String name, String image) {
        String s = "INSERT INTO "
                + TABLE_PRODUCTS + " (" +
                COLUMN_CATEGORY + "," +
                COLUMN_NAME + "," +
                COLUMN_CAT_IMAGE + ") " +
                "VALUES ('" + category + "','" + name + "','" + image + "');";
        return s;
    }
    private ArrayList<String> makeArrayOfInserts() {
        StringBuffer sb = new StringBuffer();
        ArrayList<String> inserts = new ArrayList<>();

        InputStream in = mContext.getResources().openRawResource(R.raw.products);
        ProductXmlParser parser = new ProductXmlParser();
        try {
            List list = parser.parse(in);
            for (Object o : list){
                ProdCategory prodCategory = (ProdCategory)o;
                String catName = prodCategory.getName();
                String catImage = prodCategory.getImage();
                for (String prodName : prodCategory.getProductNames()){
                    inserts.add(makeSQLInsert(catName,prodName,catImage));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
/*        inserts.add(makeSQLInsert("bread","хлеб"));
        inserts.add(makeSQLInsert("bread","булка"));
        inserts.add(makeSQLInsert("canned_goods","сайра"));
        inserts.add(makeSQLInsert("chick","курица"));
        inserts.add(makeSQLInsert("coffee","кофе"));
        inserts.add(makeSQLInsert("fish","минтай"));
        inserts.add(makeSQLInsert("fruit","апельсины"));
        inserts.add(makeSQLInsert("groats","лапша"));
        inserts.add(makeSQLInsert("household_chemicals","пемоксоль"));
        inserts.add(makeSQLInsert("hygiene","зубная паста"));
        inserts.add(makeSQLInsert("meat","говядина"));
        inserts.add(makeSQLInsert("milk","молоко"));
        inserts.add(makeSQLInsert("pet","корм adult СС"));
        inserts.add(makeSQLInsert("sauce","майонез"));
        inserts.add(makeSQLInsert("seasoning","прованские травы"));
        inserts.add(makeSQLInsert("sweets","шоколад"));
        inserts.add(makeSQLInsert("vegetables","помидоры"));
        inserts.add(makeSQLInsert("vegetables","очень длинное название продукта, такое что нигде не влезет"));*/
        return inserts;
    }
}
