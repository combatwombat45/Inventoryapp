package com.example.inventory;

import android.content.Context;
import android.widget.Toast;

public class Tools {
    public static void exceptionToast(Context context, String message){
        Toast.makeText(context,"Material or Count Cannot be Blank",Toast.LENGTH_LONG).show();
    }
}