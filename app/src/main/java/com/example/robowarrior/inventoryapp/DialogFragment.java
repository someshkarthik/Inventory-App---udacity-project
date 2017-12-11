package com.example.robowarrior.inventoryapp;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.example.robowarrior.inventoryapp.data.InventoryContract;

/**
 * Created by Robo warrior on 06-12-2017.
 */


public class DialogFragment extends android.support.v4.app.DialogFragment {
  // Uri uri1;
//ProductDetailActivity productDetailActivity;
public DialogFragment()
{

}
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//          String uri = getArguments().getString("URI");
//          uri1 = Uri.parse(uri);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Delete the Product")
                .setPositiveButton("proceed", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //productDetailActivity = new ProductDetailActivity(ProductDetailActivity.helperclass.id);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

    }
}
