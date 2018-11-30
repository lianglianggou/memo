package com.example.memo;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.R.attr.author;

import static android.R.id.list;

public class MainActivity extends AppCompatActivity {
    Button bn;
    //String[] date=new String[20];
    ArrayList<String> date=new ArrayList<String>();
    //String[] date={"aaaaa","bbbb"};
    int count=0;
    MyDatabaseHelper dbmemo;
    EditText e;
    ListView mListView;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.find:
                final EditText et = new EditText(this);

                et.setText("");
                new AlertDialog.Builder(this).setTitle("请输入：")
                        .setIcon(android.R.drawable.sym_def_app_icon)
                        .setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String a=et.getText().toString();
                                String[] where1 = {"%"+a+"%"};
                                //final String[] where1={a};
                                SQLiteDatabase db=dbmemo.getWritableDatabase();


                                String sql = "select "+ "author" + " from " + "memo"
                                        + " where " + "author" + " like ? ";
                                Cursor cursor = db.rawQuery(sql,where1);




                                // Cursor cursor = db.query("WordTable",null,null,null,null,null,null);
                                //Cursor cursor =  db.query("memo",new String[]{"author"},author+ " like ? ",new String[]{"%"+a+"%"},null,null,null);
                                date.clear();
                                if(cursor.moveToFirst()){
                                    do{
                                        String author=cursor.getString(cursor.getColumnIndex("author"));
                                        date.add(author);
                                    }while (cursor.moveToNext());
                                    cursor.close();
                                    //oast.makeText(MainActivity.this,"success",Toast.LENGTH_SHORT).show();
                                }


                                ArrayAdapter<String> adapter=new ArrayAdapter<String>(MainActivity.this,R.layout.support_simple_spinner_dropdown_item,date);
                                ListView listView=(ListView)findViewById(R.id.list_view);
                                listView.setAdapter(adapter);

                            }
                        }).setNegativeButton("取消",null).show();
//                Intent intent = new Intent(MainActivity.this, CheckActivity.class);//实现点击菜单选项启动相应活动
//                startActivity(intent);
                //checkDialog();
//                Toast.makeText(this,"check",Toast.LENGTH_SHORT).show();
                break;

            default:
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        e=(EditText)findViewById(R.id.textt);
        dbmemo=new MyDatabaseHelper(this,"memo.db",null,1);
        mListView=(ListView) findViewById(R.id.list_view);
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
        ItemOnLongClick1();

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
        /*创建数据库语句  一次就够
        bn=(Button)findViewById(R.id.create);
        bn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                dbmemo.getWritableDatabase();
            }
        });*/
    }
    private void ItemOnLongClick1() {
        mListView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {

                    public void onCreateContextMenu(ContextMenu menu, View v,
                                                    ContextMenu.ContextMenuInfo menuInfo) {
                        menu.add(0, 0, 0, "删除");
                        menu.add(0, 1, 0, "修改");
                        menu.add(0, 2, 0, "上升");
                        menu.add(0, 3, 0, "置顶");

                    }
                });
    }
    public boolean onContextItemSelected(MenuItem item) {
        dbmemo=new MyDatabaseHelper(this,"memo.db",null,1);
        SQLiteDatabase db=dbmemo.getWritableDatabase();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        int MID = (int) info.id;// 这里的info.id对应的就是数据库中_id的值

        switch (item.getItemId()) {
            case 0:
                String[] where={date.get(MID)};
                db.delete("memo","author=?",where);
                date.remove(MID);
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(MainActivity.this,R.layout.support_simple_spinner_dropdown_item,date);
                ListView listView=(ListView)findViewById(R.id.list_view);
                listView.setAdapter(adapter);
                break;

            case 1:
                final EditText et = new EditText(this);
                final String[] where1={date.get(MID)};
                final int c=MID;
                et.setText(date.get(MID));
                new AlertDialog.Builder(this).setTitle("请更改")
                        .setIcon(android.R.drawable.sym_def_app_icon)
                        .setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SQLiteDatabase db=dbmemo.getWritableDatabase();
                                String a=et.getText().toString();
                                ContentValues values=new ContentValues();
                                values.put("author",a);
                                db.update("memo",values,"author=?",where1);
                                date.set(c,a);
                                ArrayAdapter<String> adapter=new ArrayAdapter<String>(MainActivity.this,R.layout.support_simple_spinner_dropdown_item,date);
                                ListView listView=(ListView)findViewById(R.id.list_view);
                                listView.setAdapter(adapter);
                            }
                        }).setNegativeButton("取消",null).show();
                break;
            case 2:
                String xx=date.get(MID);
                String yy=date.get(MID-1);
                final String[] where2={date.get(MID-1)};
                final String[] where3={date.get(MID)};
                final String[] where4={"*"};
                ContentValues values0=new ContentValues();
                values0.put("author","*");
                ContentValues values=new ContentValues();
                values.put("author",xx);
                db.update("memo",values0,"author=?",where2);
                date.set(MID-1,xx);
                ContentValues values1=new ContentValues();
                values1.put("author",yy);
                db.update("memo",values1,"author=?",where3);
                db.update("memo",values,"author=?",where4);
                date.set(MID,yy);
                ArrayAdapter<String> adapter1=new ArrayAdapter<String>(MainActivity.this,R.layout.support_simple_spinner_dropdown_item,date);
                ListView listView1=(ListView)findViewById(R.id.list_view);
                listView1.setAdapter(adapter1);
                break;
            case 3:
                String xxx=date.get(MID);
                String yyy=date.get(0);
                final String[] where22={date.get(0)};
                final String[] where33={date.get(MID)};
                final String[] where44={"*"};
                ContentValues values00=new ContentValues();
                values00.put("author","*");
                ContentValues values2=new ContentValues();
                values2.put("author",xxx);
                db.update("memo",values00,"author=?",where22);
                date.set(0,xxx);
                ContentValues values3=new ContentValues();
                values3.put("author",yyy);
                db.update("memo",values3,"author=?",where33);
                db.update("memo",values2,"author=?",where44);
                date.set(MID,yyy);
                ArrayAdapter<String> adapter2=new ArrayAdapter<String>(MainActivity.this,R.layout.support_simple_spinner_dropdown_item,date);
                ListView listView2=(ListView)findViewById(R.id.list_view);
                listView2.setAdapter(adapter2);
                break;
            default:
                break;
        }

        return super.onContextItemSelected(item);

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
