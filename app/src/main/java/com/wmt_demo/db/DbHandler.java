package com.wmt_demo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wmt_demo.model.Brand;
import com.wmt_demo.utility.DateTimeUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vishal on 16-12-2017.
 */

public class DbHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "contactsManager";

    // Contacts table name
    private static final String TABLE_BRAND = "brand";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_SYNC_STATUS = "sync_status";


    public DbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BRAND_TABLE = "CREATE TABLE " + TABLE_BRAND + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_DESCRIPTION + " TEXT," + KEY_CREATED_AT + " TEXT," + KEY_SYNC_STATUS + " TEXT)";
        db.execSQL(CREATE_BRAND_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BRAND);

        // Create tables again
        onCreate(db);
    }


    // Adding new brand
    public void addBrand(Brand brand) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, brand.getName()); // Brand Name
        values.put(KEY_DESCRIPTION, brand.getDescription()); // Brand description
        values.put(KEY_CREATED_AT, DateTimeUtility.getCurrentDateTime()); // date time
        values.put(KEY_SYNC_STATUS, brand.getSyncStatus());

        // Inserting Row
        db.insert(TABLE_BRAND, null, values);
        db.close(); // Closing database connection
    }

    // Getting All brands
    public List<Brand> getAllBrand() {
        List<Brand> brandList = new ArrayList<Brand>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_BRAND;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Brand brand = new Brand();
                brand.setId(Integer.parseInt(cursor.getString(0)));
                brand.setName(cursor.getString(1));
                brand.setDescription(cursor.getString(2));
                brand.setCreatedAt(cursor.getString(3));
                brand.setSyncStatus(cursor.getString(4));
                // Adding brand to list
                brandList.add(brand);
            } while (cursor.moveToNext());
        }

        // return brand list
        return brandList;
    }

    // Get un sync recode
    public List<Brand> getAllUnSyncBrand() {
        List<Brand> brandList = new ArrayList<Brand>();

        String selectQuery = "SELECT  * FROM " + TABLE_BRAND + " WHERE " + KEY_SYNC_STATUS + " = " + "0";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Brand brand = new Brand();
                brand.setId(Integer.parseInt(cursor.getString(0)));
                brand.setName(cursor.getString(1));
                brand.setDescription(cursor.getString(2));
                brand.setCreatedAt(cursor.getString(3));
                brand.setSyncStatus(cursor.getString(4));
                // Adding brand to list
                brandList.add(brand);
            } while (cursor.moveToNext());
        }

        // return brand list
        return brandList;
    }

    // Delete brand recode
    public void deleteAllBrand()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_BRAND);
    }
}
