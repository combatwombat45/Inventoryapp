package com.example.inventory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;


import com.google.android.material.tabs.TabLayout;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static String ACTION_DELETE = "delete";
    static String ACTION_UPDATE = "update";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        handleIntent(getIntent());

//        Code to manually reset current inventory
//        ArrayList<InventoryObject> inventory = new ArrayList<InventoryObject>();
//        SaveArrayListToSD(this, "currentInventoryList", inventory);
//        SaveArrayListToSD(this, "inventoryList", inventory);

        ListView listView = (ListView) findViewById(R.id.listView);
        ArrayList<InventoryObject> inventory = (ArrayList) ReadArrayListFromSD(this, "inventoryList");
        ArrayList<InventoryObject> currentInventory = (ArrayList) ReadArrayListFromSD(this, "currentInventoryList");
        InventoryAdapter inventoryAdapter = new InventoryAdapter(currentInventory, this);
        listView.setAdapter(inventoryAdapter);

        TextView titleView = (TextView) findViewById(R.id.inventoryTitle);
        ImageView backView = (ImageView) findViewById(R.id.back);
        if (inventory.size() != currentInventory.size()){
            titleView.setVisibility(View.GONE);
            backView.setVisibility(View.VISIBLE);
        } else {
            titleView.setVisibility(View.VISIBLE);
            backView.setVisibility(View.GONE);
        }

        CheckForEmptyInventory(this);
    }


    public void CheckForEmptyInventory(Context mContext) {
        ListView listView = (ListView) findViewById(R.id.listView);
        ArrayList<InventoryObject> inventory = (ArrayList) ReadArrayListFromSD(this, "inventoryList");
        TextView emptyInventoryView = (TextView) findViewById(R.id.emptyInventory);
        if (inventory.size() == 0) {
            listView.setVisibility(View.GONE);
            emptyInventoryView.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            emptyInventoryView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        System.out.println("NEW SEARCH INTENT");
        handleIntent(intent);
    }

    // handleIntent, when this function runs, it causes a refresh of the activity, causing OnCreate to run again.
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            // Filter out all inventory objects that don't contain the query. Then Display them. DO NOT SAVE TO SD!
            ArrayList<InventoryObject> inventory = (ArrayList) ReadArrayListFromSD(this, "inventoryList");
            ArrayList<InventoryObject> searchedInventory = new ArrayList<InventoryObject>();
            for (int i = 0; i < inventory.size(); i++) {
                InventoryObject inventoryObject = (InventoryObject) inventory.get(i);
                if (inventoryObject.getName().toLowerCase().contains(query.toLowerCase())) {
                    searchedInventory.add(inventoryObject);
                }
            }
            SaveArrayListToSD(this, "currentInventoryList", searchedInventory);
        }
