package com.example.databasetest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ProxyInfo;
import android.net.Uri;
import android.os.Parcelable;

public class DatabaseProvider extends ContentProvider {

    public final static int BOOK_DIR = 0;//访问book的所有数据
    public final static int BOOK_ITEM = 1;//访问book的单条数据
    public final static int CATEGORY_DIR = 2;//访问category的所有数据
    public final static int CATEGORY_ITEM = 3;//访问category的单条数据

    public final static String AUTHORITY = "com.example.databasetest.provider";

    private static UriMatcher uriMatcher;
    private MyDatabaseHelper dbHelper;

    /**
     * uriMatcher.addURI三个参数：authority, path, 自定义代码
     * 当调用uriMatcher.match(uri)方法时，返回某个能匹配这个Uri对象所对应的自定义代码
     * 利用这个代码可以判断出调用方期望访问的是哪张表的数据了
     */
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "book", BOOK_DIR);
        uriMatcher.addURI(AUTHORITY, "book/#", BOOK_ITEM);
        uriMatcher.addURI(AUTHORITY, "category", CATEGORY_DIR);
        uriMatcher.addURI(AUTHORITY, "category/#", CATEGORY_ITEM);
    }

    public DatabaseProvider() {
    }

    /**
     * 删除数据
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return 返回被删除的行数
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deleteRow = 0;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                deleteRow = db.delete("Book", selection, selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                deleteRow = db.delete("Book", "id = ?", new String[]{bookId});
                break;
            case CATEGORY_DIR:
                deleteRow = db.delete("Category", selection, selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                deleteRow = db.delete("Category", "id = ?", new String[]{categoryId});
                break;
            default:
                break;

        }
        return deleteRow;
    }

    /**
     * 获取Uri对象所对应的MIME类型
     * @param uri
     * @return
     */
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.databasetest.provider.book";
            case BOOK_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.databasetest.provider.book";
            case CATEGORY_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.databasetest.provider.category";
            case CATEGORY_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.databasetest.provider.category";
            default:
                break;
        }
        return null;
    }

    /**
     * 插入数据
     * @param uri
     * @param values
     * @return 以新增数据id结尾的Uri
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri uriReturn = null;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
            case BOOK_ITEM:
                long newBookId = db.insert("Book", null, values);
                uriReturn = Uri.parse("content://" + AUTHORITY + "/book/" + newBookId);
                break;
            case CATEGORY_DIR:
            case CATEGORY_ITEM:
                long categoryId = db.insert("Category", null, values);
                uriReturn = Uri.parse("content://" + AUTHORITY + "/category/" + categoryId);
                break;
            default:
                break;
        }
        return uriReturn;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new MyDatabaseHelper(getContext(), "BookStore.db", null, 5);
        return true;
    }

    /**
     * 数据查询
     * 其中，uri.getPathSegments()：
     *  将内容URI权限之后的部分以“/"符号进行分割，并把分割的结果放到一个字符串列表中，
     *  那么这个列表第0个位置就是路径，第一个位置就是id
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                cursor = db.query("Book", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                cursor = db.query("Book", projection, "id = ?", new String[]{bookId}, null, null, sortOrder);
                break;
            case CATEGORY_DIR:
                cursor = db.query("Category", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                cursor = db.query("Category", projection, "id = ?", new String[]{categoryId}, null, null, sortOrder);
                break;
            default:
                break;
        }
        return cursor;
    }

    /**
     * 数据更新
     * @param uri
     * @param values
     * @param selection
     * @param selectionArgs
     * @return 返回受影响的行数
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int updateRows = 0;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                updateRows = db.update("Book", values, selection, selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                updateRows = db.update("Book", values, "id = ?", new String[]{bookId});
                break;
            case CATEGORY_DIR:
                updateRows = db.update("Category", values, selection, selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                updateRows = db.update("Category", values, "id = ?", new  String[]{categoryId});
                break;
            default:
                break;
        }
        return updateRows;
    }
}