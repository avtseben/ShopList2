package ru.alexandertsebenko.shoplist2.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.CursorAdapter;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import ru.alexandertsebenko.shoplist2.datamodel.Product;
import ru.alexandertsebenko.shoplist2.datamodel.ShopList;

public class DataSource {

    private SQLiteDatabase mDataBase;
    private DbHelper mDbHelper;
    private final String TABLE = DbHelper.TABLE_PRODUCTS;
    private final String SL_TABLE = DbHelper.TABLE_SHOP_LISTS;
    private final String PNAME = DbHelper.COLUMN_NAME;
    private final String PCATEG = DbHelper.COLUMN_CATEGORY;
    private final String PCATEGIMG = DbHelper.COLUMN_CAT_IMAGE;
    private final String ID = DbHelper.COLUMN_ID;


    public DataSource (Context context) {
        mDbHelper = new DbHelper(context);
    }
    public void open() throws SQLException {
        if((mDataBase = mDbHelper.getWritableDatabase()) == null)
            mDataBase = mDbHelper.getReadableDatabase();
        Log.d(getClass().getSimpleName(), "DataSource opened");
    }
    public void close() {
        mDbHelper.close();
    }

    public Cursor getMatches(String query){
        return mDataBase.query(TABLE,
                new String[]{ID,PNAME},//Для CursorAdapter обязательно нужен ID
                DbHelper.COLUMN_NAME + " LIKE " + "'%" + query + "%'",
                null,null,null,null);
    }
    public Cursor getAllProducts(){
        return mDataBase.query(TABLE,
                new String[]{ID,PNAME,PCATEG},
                null,null,null,null,null);
    }
    public List<Product> getProductByNameMatches(String query) {
        Log.d(getClass().getSimpleName(),"query: " + query);
        ArrayList<Product> list = new ArrayList<>();
        Cursor cursor = mDataBase.query(TABLE,
                null,//Нужны все поля//TODO переделать нужно явно указывать поля
                DbHelper.COLUMN_NAME + " LIKE " + "'%" + query + "%'",
                null,null,null,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            list.add(new Product(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)));
            cursor.moveToNext();
        }
        return list;
    }
    //TODO переделать всё на AsyncTask
    public long addNewShopList(String listName,long date){
        ContentValues values = new ContentValues();
        values.put(DbHelper.COLUMN_NAME,listName);
        values.put(DbHelper.COLUMN_DATE,date);

        return mDataBase.insert(SL_TABLE,null,values);
    }
    public long addProductInstance(long listId, long productId,
                                   int quantity, String measure, int state){
        ContentValues values = new ContentValues();
        values.put(DbHelper.COLUMN_SHOPLIST_ID,listId);
        values.put(DbHelper.COLUMN_PRODUCT_ID,productId);
        values.put(DbHelper.COLUMN_QUANTITY,quantity);
        values.put(DbHelper.COLUMN_MEASURE_ID,1);//TODO заглушка - убрать
        values.put(DbHelper.COLUMN_STATE,state);

        return mDataBase.insert(DbHelper.TABLE_PRODUCT_INSTANCES,null,values);
    }

    public void updateProductInstanceState(long id, int state) {
        ContentValues values = new ContentValues();
        values.put(DbHelper.COLUMN_STATE,state);

        mDataBase.update(DbHelper.TABLE_PRODUCT_INSTANCES,
                values,DbHelper.COLUMN_ID + " = " + id, null);
    }
    public void deleteProductInstanceById(long id) {
        mDataBase.delete(DbHelper.TABLE_PRODUCT_INSTANCES,
                DbHelper.COLUMN_ID + " = " + id, null);
    }

    public List<ShopList> getAllLists() {
        List<ShopList> list = new ArrayList<>();
        Cursor cursor = mDataBase.query(DbHelper.TABLE_SHOP_LISTS,
                new String[]{DbHelper.COLUMN_ID,DbHelper.COLUMN_NAME,DbHelper.COLUMN_DATE},
                null,null,null,null,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            list.add(new ShopList(
                    cursor.getLong(0),
                    cursor.getLong(1),
                    cursor.getString(2)));
            cursor.moveToNext();
        }
        return list;
    }

    public void deleteShopListById(long id) {
        mDataBase.delete(DbHelper.TABLE_SHOP_LISTS,
                DbHelper.COLUMN_ID + " = " + id,null);
    }
}
