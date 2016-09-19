package ru.alexandertsebenko.shoplist2.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import ru.alexandertsebenko.shoplist2.datamodel.Product;

public class DbHelper extends SQLiteOpenHelper{

    public static final String TABLE_PRODUCTS = "products";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_NAME = "name";

    private static final String DATABASE_NAME = "shoplist.db";
    private static final int DATABASE_VERSION = 2;

    private static final String PRODUCT_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_PRODUCTS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_CATEGORY
            + " text, " + COLUMN_NAME
            + " text);";

    private String makeSQLInsert(String category, String name) {
        String s = "INSERT INTO "
                + TABLE_PRODUCTS + " (" +
                COLUMN_CATEGORY + "," +
                COLUMN_NAME + ") " +
                "VALUES ('" + category + "','" + name + "');";
        return s;
    }

    public DbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(PRODUCT_TABLE_CREATE);
        ArrayList<String> inserts = makeArrayOfInserts();
        for (String s : inserts) {
            Log.d(getClass().getSimpleName(),s);
            database.execSQL(s);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }
    private ArrayList<String> makeArrayOfInserts() {
        StringBuffer sb = new StringBuffer();
        ArrayList<String> inserts = new ArrayList<>();
        inserts.add(makeSQLInsert("bread","хлеб"));
        inserts.add(makeSQLInsert("bread","булка"));
        inserts.add(makeSQLInsert("canned_goods","сайра"));
        inserts.add(makeSQLInsert("chick","курица"));
        inserts.add(makeSQLInsert("coffe","кофе"));
        inserts.add(makeSQLInsert("fish","минтай"));
        inserts.add(makeSQLInsert("friut","апельсины"));
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
        return inserts;
    }
}
