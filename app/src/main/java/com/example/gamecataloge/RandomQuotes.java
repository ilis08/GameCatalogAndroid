package com.example.gamecataloge;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RandomQuotes extends AppCompatActivity {
    private TextView quote, author;
    private Button getQuoteButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_quotes);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Quotes");

        quote = findViewById(R.id.quote);
        author = findViewById(R.id.author);
        getQuoteButton = findViewById(R.id.getQuoteButton);

        getQuoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://api.quotable.io/random?maxLength=50";

                new GetURLData().execute(url);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return  super.onOptionsItemSelected(item);
    }

    private class GetURLData extends AsyncTask<String, String, String>{
        @Override
        protected  void onPreExecute(){
            super.onPreExecute();
            quote.setText("Waiting...");
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(strings[0]);

                if (isOnline(getApplicationContext())){
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();

                    InputStream stream = connection.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();

                    String line = "";

                    while ((line = reader.readLine()) != null){
                        buffer.append(line).append("\n");

                        return  buffer.toString();
                    }
                }else  {
                    return "Please, try later.";
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (connection!= null)
                    connection.disconnect();
                try {
                    if (reader!=null)
                        reader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            JSONObject jsonObj = null;
            try {
                if (result.contains("Please, try later.")){

                    quote.setText(result);

                    author.setText("");
                }else{
                    jsonObj = new JSONObject(result);

                    quote.setText(" \"" + jsonObj.getString("content") + " \"");

                    author.setText(jsonObj.getString("author"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public boolean isOnline(Context context)
        {
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting())
            {
                return true;
            }
            return false;
        }
    }
}