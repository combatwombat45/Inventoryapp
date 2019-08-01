package com.example.inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
