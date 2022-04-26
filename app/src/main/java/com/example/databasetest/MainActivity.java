package com.example.databasetest;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MyDatabaseHelper dbHelper;
    private Button createDatabaseBtn;
    private Button addBtn;
    private Button updateBtn;
    private Button deleteBtn;
    private Button queryBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 1);

        createDatabaseBtn = this.findViewById(R.id.create_database);
        addBtn = this.findViewById(R.id.add_data);
        updateBtn = this.findViewById(R.id.update_data);
        deleteBtn = this.findViewById(R.id.delete_data);
        queryBtn = this.findViewById(R.id.query_data);

        createDatabaseBtn.setOnClickListener(this);
        addBtn.setOnClickListener(this);
        updateBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        queryBtn.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        SQLiteDatabase db = null;
        ContentValues values = null;
        switch (view.getId()) {
            case R.id.create_database:
                dbHelper.getWritableDatabase();
                /*
                    第一次点击按钮时，就会检测到程序中没有BookStore.db这个数据库
                    然后会创建该数据库，并调用MyDatabaseHelper类的onCreate()方法，这样就会执行建表语句并弹出toast
                    再次点击按钮时，程序中已经存在该数据库就不会再次创建
                 */
                break;
            case R.id.add_data:
                db = dbHelper.getWritableDatabase();
                values = new ContentValues();

                values.put("author", "yxq");
                values.put("name", "why you sleep?");
                values.put("pages", 100);
                values.put("price", 100);
                db.insert("Book", null, values);//第二个参数一般为null

                values.clear();
                values.put("author", "wt");
                values.put("name", "why you eat?");
                values.put("pages", 200);
                values.put("price", 10.6);
                db.insert("Book", null, values);//第二个参数一般为null

                Toast.makeText(MainActivity.this, "add succeeded", Toast.LENGTH_SHORT).show();
                break;
            case R.id.update_data:
                db = dbHelper.getWritableDatabase();
                values = new ContentValues();
                values.put("price", 10.99);
                db.update("Book", values, "name = ?", new String[]{"why you sleep?"});
                Toast.makeText(MainActivity.this, "update succeeded", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete_data:
                db = dbHelper.getWritableDatabase();
                db.delete("Book", "id = ?", new String[]{"1"});
                Toast.makeText(MainActivity.this, "delete succeeded", Toast.LENGTH_SHORT).show();
                break;
            case R.id.query_data:
                db = dbHelper.getWritableDatabase();
                //查询表中所有数据
                Cursor cursor = db.query("Book", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        //遍历Cursor对象，取出数据并打印
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        Log.d(TAG, "name == " + name);
                    } while (cursor.moveToNext());
                }

                cursor.close();
                break;
            default:
                break;
        }


    }
}