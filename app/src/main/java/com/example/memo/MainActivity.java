package com.example.memo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import static android.R.id.list;

public class MainActivity extends AppCompatActivity {
    Button bn;
    String[] date={"1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1",};
    MyDatabaseHelper dbmemo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbmemo=new MyDatabaseHelper(this,"memo.db",null,1);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(MainActivity.this,R.layout.support_simple_spinner_dropdown_item,date);
        ListView listView=(ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s=date[position];
                Toast.makeText(MainActivity.this,s,Toast.LENGTH_SHORT).show();
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
