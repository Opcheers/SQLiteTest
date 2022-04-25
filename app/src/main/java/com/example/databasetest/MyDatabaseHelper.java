package com.example.databasetest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_BOOK = "create table Book (" +
            "id integer primary key autoincrement, " +
            "author text, " +
            "price real, " +
            "pages integer, " +
            "name text)";

    public static final String CREATE_CATEGORY = "create table Category(" +
            "id integer primary key autoincrement," +
            "category_name text," +
            "category_code integer)";

    private Context mContext;

    /**
     * @param context
     * @param name 数据库名，创建数据库时使用这个名字；
     * @param factory 允许我们在查询数据时返回一个自定义的Cursor，一般传入null；
     * @param version 数据库的版本号
     */
    public MyDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    /**
     * 只有创建数据库的时候才会调用一次
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_BOOK);//执行建表语句
        sqLiteDatabase.execSQL(CREATE_CATEGORY);

    }

    /**
     * 升级数据库
     * 数据库版本号升级时，就会调用该方法
     * @param sqLiteDatabase
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists Book");
        sqLiteDatabase.execSQL("drop table if exists Category");
        onCreate(sqLiteDatabase);
    }
}
