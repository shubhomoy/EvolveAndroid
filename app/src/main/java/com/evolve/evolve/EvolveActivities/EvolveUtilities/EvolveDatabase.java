package com.evolve.evolve.EvolveActivities.EvolveUtilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.evolve.evolve.EvolveActivities.EvolveObjects.Image;

import java.sql.SQLClientInfoException;

/**
 * Created by vellapanti on 19/9/15.
 */
public class EvolveDatabase {
    SQLiteDatabase db;
    Context context;
    DbHelper dbHelper;
    public EvolveDatabase(Context context){
        this.context = context;
        dbHelper = new DbHelper(context);
    }
    public static final int Database_Version=1;
    public static final String Database_Name="EvolveDatabase";
    public static final String Table_Name="pictureinfo";
    public static final String Picture_Id="id";
    public static final String Picture_Name="name";
    public static final String Picture_Description="description";
    public static final String Picture_Date="date";
    public static final String Picture_Latitude="latitude";
    public static final String Picture_Longitude="longitude";
    public static final String Picture_Slug="slug";
    public static final String Image_ExifTag="image_id";

    public void insertInformation(Image image){
        ContentValues cv =new ContentValues();
        cv.put(Picture_Name,image.name);
        cv.put(Picture_Description,image.description);
        cv.put(Picture_Date,image.photo_date);
        cv.put(Picture_Latitude,image.lat);
        cv.put(Picture_Longitude,image.lon);
        cv.put(Image_ExifTag,image.id);

    }
    public boolean checkExif(Image img){
        Cursor cursor= db.rawQuery("select * from "+ Table_Name + " where " + Image_ExifTag + "="+ img.id,null);
        if(cursor.getCount()==0)
             return false;
        else
             return true;
    }
    private static class DbHelper extends SQLiteOpenHelper{
        String tableCreate = "CREATE TABLE "+Table_Name+"("
                +Picture_Id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +Picture_Name + " VARCHAR(50), "
                +Picture_Description + " TEXT, "
                +Picture_Date+" VARCHAR(50), "
                +Picture_Latitude+" VARCHAR(50), "
                +Picture_Longitude+" VARCHAR(50), "
                +Picture_Slug + " VARCHAR(50), "
                +Image_ExifTag + " INTEGER);";


        public DbHelper(Context context) {
            super(context,Database_Name,null,Database_Version);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(tableCreate);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    public void close() {
        db.close();
    }

    public void open() throws SQLiteException {
        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLiteException ex) {
            db = dbHelper.getReadableDatabase();
        }
    }


}
