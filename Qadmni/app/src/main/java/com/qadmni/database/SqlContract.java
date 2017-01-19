package com.qadmni.database;

/**
 * Created by akshay on 19-01-2017.
 */
public class SqlContract {

    public abstract class SqlMyCart {
        public static final String TABLE_NAME = "my_cart";
        public static final String _ID = "cart_id";
        public static final String PRODUCT_ID = "product_id";
        public static final String PRODUCT_NAME = "product_name";
        public static final String PRODUCT_QTY = "product_quantity";
        public static final String PRODUCER_ID = "producer_id";
    }
}
