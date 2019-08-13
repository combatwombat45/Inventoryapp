package com.example.inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class EditItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        String name = getIntent().getStringExtra("name");
        final String index = getIntent().getStringExtra("index");
        TextView nameView = (TextView) findViewById(R.id.material);
        nameView.setText(name);

        Button deleteButton = (Button) findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent();
                intent.putExtra("index",index);
                System.out.println("ERROR CATCHING");
                System.out.println(index);
                setResult(RESULT_OK,intent);
                finish();

            }
        });
    }

    public void CancelAddItem(View view) {
        finish();
    }
}
