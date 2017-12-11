package com.example.robowarrior.inventoryapp;

import android.app.Dialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.robowarrior.inventoryapp.data.InventoryContract;
import com.example.robowarrior.inventoryapp.data.InventoryCursor;
import com.example.robowarrior.inventoryapp.data.InventoryDbHelper;

public class InventoryLogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private final static int Url_loader = 0;
    ListView listView;
    InventoryCursor inventoryCursor;
    InventoryDbHelper inventoryDbHelper;
    Dialog dialog;
    String toastmsg;
    String[] projection = {
            InventoryContract.InventoryEntry._ID,
            InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME,
            InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE,
            InventoryContract.InventoryEntry.COLUMN_QUANTITY,
            InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME,
            InventoryContract.InventoryEntry.COLUMN_SUPPLIER_EMAIL,
            InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_log);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InventoryLogActivity.this, InventoryInputActivity.class));
            }
        });

        listView = (ListView) findViewById(R.id.listview);
        inventoryCursor = new InventoryCursor(this, null);
        listView.setAdapter(inventoryCursor);
        listView.setEmptyView(findViewById(R.id.empty_view));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int quantity = 0;
                String selection = InventoryContract.InventoryEntry._id + "=?";
                String[] SelectionArgs = new String[]{String.valueOf(id)};
                Cursor cursor = getContentResolver().query(InventoryContract.InventoryEntry.Content_Uri, projection, selection, SelectionArgs, null);
                if (cursor.moveToFirst()) {
                    quantity = cursor.getInt(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.COLUMN_QUANTITY));
                    --quantity;
                }
                if (quantity < 0) {
                    Toast.makeText(getApplicationContext(), "Product Not Available, PLEASE BUY!!", Toast.LENGTH_SHORT).show();
                } else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(InventoryContract.InventoryEntry.COLUMN_QUANTITY, quantity);
                    int updatedRowId = getContentResolver().update(InventoryContract.InventoryEntry.Content_Uri, contentValues, selection, SelectionArgs);
                }

            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(InventoryLogActivity.this, ProductDetailActivity.class);
                Uri EditUri = ContentUris.withAppendedId(InventoryContract.InventoryEntry.Content_Uri, id);
                positionprovider.mposition = position;
                intent.setData(EditUri);
                startActivity(intent);
                return true;
            }
        });
        dialog = new Dialog(this);
        inventoryDbHelper = new InventoryDbHelper(this);
        //listView.setEmptyView(findViewById(R.id.));
        getLoaderManager().initLoader(Url_loader, null, this);
    }

    public void showPopUp() {
        TextView txtclose;

        dialog.setContentView(R.layout.activity_pop_up);
        txtclose = (TextView) dialog.findViewById(R.id.txtclose);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_inventory_log, menu);
        return true;

    }

    public void deletePrompt() {
        dialog.setContentView(R.layout.delete_prompt);
        Button button1 = (Button) dialog.findViewById(R.id.cancel);
        Button button2 = (Button) dialog.findViewById(R.id.delete);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct();
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void deleteProduct() {

        int rowId = getContentResolver().delete(InventoryContract.InventoryEntry.Content_Uri, null, null);
        Toast.makeText(getApplicationContext(), "All Products deleted", Toast.LENGTH_SHORT).show();
    }

    public void InsertDummyData() {
        Uri uri;
        int rowId;

        ContentValues contentValues = new ContentValues();
        contentValues.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME, "CocaCola");
        contentValues.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE, "23");
        contentValues.put(InventoryContract.InventoryEntry.COLUMN_QUANTITY, 30);
        contentValues.put(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME, "Bala");
        contentValues.put(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE, "+919458392984");
        contentValues.put(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_EMAIL, "baladd123@gmail.com");
        uri = getContentResolver().insert(InventoryContract.InventoryEntry.Content_Uri, contentValues);
        rowId = Integer.parseInt(String.valueOf(ContentUris.parseId(uri)));
        if (rowId >= 0)
            toastmsg = "Dummy data inserted";
        else
            toastmsg = "Error inserting the data";
    }

    public void instructionsPopUp() {

        TextView txtclose;

        dialog.setContentView(R.layout.instructions_dialog);
        txtclose = (TextView) dialog.findViewById(R.id.txtclose);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_about:
                showPopUp();
                return true;
            case R.id.deleteall:
                deletePrompt();
                return true;
            case R.id.Insert_Dummy_Data:
                InsertDummyData();
                Toast.makeText(getApplicationContext(), toastmsg, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.Instructions:
                instructionsPopUp();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case Url_loader:
                return new CursorLoader(this, InventoryContract.InventoryEntry.Content_Uri, projection, null, null, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        inventoryCursor.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        inventoryCursor.swapCursor(null);

    }

    public static final class positionprovider {
        public static int mposition;
    }
}
