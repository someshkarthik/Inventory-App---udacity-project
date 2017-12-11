package com.example.robowarrior.inventoryapp.data;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.robowarrior.inventoryapp.R;

import org.w3c.dom.Text;

/**
 * Created by Robo warrior on 07-12-2017.
 */

public class InventoryCursor extends CursorAdapter {
    public InventoryCursor(Context context,Cursor cursor)
    {
        super(context,cursor,0);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_layout,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv1 = (TextView) view.findViewById(R.id.product_name);
        TextView tv2 = (TextView) view.findViewById(R.id.product_price);
        TextView tv3 = (TextView) view.findViewById(R.id.product_count);
        String ProductName = cursor.getString(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME));
        String ProductPrice = "Rs."+cursor.getString(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE));
        String Quantity = cursor.getString(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.COLUMN_QUANTITY));
        tv1.setText(ProductName);
        tv2.setText(ProductPrice);
        tv3.setText(Quantity);

    }
}
