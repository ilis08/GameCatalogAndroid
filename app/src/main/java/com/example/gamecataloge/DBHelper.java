package com.example.gamecataloge;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DBHelper extends AppCompatActivity {
    protected interface OnQuerySuccess{
        public void OnSuccess();
    }

    protected  interface OnSelectSuccess{
        public void OnElementSelected(String Id, String Name, String Description, String Price);
    }

    //protected String DBFILE = getFilesDir().getPath()+"/GameStore.db";

    @Override
    protected void onCreate(Bundle savedInstData){
        super.onCreate(savedInstData);
    }

    protected void SelectSql(String SelectQ, String[] args, OnSelectSuccess success)
            throws Exception
    {
        SQLiteDatabase db = SQLiteDatabase
                .openOrCreateDatabase(getFilesDir().getPath()+"/GameCatalog.db", null);

        Cursor cursor = db.rawQuery(SelectQ, args);

        if(cursor.getCount() == 0){
            Toast.makeText(getApplicationContext(),
                    "No data", Toast.LENGTH_SHORT).show();
        }else   {
            while (cursor.moveToNext()){
                String Id = cursor.getString(cursor.getColumnIndexOrThrow("ID"));
                String Title = cursor.getString(cursor.getColumnIndexOrThrow("Title"));
                String Description = cursor.getString(cursor.getColumnIndexOrThrow("Description"));
                String Price = cursor.getString(cursor.getColumnIndexOrThrow("Price"));
                success.OnElementSelected(Id, Title, Description, Price);
            }
        }

        db.close();
    }

    protected void ExecSQL(String SQL, Object[] args, OnQuerySuccess success)
            throws Exception
    {
        SQLiteDatabase db = SQLiteDatabase
                .openOrCreateDatabase(getFilesDir().getPath()+"/GameCatalog.db", null);
        if (args!=null)
            db.execSQL(SQL, args);
        else
            db.execSQL(SQL);

        db.close();
        success.OnSuccess();
    }

    protected void InitDB() throws Exception{
        ExecSQL("CREATE TABLE if not exists Games ( "+
                        "ID integer PRIMARY KEY AUTOINCREMENT, " +
                        "Title text not null, " +
                        "Description text not null, " +
                        "Price int not null " +
                        ")", null,
                () -> Toast.makeText(getApplicationContext(),
                        "DB Init Successful", Toast.LENGTH_LONG).show()
        );
    }
}
