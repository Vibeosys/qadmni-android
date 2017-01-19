package com.qadmni.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.qadmni.data.ItemListDetailsDTO;
import com.qadmni.utils.SessionManager;

import java.util.ArrayList;

/**
 * Created by akshay on 19-01-2017.
 */
public class QadmniHelper extends SQLiteOpenHelper {

    private static final String TAG = QadmniHelper.class.getSimpleName();
    private final static String DATABASE_NAME = "qadmni.db";

    private final String CREATE_MY_CART = "CREATE TABLE IF NOT EXISTS " + SqlContract.SqlMyCart.TABLE_NAME
            + "(" + SqlContract.SqlMyCart._ID + " INT NOT NULL ," + SqlContract.SqlMyCart.PRODUCT_ID +
            "  INT NOT NULL," + SqlContract.SqlMyCart.PRODUCT_NAME +
            "  VARCHAR(45) NOT NULL," + SqlContract.SqlMyCart.PRODUCT_QTY +
            "  INT NULL DEFAULT 0," + SqlContract.SqlMyCart.PRODUCER_ID +
            "  INT NULL," +
            "  PRIMARY KEY (" + SqlContract.SqlMyCart._ID + "));";

    public QadmniHelper(Context context, SessionManager sessionManager) {
        super(context, DATABASE_NAME, null, sessionManager.getDatabaseVersion());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_MY_CART);
            Log.d(TAG, "##Signal Table Create " + CREATE_MY_CART);
        } catch (SQLiteException e) {
            Log.e(TAG, "##Could not create myCart table" + e.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void getDatabaseStructure() {
        final ArrayList<String> dirArray = new ArrayList<String>();

        SQLiteDatabase DB = getReadableDatabase();
        //SQLiteDatabase DB = sqlHelper.getWritableDatabase();
        Cursor c = DB.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        c.moveToFirst();
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                dirArray.add(c.getString(c.getColumnIndex("name")));

                c.moveToNext();
            }
        }
        Log.i(TAG, "##" + dirArray);
        c.close();

    }


    public boolean insertOrUpdateCart(ItemListDetailsDTO itemDetails) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        int rowCount = 0;
        long count = -1;
        int qty = itemDetails.getQuantity();
        try {
            String[] whereClause = new String[]{String.valueOf(itemDetails.getItemId())};
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                Cursor cursor = sqLiteDatabase.rawQuery("Select * From " + SqlContract.SqlMyCart.TABLE_NAME
                        + " Where " + SqlContract.SqlMyCart.PRODUCT_ID + "=?", whereClause);
                rowCount = cursor.getCount();
                cursor.close();
                sqLiteDatabase.close();
            }

            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                contentValues.put(SqlContract.SqlMyCart.PRODUCT_ID, itemDetails.getItemId());
                contentValues.put(SqlContract.SqlMyCart.PRODUCT_NAME, itemDetails.getItemName());
                contentValues.put(SqlContract.SqlMyCart.PRODUCT_QTY, qty);
                contentValues.put(SqlContract.SqlMyCart.PRODUCER_ID, itemDetails.getProducerId());
                if (rowCount == 0 && qty != 0) {
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.insert(SqlContract.SqlMyCart.TABLE_NAME, null, contentValues);
                } else if (qty == 0) {
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.delete(SqlContract.SqlMyCart.TABLE_NAME,
                            SqlContract.SqlMyCart.PRODUCT_ID + "=?", whereClause);
                } else {
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.update(SqlContract.SqlMyCart.TABLE_NAME, contentValues,
                            SqlContract.SqlMyCart.PRODUCT_ID + "=? ", whereClause);
                }

                flagError = true;
                contentValues.clear();
                Log.i(TAG, "Values are inserted into TempTable");
            }
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e(TAG, "error at insertOrUpdateTempOrder");
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                Log.e(TAG, "Insert Or Update Temp Order");
        }

        return count != -1;
    }
}
