package com.example.inventory;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class OpenAddItemDialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_add_item_dialog);
        final Context context = getApplicationContext();

        Spinner unitDropdown = findViewById(R.id.unit_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.unit_types, android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitDropdown.setAdapter(adapter);
        unitDropdown.setRotationY(180);
        unitDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapter, View view, int pos, long id) {
                view.setRotationY(180);
            }
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        Button addButton = (Button) findViewById(R.id.add);
        addButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent();
                EditText text = findViewById(R.id.material);
                String material = text.getText().toString();
                EditText countText = findViewById(R.id.count);
                String count = countText.getText().toString();
                Spinner unitDropdown = findViewById(R.id.unit_spinner);
                String unit = unitDropdown.getSelectedItem().toString();
                EditText minimumCountText = findViewById(R.id.minimum_count);
                String minimumCount = minimumCountText.getText().toString();
                Bundle extras = new Bundle();
                extras.putString("name",material);
                extras.putString("count",count);
                extras.putString("unit",unit);
                extras.putString("minimumCount", minimumCount);

                if (count.isEmpty() || material.isEmpty()){
                    Tools.exceptionToast(context,"Material or Count Cannot be Blank");

                } else {
                    intent.putExtras(extras);
                    setResult(RESULT_OK, intent);
                    finish();
                }

            }
        });
    }

    public void CancelAddItem(View view) {
        finish();
    }
}