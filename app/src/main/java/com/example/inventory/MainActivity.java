package com.example.inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.listView);

        ArrayList<InventoryObject> inventory = new ArrayList<InventoryObject>();
        InventoryObject item1 = new InventoryObject("Single Pole Switch1", 5);
        InventoryObject item2 = new InventoryObject("Single Pole Switch2", 5);
        InventoryObject item3 = new InventoryObject("Single Pole Switch3", 5);
        InventoryObject item4 = new InventoryObject("Single Pole Switch4", 5);
        inventory.add(item1);
        inventory.add(item2);
        inventory.add(item3);
        inventory.add(item4);

        InventoryAdapter inventoryAdapter = new InventoryAdapter(inventory, this);

        listView.setAdapter(inventoryAdapter);
    }

    public void OpenAddItemDialog(View view){
        Intent intent = new Intent(this, OpenAddItemDialogActivity.class);
        startActivity(intent);

    }
    public void AddItem(View view) {
        EditText text = findViewById(R.id.material);
        String material = text.getText().toString();
        EditText countText = findViewById(R.id.count);
        Number count = Integer.parseInt(countText.getText().toString());
    }
}
