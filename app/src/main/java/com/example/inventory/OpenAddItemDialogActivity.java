package com.example.inventory;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OpenAddItemDialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_add_item_dialog);
        final Context context = getApplicationContext();
        Button addButton = (Button) findViewById(R.id.add);
        addButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent();
                EditText text = findViewById(R.id.material);
                String material = text.getText().toString();
                EditText countText = findViewById(R.id.count);
                String count = countText.getText().toString();
                Bundle extras = new Bundle();
                extras.putString("name",material);
                extras.putString("count",count);

                if (count.isEmpty() || material.isEmpty()){
//                    AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
//                    builder1.setMessage("Fields Material or Count Cannot be Empty");
//                    builder1.setCancelable(true);
//                    AlertDialog alert11 = builder1.create();
//                    alert11.show();
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