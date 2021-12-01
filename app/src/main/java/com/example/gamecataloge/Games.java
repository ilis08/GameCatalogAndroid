package com.example.gamecataloge;

import androidx.annotation.CallSuper;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Games extends DBHelper {

    protected ListView simpleList;

    protected void FillListView() throws Exception{
        final ArrayList<String> listResults=
                new ArrayList<>();

        SelectSql("SELECT * FROM Games ORDER BY Title",
                null,
                (Id, Title, Description, Price) -> {
                    listResults.add(Id+"\t"+Title+"\t"+Description+"\t"+Price+"\n");
                }
        );

        simpleList.clearChoices();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.activity_games_list,
                R.id.textView,
                listResults
        );

        simpleList.setAdapter(arrayAdapter);
    }

    @Override
    @CallSuper
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        try {
            FillListView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games2);

        simpleList = findViewById(R.id.gamesList);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Games");

        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
                TextView clickedText=view.findViewById(R.id.textView);
                String selected = clickedText.getText().toString();
                String [] elements = selected.split("\t");
                String Id = elements[0];
                String Title = elements[1];
                String Description = elements[2];
                String Price = elements[3];

                Intent intent = new Intent(Games.this, UpdateDeleteGame.class);

                Bundle b = new Bundle();
                b.putString("Id", Id);
                b.putString("Title", Title);
                b.putString("Description", Description);
                b.putString("Price", Price);

                intent.putExtras(b);

                startActivityForResult(intent, 200, b);
            }
        });

        try {
            InitDB();
            FillListView();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void createGame(View view){
        Intent intent = new Intent(this, CreateGame.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.deleteAll){
            try {
                ExecSQL("DELETE FROM GAMES", null, () ->Toast.makeText(getApplicationContext(), "All deleted", Toast.LENGTH_SHORT).show());
            }catch (Exception ex){
                Toast.makeText(getApplicationContext(),
                        "Delete action failed: "+ ex.getLocalizedMessage(),
                        Toast.LENGTH_SHORT).show();
            }finally {
                finishActivity(200);
                Intent intent = new Intent(this, Games.class);
                startActivity(intent);
            }
        }
        else if (item.getItemId() == android.R.id.home){
            this.finish();
            Intent intent = new Intent(Games.this, MainActivity.class);
            startActivity(intent);
        }

        return  super.onOptionsItemSelected(item);
    }
}