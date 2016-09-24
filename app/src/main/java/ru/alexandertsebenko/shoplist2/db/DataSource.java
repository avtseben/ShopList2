package ru.alexandertsebenko.shoplist2.db;


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

public class DataSource {

    private SQLiteDatabase mDataBase;
    private DbHelper mDbHelper;
    private final String TABLE = DbHelper.TABLE_PRODUCTS;
    private final String PNAME = DbHelper.COLUMN_NAME;
    private final String PCATEG = DbHelper.COLUMN_CATEGORY;
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
                null,//Нужны все поля
                DbHelper.COLUMN_NAME + " LIKE " + "'%" + query + "%'",
                null,null,null,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            list.add(new Product(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2)));
            cursor.moveToNext();
        }
        return list;
    }
}
