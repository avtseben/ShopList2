package ru.alexandertsebenko.shoplist2.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.widget.CursorAdapter;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import ru.alexandertsebenko.shoplist2.datamodel.PeoplePleaseBuy;
import ru.alexandertsebenko.shoplist2.datamodel.Pinstance;
import ru.alexandertsebenko.shoplist2.datamodel.Product;
import ru.alexandertsebenko.shoplist2.datamodel.ProductInstance;
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
        ArrayList<Product> list = new ArrayList<>();
        String queryFistCharUpperCase = query.substring(0,1).toUpperCase() + query.substring(1);
        Cursor cursor = mDataBase.query(
                TABLE,
                new String[]{
                        DbHelper.COLUMN_ID,
                        DbHelper.COLUMN_CATEGORY,
                        DbHelper.COLUMN_NAME,
                        DbHelper.COLUMN_CAT_IMAGE},
                DbHelper.COLUMN_NAME + " LIKE " + "'%" + query + "%' OR " +
                DbHelper.COLUMN_NAME + " LIKE " + "'%" + queryFistCharUpperCase + "%'",
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
    public Product getProductById (long id) {
        Cursor cursor = mDataBase.query(DbHelper.TABLE_PRODUCTS,
                new String[]{DbHelper.COLUMN_ID,
                    DbHelper.COLUMN_CATEGORY,
                    DbHelper.COLUMN_NAME,
                    DbHelper.COLUMN_CAT_IMAGE},
                DbHelper.COLUMN_ID + " = " + id,
                null,null,null,null);
        cursor.moveToFirst();
        int c = cursor.getColumnCount();
        System.out.println("count " + c);
        return new Product(cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3));
    }
    public String getMeasureById(long id) {
        Cursor cursor = mDataBase.query(DbHelper.TABLE_MEASURES,
                new String[]{DbHelper.COLUMN_NAME},
                DbHelper.COLUMN_ID + " = " + id,
                null,null,null,null);
        cursor.moveToFirst();
        return cursor.getString(0);
    }
    //TODO переделать всё на AsyncTask
    public long addNewShopList(String listName,long date){
        ContentValues values = new ContentValues();
        values.put(DbHelper.COLUMN_NAME,listName);
        values.put(DbHelper.COLUMN_DATE,date);

        return mDataBase.insert(SL_TABLE,null,values);
    }
    public long addProductInstance(long listId, long productId,
                                   int quantity, String measure, int state,
                                   String uuid){
        ContentValues values = new ContentValues();
        values.put(DbHelper.COLUMN_SHOPLIST_ID,listId);
        values.put(DbHelper.COLUMN_PRODUCT_ID,productId);
        values.put(DbHelper.COLUMN_QUANTITY,quantity);
        values.put(DbHelper.COLUMN_MEASURE_ID,1);//TODO заглушка - убрать
        values.put(DbHelper.COLUMN_STATE,state);
        values.put(DbHelper.COLUMN_GLOBAL_UUID,uuid);

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
    public long getListSizeByShopListId(long id) {
        SQLiteStatement s = mDataBase.compileStatement( "select count(*) from " +
                DbHelper.TABLE_PRODUCT_INSTANCES +
                " where " +
                DbHelper.COLUMN_SHOPLIST_ID +
                " = " + id + ";");
         return s.simpleQueryForLong();
    }
    public List<ShopList> getAllLists() {
        List<ShopList> list = new ArrayList<>();
        Cursor cursor = mDataBase.query(DbHelper.TABLE_SHOP_LISTS,
                new String[]{DbHelper.COLUMN_ID,DbHelper.COLUMN_DATE,DbHelper.COLUMN_NAME},
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

    public List<ShopList> getAllListsWithInnerList() {
        List<ShopList> list = new ArrayList<>();
        Cursor cursor = mDataBase.query(DbHelper.TABLE_SHOP_LISTS,
                new String[]{DbHelper.COLUMN_ID,DbHelper.COLUMN_DATE,DbHelper.COLUMN_NAME},
                null,null,null,null,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            long shListId = cursor.getLong(0);
            list.add(new ShopList(
                    shListId,
                    cursor.getLong(1),
                    cursor.getString(2),
                    getProductInstancesByShopListId(shListId)));
            cursor.moveToNext();
        }
        return list;
    }
    public void deleteShopListById(long id) {
        mDataBase.delete(DbHelper.TABLE_SHOP_LISTS,
                DbHelper.COLUMN_ID + " = " + id,null);
    }

    public List<ProductInstance> getProductInstancesByShopListId(long id) {
        ArrayList<ProductInstance> list = new ArrayList<>();
        Cursor cursor = mDataBase.query(DbHelper.TABLE_PRODUCT_INSTANCES,
                new String[]{DbHelper.COLUMN_ID,
                        DbHelper.COLUMN_PRODUCT_ID,
                        DbHelper.COLUMN_QUANTITY,
                        DbHelper.COLUMN_MEASURE_ID,
                        DbHelper.COLUMN_STATE},
                DbHelper.COLUMN_SHOPLIST_ID+ " = " + id,
                null,null,null,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            list.add(new ProductInstance(
                    cursor.getLong(0),
                    getProductById(cursor.getLong(1)),
                    cursor.getFloat(2),
                    getMeasureById(cursor.getLong(3)),
                    cursor.getInt(4)));
            cursor.moveToNext();
        }
        return list;

    }

    public List<Pinstance> getPinstancesByShopListId(long id) {
        ArrayList<Pinstance> list = new ArrayList<>();
        Cursor cursor = mDataBase.query(DbHelper.TABLE_PRODUCT_INSTANCES,
                new String[]{
                        DbHelper.COLUMN_GLOBAL_UUID,
                        DbHelper.COLUMN_PRODUCT_ID,
                        DbHelper.COLUMN_QUANTITY,
                        DbHelper.COLUMN_MEASURE_ID},
                DbHelper.COLUMN_SHOPLIST_ID + " = " + id,
                null,null,null,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            list.add(new Pinstance(
                    cursor.getString(0),
                    getProductById(cursor.getLong(1)).getName(),
                    cursor.getFloat(2),
                    getMeasureById(cursor.getLong(3))));
            cursor.moveToNext();
        }
        return list;
    }
    public long getProductIdByProductName(String name){
        Cursor cursor = mDataBase.query(DbHelper.TABLE_PRODUCTS,
                new String[]{DbHelper.COLUMN_ID},
                DbHelper.COLUMN_NAME + " = " + "'" + name + "'",
                null,null,null,null);
        cursor.moveToFirst();
        return cursor.getLong(0);
    }

    public void saveNewListFromPpb(PeoplePleaseBuy ppb) {
        long shopListId = addNewShopList("От " + ppb.getFromWho(), System.currentTimeMillis());
        for (Pinstance pi : ppb.getPil()){
             addProductInstance(shopListId,
                     getProductIdByProductName(pi.getProduct()),
                     1,//TODO в срочном порядке !!! Какого фига я здесь в базе делал INT. Переделать на Float
                     pi.getMeasure(),
                     ProductInstance.IN_LIST,
                     pi.getGlobalId());
        }

    }
}
