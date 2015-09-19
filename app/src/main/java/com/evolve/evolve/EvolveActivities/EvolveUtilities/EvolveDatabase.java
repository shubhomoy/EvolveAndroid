package com.evolve.evolve.EvolveActivities.EvolveUtilities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLClientInfoException;

/**
 * Created by vellapanti on 19/9/15.
 */
public class EvolveDatabase {
    SQLiteDatabase sqLiteDatabase;
    PictureInformation pictureInformation;
    public EvolveDatabase(Context context){
        pictureInformation=new PictureInformation(context);
        sqLiteDatabase=pictureInformation.getWritableDatabase();
    }
    public static final int Database_Version=1;
    public static final String Database_Name="EvolveDatabase";
    public static final String Table_Name="pictueinfo";
    public static final String Picture_Id="id";
    public static final String Picture_Name="name";
    public static final String Picture_Description="description";
    public static final String Picture_Date="date";
    public static final String Picture_Latitude="latitude";
    public static final String Picture_Longitude="longitude";
    public static final String Picture_Slug="slug";
    public static final String Image_Id="image_id";


    private static class PictureInformation extends SQLiteOpenHelper{
        String tableCreate = "CREATE TABLE "+Table_Name+"("
                +Picture_Id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +Picture_Name + " VARCHAR(50), "
                +Picture_Description + " TEXT, "
                +Picture_Date+" VARCHAR(50), "
                +Picture_Latitude+" VARCHAR(50), "
                +Picture_Longitude+" VARCHAR(50), "
                +Picture_Slug + " VARCHAR(50), "
                +Image_Id + " INTEGER);";
        public PictureInformation(Context context) {
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
}
