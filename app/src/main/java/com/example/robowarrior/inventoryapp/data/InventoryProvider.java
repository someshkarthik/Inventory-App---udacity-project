package com.example.robowarrior.inventoryapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Robo warrior on 07-12-2017.
 */

public class InventoryProvider extends ContentProvider {
    private static final int Inventory = 100;
    private static final int Inventory_ID =101;
    private static final UriMatcher sUrimatcher= new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUrimatcher.addURI("com.example.robowarrior.inventoryapp", InventoryContract.InventoryEntry.TABLE_NAME,Inventory);
        sUrimatcher.addURI("com.example.robowarrior.inventoryapp", InventoryContract.InventoryEntry.TABLE_NAME+"/#",Inventory_ID);
    }
    private InventoryDbHelper inventoryDbHelper;
    @Override
    public boolean onCreate() {
        inventoryDbHelper = new InventoryDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase sqLiteDatabase = inventoryDbHelper.getReadableDatabase();
        Cursor cursor;
        int result = sUrimatcher.match(uri);
        switch(result)
        {
            case Inventory:
                cursor = sqLiteDatabase.query(InventoryContract.InventoryEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case Inventory_ID:
                selection = InventoryContract.InventoryEntry._id + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = sqLiteDatabase.query(InventoryContract.InventoryEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown uri"+uri);

        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int result = sUrimatcher.match(uri);
        switch(result)
        {
            case Inventory:
                return insertProducts(uri, values);

            default:
                throw new IllegalArgumentException("Connot insert unknown uri"+uri);
        }
    }
    private Uri insertProducts(Uri uri,ContentValues contentValues)
    {           SQLiteDatabase sqLiteDatabase = inventoryDbHelper.getReadableDatabase();
        Long rowID= sqLiteDatabase.insert(InventoryContract.InventoryEntry.TABLE_NAME,null,contentValues);
        getContext().getContentResolver().notifyChange(uri,null);
        return Uri.withAppendedPath(uri,rowID.toString());

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        SQLiteDatabase sqLiteDatabase  = inventoryDbHelper.getWritableDatabase();
        int rowId = sqLiteDatabase.delete(InventoryContract.InventoryEntry.TABLE_NAME,selection,selectionArgs);
        getContext().getContentResolver().notifyChange(uri,null);
        return rowId;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = inventoryDbHelper.getWritableDatabase();

        int rowId=sqLiteDatabase.update(InventoryContract.InventoryEntry.TABLE_NAME,values,selection,selectionArgs);
        getContext().getContentResolver().notifyChange(uri,null);
        return rowId;
    }
}
