package com.example.robowarrior.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.robowarrior.inventoryapp.data.InventoryContract.InventoryEntry;

/**
 * Created by Robo warrior on 07-12-2017.
 */

public class InventoryDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Inventory.db";
    private static final int DATABASE_VERSION = 1;
    public InventoryDbHelper(Context context)
    {

        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        String Create_Dp_Table = "CREATE TABLE " + InventoryEntry.TABLE_NAME + "("
                + InventoryEntry._id+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + InventoryEntry.COLUMN_PRODUCT_NAME  + " TEXT NOT NULL,"
                + InventoryEntry.COLUMN_PRODUCT_PRICE  + " INTEGER NOT NULL,"
                + InventoryEntry.COLUMN_QUANTITY + " INTEGER NOT NULL,"
                + InventoryEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL,"
                + InventoryEntry.COLUMN_SUPPLIER_PHONE+ " INTEGER NOT NULL,"
                + InventoryEntry.COLUMN_SUPPLIER_EMAIL+ " TEXT NOT NULL);";
        db.execSQL(Create_Dp_Table);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
