package com.nikhilkumar.mopidevi.pricetracker;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by NIKHIL on 19-Feb-15.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {

    Context context;

    SQLiteDatabase db;

    String url;
    String title;
    String init_price;
    String cur_price;
    String img_path;


    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ProductsDB";

    // Products table name
    private static final String TABLE_NAME = "products";

    // Products Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_URL = "url";
    private static final String KEY_TITLE = "title";
    private static final String KEY_INIT_PRICE = "init_price";
    private static final String KEY_CUR_PRICE = "cur_price";
    private static final String KEY_IMAGE = "img_path";
//  private static final String[] COLUMNS = {KEY_ID, KEY_URL, KEY_TITLE, KEY_INIT_PRICE,KEY_CUR_PRICE, KEY_IMAGE};

    //An array of database
    String[][] table = new String[30][6];

    int len = 0; //no.of rows in the table

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        this.db = db;

        // SQL statement to create a products table
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "url TEXT, " +
                "title TEXT, " +
                "init_price TEXT," +
                "cur_price TEXT," +
                "img_path TEXT" +
                " );";

        // create products table
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public void open() throws SQLException {

        db = getWritableDatabase();
    }

    public void close() {
       if(db!=null)
        db.close();
    }

    int add(String url, String title, String init_price, String cur_price, String img_path) {

        this.url = url;
        this.title = title;
        this.init_price =init_price;
        this.cur_price = cur_price;
        this.img_path = img_path;

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

       if(alreadyExists(url)) {

            return 0;
        }
        else
        {
            // 2. create ContentValues to add key "column"/value
            ContentValues values = new ContentValues();
            values.put(KEY_URL, url);
            values.put(KEY_TITLE, title);
            values.put(KEY_INIT_PRICE, init_price);
            values.put(KEY_CUR_PRICE, cur_price);
            values.put(KEY_IMAGE, img_path);

            // 3. insert
            db.insert(TABLE_NAME, // table
                    null, //nullColumnHack
                    values); // key/value -> keys = column names/ values = column values

            // 4. close
            db.close();
            return 1;
        }
    }

    void read() {

        int i = 0;

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {

                table[i][0] = cursor.getString(0);
                table[i][1] = cursor.getString(1);
                table[i][2] = cursor.getString(2);
                table[i][3] = cursor.getString(3);
                table[i][4] = cursor.getString(4);
                table[i][5] = cursor.getString(5);

                i++;
            } while (cursor.moveToNext());
        }
    }

    boolean alreadyExists(String url) {
        int i = 0;
        String[] allUrls = new String[30];
        len = getLength();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {

                for (i = 0; i < len; i++) {
                    allUrls[i] = cursor.getString(1);
                }
            } while (cursor.moveToNext());
        }

        for (i = 0; i < len; i++) {
            if (allUrls[i].equals(url))
                return true;
        }

        return false;
    }


    String[][] getTable() {
        return table;
    }

    public int getLength()
    {
        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {

                len++;
            } while (cursor.moveToNext());
        }

        return len;
    }

    int update(int index, String price)
    {
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();

        values.put(KEY_URL,table[index][1]);
        values.put(KEY_TITLE,table[index][2]);
        values.put(KEY_INIT_PRICE,table[index][3]);
        values.put(KEY_CUR_PRICE,price);
        values.put(KEY_IMAGE,table[index][5]);

        // 3. updating row
        int i = db.update(TABLE_NAME, //table
                values, // column/value
                KEY_ID+" = ", // selections
                new String[] { String.valueOf(index + 1) }); //selection args

        // 4. close
        db.close();

        return i;
    }

    int delete(int id)
    {

        SQLiteDatabase db = this.getWritableDatabase();

        if(db.delete(TABLE_NAME, KEY_ID + "=" + id, null)>0) {
            return 1;
        }
        else {
          return 0;
        }

    }

  /*
    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {

        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
*/

    public String saveToInternalStorage(Bitmap bitmapImage, String name){
        ContextWrapper cw = new ContextWrapper(context);

        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

        // Create imageDir
        File myPath=new File(directory,name);

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(myPath);

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return directory.getAbsolutePath()+"/"+name;
    }
}