//        finish();
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
                String minimumCountText = data.getStringExtra("minimumCount");
                Integer minimumCount = 0;
                System.out.println("*****");
                System.out.println(minimumCountText);
                if (!minimumCountText.isEmpty()) {
                    minimumCount = Integer.parseInt(minimumCountText);
                }
                ArrayList<InventoryObject> inventory = (ArrayList) ReadArrayListFromSD(this, "inventoryList");
                ArrayList<InventoryObject> currentInventory = (ArrayList) ReadArrayListFromSD(this, "currentInventoryList");
                InventoryObject item = new InventoryObject(name, count, unit, minimumCount);
                inventory.add(item);
                currentInventory.add(item);
                SaveArrayListToSD(this, "inventoryList", inventory);
                SaveArrayListToSD(this, "currentInventoryList", currentInventory);
                InventoryAdapter inventoryAdapter = new InventoryAdapter(currentInventory, this);
                ListView listView = (ListView) findViewById(R.id.listView);
                listView.setAdapter(inventoryAdapter);
                TextView emptyInventoryView = (TextView) findViewById(R.id.emptyInventory);
                listView.setVisibility(View.VISIBLE);
                emptyInventoryView.setVisibility(View.GONE);
            } else { }
        } else if (requestCode == 2){
            if (resultCode == RESULT_OK) {
                String action = data.getStringExtra("action");
                if (action.equals(ACTION_DELETE)) {
                    String id = data.getStringExtra("id");
                    ArrayList<InventoryObject> inventory = (ArrayList) ReadArrayListFromSD(this, "inventoryList");
                    ArrayList<InventoryObject> currentInventory = (ArrayList) ReadArrayListFromSD(this, "currentInventoryList");

                    for (int i = 0; i < inventory.size(); i++) {
                        InventoryObject inventoryObject = (InventoryObject) inventory.get(i);
                        if (inventoryObject.getID().equals(id)) {
                            inventory.remove(inventoryObject);
                        }
                    }

                    for (int i = 0; i < currentInventory.size(); i++) {
                        InventoryObject inventoryObject = (InventoryObject) currentInventory.get(i);
                        if (inventoryObject.getID().equals(id)) {
                            currentInventory.remove(inventoryObject);
                        }
                    }

                    SaveArrayListToSD(this, "inventoryList", inventory);
                    SaveArrayListToSD(this, "currentInventoryList", currentInventory);
                    InventoryAdapter inventoryAdapter = new InventoryAdapter(currentInventory, this);
                    ListView listView = (ListView) findViewById(R.id.listView);
                    listView.setAdapter(inventoryAdapter);
                    CheckForEmptyInventory(this);
                } else if (action.equals(ACTION_UPDATE)) {
                    String countText = data.getStringExtra("count");
                    Integer count = Integer.parseInt(countText);
                    String unit = data.getStringExtra("unit");
                    String material = data.getStringExtra("name");
                    String id = data.getStringExtra("id");
                    Integer minimumCount = Integer.parseInt(data.getStringExtra("minimumCount"));
                    ArrayList<InventoryObject> inventory = (ArrayList) ReadArrayListFromSD(this, "inventoryList");
                    ArrayList<InventoryObject> currentInventory = (ArrayList) ReadArrayListFromSD(this, "currentInventoryList");


                    for (int i = 0; i < inventory.size(); i++) {
                        InventoryObject inventoryObject = (InventoryObject) inventory.get(i);
                        if (inventoryObject.getID().equals(id)) {
                            inventoryObject.setName(material);
                            inventoryObject.setCount(count);
                            inventoryObject.setMinimumCount(minimumCount);
                            inventoryObject.setUnit(unit);
                            inventory.set(i,inventoryObject);
                        }
                    }

                    for (int i = 0; i < currentInventory.size(); i++) {
                        InventoryObject inventoryObject = (InventoryObject) currentInventory.get(i);
                        if (inventoryObject.getID().equals(id)) {
                            inventoryObject.setName(material);
                            inventoryObject.setCount(count);
                            inventoryObject.setMinimumCount(minimumCount);
                            inventoryObject.setUnit(unit);
                            currentInventory.set(i,inventoryObject);
                        }
                    }

                    SaveArrayListToSD(this, "inventoryList", inventory);
                    SaveArrayListToSD(this, "currentInventoryList", currentInventory);
                    InventoryAdapter inventoryAdapter = new InventoryAdapter(currentInventory, this);

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
        TextView idView = (TextView) row.getChildAt(0);
        ArrayList<InventoryObject> inventory = (ArrayList) ReadArrayListFromSD(this, "inventoryList");
        ArrayList<InventoryObject> currentInventory = (ArrayList) ReadArrayListFromSD(this, "currentInventoryList");
        int currentInventoryIndex = 0;

        for (int i=0;i<inventory.size();i++) {
            InventoryObject inventoryObject = (InventoryObject) inventory.get(i);
            if (inventoryObject.getID().equals(idView.getText().toString())) {
                if (inventoryObject.getCount() > 0) {
                    inventoryObject.decrementCount();
                }
                inventory.set(i, inventoryObject);
            }
        }

        for (int i=0;i<currentInventory.size();i++) {
            InventoryObject inventoryObject = (InventoryObject) currentInventory.get(i);
            if (inventoryObject.getID().equals(idView.getText().toString())) {
                currentInventoryIndex = i;
                if (inventoryObject.getCount() > 0) {
                    inventoryObject.decrementCount();
                }
                currentInventory.set(i, inventoryObject);
            }
        }

        SaveArrayListToSD(this, "inventoryList", inventory);
        SaveArrayListToSD(this, "currentInventoryList", currentInventory);
        InventoryAdapter inventoryAdapter = new InventoryAdapter(currentInventory, this);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(inventoryAdapter);
        listView.setSelection(currentInventoryIndex);
    }

    public void addFromCount(View view) {
        ImageView subtract = (ImageView) view;
        com.google.android.flexbox.FlexboxLayout row = (com.google.android.flexbox.FlexboxLayout) subtract.getParent();
        TextView idView = (TextView) row.getChildAt(0);
        ArrayList<InventoryObject> inventory = (ArrayList) ReadArrayListFromSD(this, "inventoryList");
        ArrayList<InventoryObject> currentInventory = (ArrayList) ReadArrayListFromSD(this, "currentInventoryList");
        int currentInventoryIndex = 0;

        for (int i=0;i<inventory.size();i++) {
            InventoryObject inventoryObject = (InventoryObject) inventory.get(i);
            if (inventoryObject.getID().equals(idView.getText().toString())) {
                inventoryObject.incrementCount();
                inventory.set(i, inventoryObject);
            }
        }

        for (int i=0;i<currentInventory.size();i++) {
            InventoryObject inventoryObject = (InventoryObject) currentInventory.get(i);
            if (inventoryObject.getID().equals(idView.getText().toString())) {
                currentInventoryIndex = i;
                inventoryObject.incrementCount();
                currentInventory.set(i, inventoryObject);
            }
        }

        SaveArrayListToSD(this, "inventoryList", inventory);
        SaveArrayListToSD(this, "currentInventoryList", currentInventory);
        InventoryAdapter inventoryAdapter = new InventoryAdapter(currentInventory, this);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(inventoryAdapter);
        listView.setSelection(currentInventoryIndex);
    }

    public void editItem(View view){
        TextView nameView = (TextView) view;
        com.google.android.flexbox.FlexboxLayout row = (com.google.android.flexbox.FlexboxLayout) nameView.getParent();
        TextView idView = (TextView) row.getChildAt(0);
        String id = idView.getText().toString();
        ArrayList<InventoryObject> inventory = (ArrayList) ReadArrayListFromSD(this, "inventoryList");
        InventoryObject inventoryObject = new InventoryObject("", 0, "", 0);

        for (int i=0;i<inventory.size();i++) {
            InventoryObject temp = (InventoryObject) inventory.get(i);
            if (temp.getID().equals(id)) {
                inventoryObject = temp;
            }
        }
        ArrayList<InventoryObject> currentInventory = (ArrayList) ReadArrayListFromSD(this, "currentInventoryList");
        Intent intent = new Intent(this, EditItemActivity.class);

        intent.putExtra("name", inventoryObject.getName());
        intent.putExtra("unit", inventoryObject.getUnit());
        intent.putExtra("count", Integer.toString(inventoryObject.getCount()));
        intent.putExtra("id", inventoryObject.getID());
        intent.putExtra("minimumCount", Integer.toString(inventoryObject.getMinimumCount()));

        startActivityForResult(intent,2);
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    // SaveArrayListToSD saves inventory to internal memory
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

    @Override
    public void onBackPressed() {
        clearSearch();
    }

    public void onBackPressed(View view) {
        clearSearch();
    }

    public void clearSearch() {
        Button lowInventoryButton = findViewById(R.id.lowButton);
        Button mainInventoryButton = findViewById(R.id.mainButton);
        lowInventoryButton.setBackgroundColor(getResources().getColor(R.color.background_not_chosen));
        lowInventoryButton.setTextColor(Color.BLACK);
        mainInventoryButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mainInventoryButton.setTextColor(Color.WHITE);

        ArrayList<InventoryObject> inventory = (ArrayList) ReadArrayListFromSD(this, "inventoryList");
        SaveArrayListToSD(this, "inventoryList", inventory);
        SaveArrayListToSD(this, "currentInventoryList", inventory);
        ArrayList<InventoryObject> currentInventory = (ArrayList) ReadArrayListFromSD(this, "currentInventoryList");
        InventoryAdapter inventoryAdapter = new InventoryAdapter(currentInventory, this);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(inventoryAdapter);

        TextView titleView = (TextView) findViewById(R.id.inventoryTitle);
        ImageView backView = (ImageView) findViewById(R.id.back);
        if (inventory.size() != currentInventory.size()){
            titleView.setVisibility(View.GONE);
            backView.setVisibility(View.VISIBLE);
        } else {
            titleView.setVisibility(View.VISIBLE);
            backView.setVisibility(View.GONE);
        }
    }

    public void homeView(View view) {
        clearSearch();
    }

    public void lowInventoryView(View view) {
        System.out.println("LowInventoryView");
        ArrayList<InventoryObject> lowInventory = new ArrayList<InventoryObject>();
        ArrayList<InventoryObject> inventory = (ArrayList) ReadArrayListFromSD(this, "inventoryList");

        Button lowInventoryButton = findViewById(R.id.lowButton);
        Button mainInventoryButton = findViewById(R.id.mainButton);
        lowInventoryButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        lowInventoryButton.setTextColor(Color.WHITE);
        mainInventoryButton.setBackgroundColor(getResources().getColor(R.color.background_not_chosen));
        mainInventoryButton.setTextColor(Color.BLACK);

        for (int i=0;i<inventory.size();i++) {
            InventoryObject inventoryObject = inventory.get(i);
            if (inventoryObject.getCount() < inventoryObject.getMinimumCount()) {
                System.out.println("add item");
                lowInventory.add(inventoryObject);
            }
        }

        SaveArrayListToSD(this, "currentInventoryList", lowInventory);
        InventoryAdapter inventoryAdapter = new InventoryAdapter(lowInventory, this);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(inventoryAdapter);
    }

}

