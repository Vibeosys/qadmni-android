package com.qadmni.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.qadmni.data.ItemListDetailsDTO;
import com.qadmni.data.MyCartDTO;
import com.qadmni.data.requestDataDTO.OrderItemDTO;
import com.qadmni.data.responseDataDTO.ItemInfoList;
import com.qadmni.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 19-01-2017.
 */
public class QadmniHelper extends SQLiteOpenHelper {

    private static final String TAG = QadmniHelper.class.getSimpleName();
    private final static String DATABASE_NAME = "qadmni.db";

    private final String CREATE_MY_CART = "CREATE TABLE IF NOT EXISTS " + SqlContract.SqlMyCart.TABLE_NAME
            + "(" + SqlContract.SqlMyCart._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + SqlContract.SqlMyCart.PRODUCT_ID +
            "  INT NOT NULL," + SqlContract.SqlMyCart.PRODUCT_NAME +
            "  VARCHAR(45) NOT NULL," + SqlContract.SqlMyCart.PRODUCT_QTY +
            "  INT NULL DEFAULT 0," + SqlContract.SqlMyCart.PRODUCER_ID +
            "  INT NULL," + SqlContract.SqlMyCart.ITEM_UNIT_PRICE + " INT NULL DEFAULT 0)";

    private final String CREATE_MY_FAV = "CREATE TABLE IF NOT EXISTS " + SqlContract.SqlMyFav.TABLE_NAME +
            "(" + SqlContract.SqlMyFav._ID + " INTEGER PRIMARY KEY)";

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
        try {
            db.execSQL(CREATE_MY_FAV);
            Log.d(TAG, "##Signal Table Create " + CREATE_MY_FAV);
        } catch (SQLiteException e) {
            Log.e(TAG, "##Could not create my_fav table" + e.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        try {
            db.execSQL(CREATE_MY_FAV);
            Log.d(TAG, "##Signal Table Create " + CREATE_MY_FAV);
        } catch (SQLiteException e) {
            Log.e(TAG, "##Could not create my_fav table" + e.toString());
        }
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
                contentValues.put(SqlContract.SqlMyCart.PRODUCER_ID, itemDetails.getProducerDetails().getProducerId());
                contentValues.put(SqlContract.SqlMyCart.ITEM_UNIT_PRICE, itemDetails.getUnitPrice());
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


    public ArrayList<MyCartDTO> getCartDetails() {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        int rowCount = 0;
        long count = -1;
        ArrayList<MyCartDTO> myCartDTOs = new ArrayList<>();
        Cursor cursor = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                cursor = sqLiteDatabase.rawQuery("select * from " + SqlContract.SqlMyCart.TABLE_NAME, null);
                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();

                        do {
                            int myCartId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlMyCart._ID));
                            int productId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlMyCart.PRODUCT_ID));
                            int producerId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlMyCart.PRODUCER_ID));
                            String productName = cursor.getString(cursor.getColumnIndex(SqlContract.SqlMyCart.PRODUCT_NAME));
                            int productQyt = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlMyCart.PRODUCT_QTY));
                            String productUnitPrice = cursor.getString(cursor.getColumnIndex(SqlContract.SqlMyCart.ITEM_UNIT_PRICE));

                            MyCartDTO myCartDTO = new MyCartDTO(producerId, productQyt, productUnitPrice, productName, productId);
                            myCartDTOs.add(myCartDTO);
                        } while (cursor.moveToNext());
                    }
                }
                flagError = true;
            }
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e(TAG, "Error at getOrdersOf table " + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            return myCartDTOs;
        }
    }


    public ArrayList<OrderItemDTO> getItemsList() {
        boolean flagError = false;
        String errorMessage = "";
        ArrayList<OrderItemDTO> paymentModeList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                cursor = sqLiteDatabase.rawQuery("Select * From " + SqlContract.SqlMyCart.TABLE_NAME,
                        null);
                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();

                        do {
                            long itemId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlMyCart.PRODUCT_ID));
                            int qty = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlMyCart.PRODUCT_QTY));
                            OrderItemDTO orderItemDTO = new OrderItemDTO(itemId, qty);
                            paymentModeList.add(orderItemDTO);
                        } while (cursor.moveToNext());
                    }
                }
                flagError = true;
                //cursor.close();
                //sqLiteDatabase.close();
            }
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e(TAG, "Error at getPaymentList table " + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                Log.e(TAG, "Add payment List" + errorMessage);
        }
        return paymentModeList;
    }

    public int getItemQuantity(long id) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        int qty = 0;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                cursor = sqLiteDatabase.rawQuery("Select * From " + SqlContract.SqlMyCart.TABLE_NAME + " where " + SqlContract.SqlMyCart.PRODUCT_ID + "=?",
                        new String[]{String.valueOf(id)});
                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        qty = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlMyCart.PRODUCT_QTY));
                    }
                }
                flagError = true;
                //cursor.close();
                //sqLiteDatabase.close();
            }
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e(TAG, "Error at getPaymentList table " + e.toString());
        } finally

        {
            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                Log.e(TAG, "Add payment List" + errorMessage);
        }
        return qty;
    }


    public boolean deleteMyCartDetails() {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        sqLiteDatabase = getWritableDatabase();
        long count = -1;
        try {
            synchronized (sqLiteDatabase) {
                count = sqLiteDatabase.delete(SqlContract.SqlMyCart.TABLE_NAME,
                        null, null);
                Log.d(TAG, " ## delete my cart successfully");
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            e.printStackTrace();
            Log.d(TAG, "## delete my cart not successfully" + e.toString());
            //sqLiteDatabase.close();

        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                Log.e(TAG, "Delete Customer" + errorMessage);
        }
        return count != -1;
    }

    public int getCountCartTable() {
        int count = 0;
        Cursor cursor = null;
        SQLiteDatabase sqLiteDatabase = null;
        sqLiteDatabase = getReadableDatabase();
        try {
            synchronized (sqLiteDatabase) {
                cursor = sqLiteDatabase.rawQuery("Select * From " + SqlContract.SqlMyCart.TABLE_NAME,
                        null);
                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        count = cursor.getCount();
                    }
                }
            }
        } catch (Exception e) {
            Log.d("TAG", e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
                sqLiteDatabase.close();
            }
        }
        return count;
    }

    public boolean deleteMyCartDetailsWithWhere(long id) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        sqLiteDatabase = getWritableDatabase();
        long count = -1;
        try {
            synchronized (sqLiteDatabase) {
                count = sqLiteDatabase.delete(SqlContract.SqlMyCart.TABLE_NAME,
                        SqlContract.SqlMyCart.PRODUCT_ID + "=?", new String[]{String.valueOf(id)});
                Log.d(TAG, " ## delete my cart successfully" + id);
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            e.printStackTrace();
            Log.d(TAG, "## delete my cart not successfully" + e.toString());
            //sqLiteDatabase.close();

        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                Log.e(TAG, "Delete Customer" + errorMessage);
        }
        return count != -1;
    }

    public boolean editCart(MyCartDTO myCartDTO) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        int rowCount = 0;
        long count = -1;
        int qty = myCartDTO.getItemQuantity();
        try {
            String[] whereClause = new String[]{String.valueOf(myCartDTO.getProductId())};
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
                contentValues.put(SqlContract.SqlMyCart.PRODUCT_ID, myCartDTO.getProductId());
                contentValues.put(SqlContract.SqlMyCart.PRODUCT_NAME, myCartDTO.getItemName());
                contentValues.put(SqlContract.SqlMyCart.PRODUCT_QTY, qty);
                contentValues.put(SqlContract.SqlMyCart.PRODUCER_ID, myCartDTO.getProducerId());
                contentValues.put(SqlContract.SqlMyCart.ITEM_UNIT_PRICE, myCartDTO.getUnitPrice());
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

    public boolean insertFavList(List<ItemInfoList> itemListDetailsDTOs) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (ItemInfoList itemListDetailsDTO : itemListDetailsDTOs) {
                    contentValues.put(SqlContract.SqlMyFav._ID, itemListDetailsDTO.getItemId());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.insert(SqlContract.SqlMyFav.TABLE_NAME, null, contentValues);
                    contentValues.clear();
                    Log.d(TAG, "## Table Item is added successfully" + itemListDetailsDTO.getItemId());

                }
                flagError = true;
            }

        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.d(TAG, "## Error at insert in fav" + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                FirebaseCrash.log(TAG + " error insert in fav" + flagError);
        }
        return count != -1;
    }

    public boolean getMyFavItem(long itemId) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        boolean isPresent = false;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                cursor = sqLiteDatabase.rawQuery("Select * From " + SqlContract.SqlMyFav.TABLE_NAME + " where " + SqlContract.SqlMyFav._ID + "=?",
                        new String[]{String.valueOf(itemId)});
                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        isPresent = true;
                    }
                }
                flagError = true;
                //cursor.close();
                //sqLiteDatabase.close();
            }
        } catch (Exception e) {
            isPresent = false;
            flagError = false;
            errorMessage = e.getMessage();
            Log.e(TAG, "Error at getPaymentList table " + e.toString());
        } finally

        {
            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                Log.e(TAG, "Add payment List" + errorMessage);
        }
        return isPresent;
    }

    public boolean insertFav(long itemId) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();

                contentValues.put(SqlContract.SqlMyFav._ID, itemId);
                if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                count = sqLiteDatabase.insert(SqlContract.SqlMyFav.TABLE_NAME, null, contentValues);
                contentValues.clear();
                Log.d(TAG, "## Table Item is added successfully" + itemId);
                flagError = true;
            }

        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.d(TAG, "## Error at insert in fav" + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                FirebaseCrash.log(TAG + " error insert in fav" + flagError);
        }
        return count != -1;
    }

    public boolean deleteMyFav(long id) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        sqLiteDatabase = getWritableDatabase();
        long count = -1;
        try {
            synchronized (sqLiteDatabase) {
                count = sqLiteDatabase.delete(SqlContract.SqlMyFav.TABLE_NAME,
                        SqlContract.SqlMyFav._ID + "=?", new String[]{String.valueOf(id)});
                Log.d(TAG, " ## delete my Fav successfully" + id);
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            e.printStackTrace();
            Log.d(TAG, "## delete my Fav not successfully" + e.toString());
            //sqLiteDatabase.close();

        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                Log.e(TAG, "Delete my fav" + errorMessage);
        }
        return count != -1;
    }

    public boolean deleteMyFav() {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        sqLiteDatabase = getWritableDatabase();
        long count = -1;
        try {
            synchronized (sqLiteDatabase) {
                count = sqLiteDatabase.delete(SqlContract.SqlMyFav.TABLE_NAME,
                        null, null);
                Log.d(TAG, " ## delete my fav successfully");
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            e.printStackTrace();
            Log.d(TAG, "## delete my fav not successfully" + e.toString());
            //sqLiteDatabase.close();

        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                Log.e(TAG, "Delete fav" + errorMessage);
        }
        return count != -1;
    }
}
