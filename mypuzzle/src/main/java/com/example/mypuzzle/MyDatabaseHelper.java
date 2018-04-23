package com.example.mypuzzle;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by DoubleJ on 2017/7/11.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_USER = "create table User ( "
    + " userName text primary key , "
    + " userPassword text ) ";
    public static final String CREATE_EASYRANKINGLIST = "create table EasyRankingList ( "
            + " userName text primary key , "
            + " steps integer , "
            + " times text ) ";
    public static final String CREATE_MIDDLERANKINGLIST = "create table MiddleRankingList ( "
            + " userName text primary key , "
            + " steps integer , "
            + " times text ) ";
    public static final String CREATE_HARDRANKINGLIST = "create table HardRankingList ( "
            + " userName text primary key , "
            + " steps integer , "
            + " times text ) ";
    private Context context;
    public MyDatabaseHelper(Context context, String name,
                            SQLiteDatabase.CursorFactory factory,
                            int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    public MyDatabaseHelper(Context context, String name,
                            SQLiteDatabase.CursorFactory factory,
                            int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER);
        db.execSQL("insert into User (userName,userPassword) values(?,?)",
                new String[]{"Lily","123456"});
        db.execSQL(CREATE_EASYRANKINGLIST);
        db.execSQL(CREATE_MIDDLERANKINGLIST);
        db.execSQL(CREATE_HARDRANKINGLIST);
       // Toast.makeText(context,"Create User Success",Toast.LENGTH_LONG).show();


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*switch (oldVersion){
            case 1:
                db.execSQL(CREATE_EASYRANKINGLIST);
                db.execSQL(CREATE_MIDDLERANKINGLIST);
                db.execSQL(CREATE_HARDRANKINGLIST);
                Toast.makeText(context,"Create RankingList Success",Toast.LENGTH_LONG).show();
                //break;
            default:

        }*/

    }
}
