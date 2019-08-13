package com.example.inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.listView);

        ArrayList<InventoryObject> inventory = new ArrayList<InventoryObject>();
        InventoryObject item = new InventoryObject("Single Pole Switch", 500);
        inventory.add(item);
        SaveArrayListToSD(this, "inventoryList", inventory);



        ArrayList<InventoryObject> readInventory = (ArrayList) ReadArrayListFromSD(this, "inventoryList");



        InventoryAdapter inventoryAdapter = new InventoryAdapter(readInventory, this);

        listView.setAdapter(inventoryAdapter);
    }

    public void OpenAddItemDialog(View view){
        Intent intent = new Intent(this, OpenAddItemDialogActivity.class);
        startActivityForResult(intent,1);
    }

    public void AddItem(View view) {
        EditText text = findViewById(R.id.material);
        String material = text.getText().toString();
        EditText countText = findViewById(R.id.count);
        Number count = Integer.parseInt(countText.getText().toString());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String name = data.getStringExtra("name");
                String countText = data.getStringExtra("count");
                System.out.println("ERROR CATCH");
                System.out.println(name);
                System.out.println(countText);
                Integer count = Integer.parseInt(countText);
                ArrayList<InventoryObject> inventory;
                inventory = (ArrayList) ReadArrayListFromSD(this, "inventoryList");
                System.out.print(Arrays.toString(inventory.toArray()));
                InventoryObject item = new InventoryObject(name, count);
                inventory.add(item);
                SaveArrayListToSD(this, "inventoryList", inventory);
                InventoryAdapter inventoryAdapter = new InventoryAdapter(inventory, this);

                ListView listView = (ListView) findViewById(R.id.listView);
                listView.setAdapter(inventoryAdapter);
            } else {
                System.out.println("FAILED TO GET PARAMETER");
            }
        }
    }

    public static <E> void SaveArrayListToSD(Context mContext, String filename, ArrayList<E> list){
        try {

            FileOutputStream fos = mContext.openFileOutput(filename + ".dat", mContext.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(list);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Object ReadArrayListFromSD(Context mContext,String filename){
        try {
            FileInputStream fis = mContext.openFileInput(filename + ".dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object obj= (Object) ois.readObject();
            fis.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<Object>();
        }
    }
}
