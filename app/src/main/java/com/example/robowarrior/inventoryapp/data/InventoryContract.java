package com.example.robowarrior.inventoryapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Robo warrior on 07-12-2017.
 */

public final class InventoryContract  {
    private InventoryContract()
    {

    }
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://com.example.robowarrior.inventoryapp/");
    private static final String Path_Pets = "inventoryapp";
    public static final class InventoryEntry implements BaseColumns{
        public static final Uri Content_Uri = Uri.withAppendedPath(BASE_CONTENT_URI,Path_Pets);
        public static final String TABLE_NAME = "inventoryapp";
        public final static String _id = BaseColumns._ID;
        public final static String COLUMN_PRODUCT_NAME = "ProductName";
        public final static String  COLUMN_PRODUCT_PRICE = "Price";
        public final static String COLUMN_QUANTITY = "Quantity";
        public final static String COLUMN_SUPPLIER_NAME = "SupplierName";
        public final static String COLUMN_SUPPLIER_PHONE = "SupplierPhone";
        public final static String COLUMN_SUPPLIER_EMAIL = "SupplierEmail";

    }
}
