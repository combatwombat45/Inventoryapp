package com.example.inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    static String ACTION_DELETE = "delete";
    static String ACTION_UPDATE = "update";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.listView);

        ArrayList<InventoryObject> inventory = (ArrayList) ReadArrayListFromSD(this, "inventoryList");
        InventoryAdapter inventoryAdapter = new InventoryAdapter(inventory, this);
        listView.setAdapter(inventoryAdapter);


        final Context that = this;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                TextView listItemName = (TextView)view.findViewById(R.id.list_item_name);
                TextView listItemCount = (TextView)view.findViewById(R.id.list_item_count);
                TextView listItemUnit = (TextView)view.findViewById(R.id.list_item_unit);
                String name = listItemName.getText().toString();
                String unit = listItemUnit.getText().toString();
                String count = listItemCount.getText().toString();
                Intent intent = new Intent(that, EditItemActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("index", Integer.toString(index));
                intent.putExtra("unit", unit);
                intent.putExtra("count", count);


                startActivityForResult(intent,2);

            }
        });
    }

    public void OpenAddItemDialog(View view){
        Intent intent = new Intent(this, OpenAddItemDialogActivity.class);
        startActivityForResult(intent,1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        1 = additem, 2 = deleteitem
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String name = data.getStringExtra("name");
                String countText = data.getStringExtra("count");
                Integer count = Integer.parseInt(countText);
                String unit = data.getStringExtra("unit");
                ArrayList<InventoryObject> inventory = (ArrayList) ReadArrayListFromSD(this, "inventoryList");
                InventoryObject item = new InventoryObject(name, count, unit);
                inventory.add(item);
                SaveArrayListToSD(this, "inventoryList", inventory);
                InventoryAdapter inventoryAdapter = new InventoryAdapter(inventory, this);

                ListView listView = (ListView) findViewById(R.id.listView);
                listView.setAdapter(inventoryAdapter);
            } else {
            }
        } else if (requestCode == 2){


            if (resultCode == RESULT_OK) {
                String action = data.getStringExtra("action");
                if (action.equals(ACTION_DELETE)) {
                    String indexText = data.getStringExtra("index");
                    Integer index = Integer.parseInt(indexText);
                    ArrayList<InventoryObject> inventory = (ArrayList) ReadArrayListFromSD(this, "inventoryList");
                    inventory.remove(index.intValue());
                    SaveArrayListToSD(this, "inventoryList", inventory);
                    InventoryAdapter inventoryAdapter = new InventoryAdapter(inventory, this);

                    ListView listView = (ListView) findViewById(R.id.listView);
                    listView.setAdapter(inventoryAdapter);
                } else if (action.equals(ACTION_UPDATE)) {
                    String indexText = data.getStringExtra("index");
                    String countText = data.getStringExtra("count");
                    Integer count = Integer.parseInt(countText);
                    String unit = data.getStringExtra("unit");
                    String material = data.getStringExtra("name");
                    Integer index = Integer.parseInt(indexText);
                    ArrayList<InventoryObject> inventory = (ArrayList) ReadArrayListFromSD(this, "inventoryList");
                    InventoryObject inventoryObject = inventory.get(index.intValue());
                    inventoryObject.setName(material);
                    inventoryObject.setCount(count);
                    inventoryObject.setUnit(unit);
                    inventory.set(index,inventoryObject);

                    SaveArrayListToSD(this, "inventoryList", inventory);
                    InventoryAdapter inventoryAdapter = new InventoryAdapter(inventory, this);

                    ListView listView = (ListView) findViewById(R.id.listView);
                    listView.setAdapter(inventoryAdapter);
                } else {
                }
            }
        }
    }

    public void subtractFromCount(View view) {
        ImageView subtract = (ImageView) view;
        com.google.android.flexbox.FlexboxLayout row = (com.google.android.flexbox.FlexboxLayout) subtract.getParent();
        TextView nameView = (TextView) row.getChildAt(0);
        ArrayList<InventoryObject> inventory = (ArrayList) ReadArrayListFromSD(this, "inventoryList");

        int inventoryIndex = 0;
        for (int i=0;i<inventory.size();i++) {
            InventoryObject inventoryObject = (InventoryObject) inventory.get(i);
            if (inventoryObject.getName().equals(nameView.getText().toString())) {
                inventoryObject.decrementCount();
                inventory.set(i, inventoryObject);
                SaveArrayListToSD(this, "inventoryList", inventory);
                InventoryAdapter inventoryAdapter = new InventoryAdapter(inventory, this);
                ListView listView = (ListView) findViewById(R.id.listView);
                listView.setAdapter(inventoryAdapter);
            }
        }
    }

    public void addFromCount(View view) {
        ImageView subtract = (ImageView) view;
        com.google.android.flexbox.FlexboxLayout row = (com.google.android.flexbox.FlexboxLayout) subtract.getParent();
        TextView nameView = (TextView) row.getChildAt(0);
        ArrayList<InventoryObject> inventory = (ArrayList) ReadArrayListFromSD(this, "inventoryList");

        int inventoryIndex = 0;
        for (int i=0;i<inventory.size();i++) {
            InventoryObject inventoryObject = (InventoryObject) inventory.get(i);
            if (inventoryObject.getName().equals(nameView.getText().toString())) {
                inventoryObject.incrementCount();
                inventory.set(i, inventoryObject);
                SaveArrayListToSD(this, "inventoryList", inventory);
                InventoryAdapter inventoryAdapter = new InventoryAdapter(inventory, this);
                ListView listView = (ListView) findViewById(R.id.listView);
                listView.setAdapter(inventoryAdapter);
            }
        }
    }

    public static <E> void SaveArrayListToSD(Context mContext, String filename, ArrayList<E> list){
        try {

            FileOutputStream fos = mContext.openFileOutput(filename + ".dat", mContext.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(list);
            oos.close();
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
            ois.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<Object>();
        }
    }
}
