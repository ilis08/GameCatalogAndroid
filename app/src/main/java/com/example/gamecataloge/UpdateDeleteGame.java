package com.example.gamecataloge;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateDeleteGame extends DBHelper {
    protected EditText editTitle, editDesc, editPrice;
    protected Button btnUpdate, btnDelete;
    protected String Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete_game);

        editTitle=findViewById(R.id.editTitle);
        editDesc=findViewById(R.id.editDescription);
        editPrice=findViewById(R.id.editPrice);
        btnUpdate=findViewById(R.id.btnUpdate);
        btnDelete=findViewById(R.id.btnDelete);

        Bundle b = getIntent().getExtras();

        ActionBar ab = getSupportActionBar();
        if (ab!=null){
            ab.setTitle(b.getString("Title"));
        }


        if (b != null){
            Id = b.getString("Id");
            editTitle.setText(b.getString("Title"));
            editDesc.setText(b.getString("Description"));
            editPrice.setText(b.getString("Price"));
        }

        btnDelete.setOnClickListener((view -> {
            try {
                ExecSQL("DELETE FROM GAMES WHERE " +
                        "Id = ?",
                        new Object[]{Id},
                        () -> Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_LONG).show());
            }catch (Exception ex){
                Toast.makeText(getApplicationContext(), "Delete Error"+ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }finally {
                BackToGames();
            }
        }));

        btnUpdate.setOnClickListener((view -> {
            try {
                ExecSQL("UPDATE GAMES SET " +
                                "Title = ?, " +
                                "Description = ?, "+
                                "Price = ? "+
                                "WHERE ID = ?",
                        new Object[]{
                                editTitle.getText().toString(),
                                editDesc.getText().toString(),
                                editPrice.getText().toString(),
                                Id
                        },
                        () -> Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_LONG).show());
            }catch (Exception ex){
                Toast.makeText(getApplicationContext(), "Update Error"+ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }finally {
                BackToGames();
            }
        }));
    }

    private void BackToGames(){
        finishActivity(200);

        Intent intent = new Intent(UpdateDeleteGame.this, Games.class);

        startActivity(intent);
    }
}