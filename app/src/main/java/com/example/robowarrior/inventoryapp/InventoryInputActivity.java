package com.example.robowarrior.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.robowarrior.inventoryapp.data.InventoryContract;
import com.example.robowarrior.inventoryapp.data.InventoryDbHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InventoryInputActivity extends AppCompatActivity {
    int QuantityValue=0;
    private InventoryDbHelper inventoryDbHelper;

    private EditText mNameEditText;

    private EditText mPriceEditText;

    private EditText mSupplierNameEditText;

    private EditText mSupplierPhoneEditText;

    private EditText mSupplierEmailEditText;

    private TextView mQuantityTextView;
    public String toastmsg;
    public String deletemsg;
    private Uri uri;
    private Uri uri1;
    private Cursor cursor;
    public InventoryInputActivity()
    {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_input);
        mNameEditText = (EditText) findViewById(R.id.edit_product);
        mPriceEditText = (EditText) findViewById(R.id.edit_product_price);
        mSupplierNameEditText = (EditText) findViewById(R.id.supplier_name);
        mSupplierPhoneEditText = (EditText) findViewById(R.id.supplier_phone_number);
        mSupplierEmailEditText = (EditText) findViewById(R.id.supplier_email_address);
         mQuantityTextView = (TextView) findViewById(R.id.product_quantity);
         inventoryDbHelper = new InventoryDbHelper(this);
         String[] projection={
                 InventoryContract.InventoryEntry._ID,
                 InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME,
                 InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE,
        InventoryContract.InventoryEntry.COLUMN_SUPPLIER_EMAIL,
        InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE,
                 InventoryContract.InventoryEntry.COLUMN_QUANTITY,
        InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME,
         };
        Intent intent=getIntent();
        uri1 = intent.getData();
        if(uri1==null) {
            setTitle("Add a Product");
        }
        else
        {   setTitle("Edit Product info");
            String selection = InventoryContract.InventoryEntry._id+"=?";
            String[] selectionArgs = new String[]{String.valueOf(ProductDetailActivity.helperclass.id)};
            cursor=getContentResolver().query(InventoryContract.InventoryEntry.Content_Uri,projection,selection,selectionArgs,null);
            if(cursor.moveToFirst())
            {
            mNameEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME)));
                mPriceEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE)));
                mQuantityTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.COLUMN_QUANTITY)));
                mSupplierNameEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME)));
                mSupplierPhoneEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE)));
                mSupplierEmailEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_EMAIL)));
                QuantityValue = cursor.getInt(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.COLUMN_QUANTITY));
            }
            cursor.close();
        }
        }

    public void add(View view)
    {
      display(++QuantityValue);
    }
    public void sub(View view)
    {  if(QuantityValue>0)
      display(--QuantityValue);
    }
    private void display(int a)
    {

        mQuantityTextView.setText(String.valueOf(a));
    }
    private Boolean insert()
    {   boolean eflag=true;
        boolean pflag = true;
        boolean flag =true;
        String PRODUCTNAME = mNameEditText.getText().toString().trim();
        if(PRODUCTNAME.equals(""))
            flag =false;
        int PRODUCTPRICE=0;
        if(mPriceEditText.getText().toString().trim().equals(""))
            flag=false;
        else
            PRODUCTPRICE = Integer.parseInt(mPriceEditText.getText().toString().trim());
        String SUPPLIERNAME = mSupplierNameEditText.getText().toString().trim() ;
        if(SUPPLIERNAME.equals(""))
            flag = false;
        int PRODUCTQUANTITY = Integer.valueOf(mQuantityTextView.getText().toString().trim());
        if(PRODUCTQUANTITY==0)
            flag=false;
        String SupplierPhone=String.valueOf(123456789) ;
        if(mSupplierPhoneEditText.getText().toString().trim().equals(""))
            flag=false;
        else {
            SupplierPhone = mSupplierPhoneEditText.getText().toString().trim();
            pflag = isValidMobile(SupplierPhone);
        }
        String SupplierEmail = mSupplierEmailEditText.getText().toString().trim();
        if(SupplierEmail.equals(""))
            flag=false;
        else
            eflag = emailValidator(SupplierEmail);
        ContentValues contentValues = new ContentValues();
        contentValues.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME,PRODUCTNAME);
        contentValues.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE,PRODUCTPRICE);
        contentValues.put(InventoryContract.InventoryEntry.COLUMN_QUANTITY,PRODUCTQUANTITY);
        contentValues.put(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME,SUPPLIERNAME);
        contentValues.put(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE,SupplierPhone);
        contentValues.put(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_EMAIL,SupplierEmail);
        long rowId=-1;
        int updatedRowId=-1;
       if(uri1==null) {
            if(flag && eflag && pflag){
                uri = getContentResolver().insert(InventoryContract.InventoryEntry.Content_Uri, contentValues);
                rowId = Integer.parseInt(String.valueOf(ContentUris.parseId(uri)));}
            if(rowId>=0)
            { Toast.makeText(this.getApplicationContext(),"Product info saved",Toast.LENGTH_SHORT).show();
                return true;}
            else
                if(!eflag)
                    Toast.makeText(getApplicationContext(),"Enter a Valid Email-Address",Toast.LENGTH_SHORT).show();
                 else if(!pflag)
                    Toast.makeText(getApplicationContext(),"Enter a Valid Phone Number",Toast.LENGTH_SHORT).show();
                else
                {Toast.makeText(getApplicationContext(),"Please fill the rest of the Form",Toast.LENGTH_SHORT).show();
                return false;}
        }
        else
       {
            if(flag && eflag &&pflag)
        {
            String selection = InventoryContract.InventoryEntry._id + "=?";
            String[] selectionArgs = new String[] { String.valueOf(ProductDetailActivity.helperclass.id)};
            updatedRowId = getContentResolver().update(InventoryContract.InventoryEntry.Content_Uri,contentValues,selection,selectionArgs);}
            if(updatedRowId>=0)
            {
                Toast.makeText(this.getApplicationContext(),"Product info Updated",Toast.LENGTH_SHORT).show();
                return true;
            }
            if(!eflag && !pflag)
                Toast.makeText(getApplicationContext(),"Enter a Valid Phone and Email-Address",Toast.LENGTH_SHORT).show();
            else if(!eflag)
               Toast.makeText(getApplicationContext(),"Enter a Valid Email-Address",Toast.LENGTH_SHORT).show();
           else if(!pflag)
               Toast.makeText(getApplicationContext(),"Enter a Valid Phone Number",Toast.LENGTH_SHORT).show();
           else
           { Toast.makeText(getApplicationContext(),"Please fill the rest of the Form",Toast.LENGTH_SHORT).show();
           return false;}


        }
        return false;
    }
    private boolean isValidMobile(String phone) {
        Pattern pattern;
        Matcher matcher;
        final String PHONE_PATTERN = "([+91]+)*[0-9]{10}$";
        pattern = Pattern.compile(PHONE_PATTERN);
        matcher = pattern.matcher(phone);
        return matcher.matches();
    }
    public boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]{2,3}+)*(\\.[A-Za-z]{2,3})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_inventory_input, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                Boolean bool = insert();
               if(bool)
                   finish();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
