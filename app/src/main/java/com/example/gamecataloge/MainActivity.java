package com.example.gamecataloge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void getGames(View view){
        Intent intent = new Intent(this, Games.class);
        startActivity(intent);
    }

    public void getQuotes(View view){
        Intent intent = new Intent(this, RandomQuotes.class);
        startActivity(intent);
    }
}