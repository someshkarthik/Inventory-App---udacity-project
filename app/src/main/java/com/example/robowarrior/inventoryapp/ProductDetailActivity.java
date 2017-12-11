package com.example.robowarrior.inventoryapp;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.robowarrior.inventoryapp.data.InventoryContract;

public class ProductDetailActivity extends AppCompatActivity {
    String[] projection = {
            InventoryContract.InventoryEntry._ID,
            InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME,
            InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE,
            InventoryContract.InventoryEntry.COLUMN_QUANTITY,
            InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME,
            InventoryContract.InventoryEntry.COLUMN_SUPPLIER_EMAIL,
            InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE,
    };
    Uri uri;
    Cursor cursor;
    TextView tv1, tv2, tv3, tv4, tv5, tv6;
    ImageButton imageButton;
    ImageButton callbutton;
    String selection;
    String[] selectionArgs;
    Dialog dialog;
    public ProductDetailActivity() {

    }

    public ProductDetailActivity(long id) {
        deleteProduct();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        final Intent intent = getIntent();
        uri = intent.getData();
        show(uri);
        dialog = new Dialog(this);
        setTitle("Product Details");
        imageButton = (ImageButton) findViewById(R.id.intent_email);
       final ImageButton imageButton1 =(ImageButton) findViewById(R.id.detail_fab);
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = new AlphaAnimation(1.0f,0.0f);
                animation.setDuration(500);
                imageButton1.startAnimation(animation);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        long id = Long.valueOf(selectionArgs[0]);
                        Intent intent = new Intent(ProductDetailActivity.this, InventoryInputActivity.class);
                        Uri detailUri = ContentUris.withAppendedId(InventoryContract.InventoryEntry.Content_Uri, id);
                        intent.setData(detailUri);
                        startActivity(intent);
                        finish();
                    }
                },500);

            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = new AlphaAnimation(1.0f,0.0f);
                animation.setDuration(500);
                imageButton.startAnimation(animation);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent in = new Intent(Intent.ACTION_SENDTO);
                        String msg;
                        msg = "Product Name:" + tv1.getText().toString();
                        msg += "\n" + "Price:" + tv2.getText().toString();
                        msg += "\n" + "Required quantity: ";
                        String[] to = new String[]{tv6.getText().toString()};
                        in.setData(Uri.parse("mailto:"));
                        in.putExtra(Intent.EXTRA_EMAIL, to);
                        in.putExtra(Intent.EXTRA_SUBJECT, "In Need of more supplies");
                        in.putExtra(Intent.EXTRA_TEXT, msg);
                        if (in.resolveActivity(getPackageManager()) != null)
                            startActivity(in);
                    }
                },500);


            }
        });
        callbutton = (ImageButton) findViewById(R.id.intent_phone);
        callbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = new AlphaAnimation(1.0f,0.0f);
                animation.setDuration(500);
                callbutton.startAnimation(animation);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + tv5.getText().toString()));
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                    }
                },500);

            }
        });
        final ImageButton imageButton2 = (ImageButton) findViewById(R.id.action_delete_product);
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = new AlphaAnimation(1.0f,0.0f);
                animation.setDuration(500);
                imageButton2.startAnimation(animation);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        deletePrompt();
                    }
                },500);


            }
        });

    }
    public void show(Uri uri) {
        selection = InventoryContract.InventoryEntry._id + "=?";
        helperclass.id = ContentUris.parseId(uri);
        helperclass.uri = InventoryContract.InventoryEntry.Content_Uri;
        selectionArgs = new String[]{String.valueOf(helperclass.id)};
        cursor = getContentResolver().query(InventoryContract.InventoryEntry.Content_Uri, projection, selection, selectionArgs, null);
        if (cursor.moveToFirst()) {
            tv1 = (TextView) findViewById(R.id.detail_product_name);
            tv2 = (TextView) findViewById(R.id.detail_product_price);
            tv3 = (TextView) findViewById(R.id.detail_product_quantity);
            tv4 = (TextView) findViewById(R.id.detail_supplier_name);
            tv5 = (TextView) findViewById(R.id.detail_supplier_phone);
            tv6 = (TextView) findViewById(R.id.detail_supplier_Email);
            tv1.setText(cursor.getString(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME)));
            tv2.setText("Rs." + cursor.getString(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE)));
            tv3.setText(cursor.getString(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.COLUMN_QUANTITY)));
            tv4.setText(cursor.getString(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME)));
            tv5.setText(cursor.getString(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE)));
            tv6.setText(cursor.getString(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_EMAIL)));


        }
        cursor.close();

    }
    public void deletePrompt()
    {        dialog.setContentView(R.layout.delete_prompt);
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
        String selection = InventoryContract.InventoryEntry._id + "=?";
        String[] selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
        int rowId = getContentResolver().delete(InventoryContract.InventoryEntry.Content_Uri, selection, selectionArgs);
        finish();
        Toast.makeText(getApplicationContext(),"Product deleted",Toast.LENGTH_SHORT).show();
    }

    public static final class helperclass {
        public static long id;
        public static Uri uri;
    }

}
