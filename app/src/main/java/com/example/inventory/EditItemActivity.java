package com.example.inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

public class EditItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        final String name = getIntent().getStringExtra("name");
        String count = getIntent().getStringExtra("count");
        String unit = getIntent().getStringExtra("unit");
        final String id = getIntent().getStringExtra("id");
        String minimumCount = getIntent().getStringExtra("minimumCount");

        TextView nameView = (TextView) findViewById(R.id.material);
        TextView countView = (TextView) findViewById(R.id.count);
        Spinner unitSpinner = (Spinner) findViewById(R.id.unit_spinner);
        TextView minimumCountView = (TextView) findViewById(R.id.minimum_count);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.unit_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(adapter);

        countView.setText(count);
        minimumCountView.setText(minimumCount);
        nameView.setText(name);

        int spinnerIndex = 0;
        for (int i=0;i<unitSpinner.getCount();i++){
            if (unitSpinner.getItemAtPosition(i).toString().equalsIgnoreCase(unit)){
                spinnerIndex = i;
            }
        }
        unitSpinner.setSelection(spinnerIndex);

        Button deleteButton = (Button) findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent();
                Bundle extras = new Bundle();
                extras.putString("id",id);
                extras.putString("action","delete");
                intent.putExtras(extras);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        Button updateButton = (Button) findViewById(R.id.update);
        updateButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                EditText text = findViewById(R.id.material);
                String material = text.getText().toString();
                EditText countText = findViewById(R.id.count);
                String count = countText.getText().toString();
                Spinner unitDropdown = findViewById(R.id.unit_spinner);
                String unit = unitDropdown.getSelectedItem().toString();
                EditText minimumCountText = findViewById(R.id.minimum_count);
                String minimumCount = minimumCountText.getText().toString();

                Intent intent = new Intent();
                Bundle extras = new Bundle();
                extras.putString("action","update");
                extras.putString("name",material);
                extras.putString("count",count);
                extras.putString("minimumCount", minimumCount);
                extras.putString("unit",unit);
                extras.putString("id", id);
                intent.putExtras(extras);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    public void CancelEditItem(View view) {
        finish();
    }
}
