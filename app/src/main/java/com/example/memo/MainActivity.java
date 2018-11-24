package com.example.memo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.R.id.list;

public class MainActivity extends AppCompatActivity {
    Button bn;
    //String[] date=new String[20];
    ArrayList<String> date=new ArrayList<String>();
    //String[] date={"aaaaa","bbbb"};
    int count=0;
    MyDatabaseHelper dbmemo;
    EditText e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        e=(EditText)findViewById(R.id.textt);
        dbmemo=new MyDatabaseHelper(this,"memo.db",null,1);

        SQLiteDatabase db=dbmemo.getWritableDatabase();
        Cursor cursor=db.query("memo",null,null,null,null,null,null);

        if(cursor.moveToFirst()){
            do{
                String author=cursor.getString(cursor.getColumnIndex("author"));
                date.add(count++,author);
            }while (cursor.moveToNext());
            cursor.close();
            //oast.makeText(MainActivity.this,"success",Toast.LENGTH_SHORT).show();
        }

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(MainActivity.this,R.layout.support_simple_spinner_dropdown_item,date);
        ListView listView=(ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s=date.get(position);
                Toast.makeText(MainActivity.this,s,Toast.LENGTH_SHORT).show();
            }
                  });
        bn=(Button)findViewById(R.id.addd);

        bn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String a=e.getText().toString();
                SQLiteDatabase db=dbmemo.getWritableDatabase();
                ContentValues values=new ContentValues();
                values.put("author",a);
                db.insert("memo",null,values);
                Toast.makeText(MainActivity.this,"add success",Toast.LENGTH_SHORT).show();
                Cursor cursor=db.query("memo",null,null,null,null,null,null);
                date.add(count++,a);
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(MainActivity.this,R.layout.support_simple_spinner_dropdown_item,date);
                ListView listView=(ListView)findViewById(R.id.list_view);
                listView.setAdapter(adapter);
            }
        });
        bn=(Button)findViewById(R.id.create);
        bn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                dbmemo.getWritableDatabase();
            }
        });
    }
    public class MyDatabaseHelper extends SQLiteOpenHelper {
        public static final String CREATE_MEMO = "create table memo ("
                + "author text"+")";

        private Context mContext;
        public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {

            super(context, name, factory, version);

            mContext = context;

        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_MEMO);
            Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
