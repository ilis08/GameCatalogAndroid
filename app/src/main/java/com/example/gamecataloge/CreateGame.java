package com.example.gamecataloge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateGame extends DBHelper {
    protected EditText editTitle, editDesc, editPrice;
    protected Button btnInsert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        editTitle=findViewById(R.id.editTitle);
        editDesc=findViewById(R.id.editDescription);
        editPrice=findViewById(R.id.editPrice);
        btnInsert=findViewById(R.id.btnInsert);

        try {
            InitDB();
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnInsert.setOnClickListener(view -> {
            try {
                ExecSQL("INSERT INTO Games (Title, Description, Price) " +
                                "VALUES(?, ?, ?) ",
                        new Object[]{
                                editTitle.getText().toString(),
                                editDesc.getText().toString(),
                                editPrice.getText().toString()
                        },
                        () -> Toast.makeText(getApplicationContext(),
                                "Record Inserted", Toast.LENGTH_LONG).show()
                );

                Intent intent = new Intent(this, Games.class);
                startActivity(intent);
            }catch (Exception ex){
                Toast.makeText(getApplicationContext(),
                        "Insert Failed: "+ ex.getLocalizedMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getGames(View view){
        Intent intent = new Intent(this, Games.class);
        startActivity(intent);
    }
}