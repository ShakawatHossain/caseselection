package com.example.caseselection.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "caseSelection";
    private static String KEY_ID = "id";
    public static String SELF_ID = "self_id";
    public static String NAME = "name";
    public static String PARENT_ID = "parent_id";
    private static String TABLE_DIS = "dis";
    private static String TABLE_UP = "up";
    private static String TABLE_MC = "mc";
    private static String TABLE_THANA = "thana";
    private static String TABLE_CITY = "city";
    private static String TABLE_FACILITIES = "facility";
    private static String TABLE_LAB = "lab";

    // Table Create Statements
    // Todo table create statement
    private static final String CREATE_DIS = "CREATE TABLE " + TABLE_DIS
            + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + NAME + " TEXT )" ;

    private static final String CREATE_UP = "CREATE TABLE " + TABLE_UP
            + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + SELF_ID + " TEXT,"
            + NAME + " TEXT" + ")";

    private static final String CREATE_MC = "CREATE TABLE " + TABLE_MC
            + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + SELF_ID + " INTEGER,"
            + NAME + " TEXT,"
            + PARENT_ID + " INTEGER" + ")";

    private static final String CREATE_THANA = "CREATE TABLE "+ TABLE_THANA
            + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + SELF_ID + " INTEGER,"
            + NAME + " TEXT,"
            + PARENT_ID + " INTEGER" + ")";
    private static final String CREATE_CITY = "CREATE TABLE "+ TABLE_CITY
            + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + SELF_ID + " INTEGER,"
            + NAME + " TEXT,"
            + PARENT_ID + " INTEGER" + ")";
    private static final String CREATE_FACILITIES = "CREATE TABLE "+ TABLE_FACILITIES
            + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + SELF_ID + " TEXT,"        //uid as api
            + NAME + " TEXT" + ")";
    private static final String CREATE_LAB = "CREATE TABLE "+ TABLE_LAB
            + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + NAME + " TEXT" + ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        Log.d("DIS",CREATE_DIS);
        Log.d("DIS",CREATE_UP);
        Log.d("DIS",CREATE_MC);
        Log.d("DIS",CREATE_THANA);
        Log.d("DIS",CREATE_CITY);
        Log.d("FAS",CREATE_FACILITIES);
        Log.d("LAB",CREATE_LAB);
        db.execSQL(CREATE_DIS);
        db.execSQL(CREATE_UP);
        db.execSQL(CREATE_MC);
        db.execSQL(CREATE_THANA);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_FACILITIES);
        db.execSQL(CREATE_LAB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MC);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_THANA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FACILITIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LAB);
        onCreate(db);
    }
//1
    public void insertDIS(ContentValues cv){
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_DIS, null, cv);
    }

    public Cursor getDIS(){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_DIS ;
        Cursor c = db.rawQuery(selectQuery, null);
        return c;
    }
    public Cursor getDIS(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_DIS + " WHERE "+ NAME + " = '"+ name + "'";
        Cursor c = db.rawQuery(selectQuery, null);
        return c;
    }
//2
    public void insertUP(ContentValues cv){
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_UP, null, cv);
    }

    public Cursor getUP(String selfID, int flag){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_UP +" WHERE "+ SELF_ID +" = "+selfID;
        Cursor c = db.rawQuery(selectQuery, null);
        return c;
    }
    public Cursor getUP(){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_UP ;
        Cursor c = db.rawQuery(selectQuery, null);
        return c;
    }
    public Cursor getUP(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_UP + " WHERE "+ NAME + " = '"+ name + "'";
        Cursor c = db.rawQuery(selectQuery, null);
        return c;
    }
//    3
    public void insertMC(ContentValues cv){
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_MC, null, cv);
    }

    public Cursor getMC(int selfID){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_MC +" WHERE "+ SELF_ID +" = "+selfID;
        Cursor c = db.rawQuery(selectQuery, null);
        return c;
    }
    public Cursor getMC(){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_MC;
        Cursor c = db.rawQuery(selectQuery, null);
        return c;
    }
    public Cursor getMC(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_MC + " WHERE "+ NAME + " = '"+ name + "'";
        Cursor c = db.rawQuery(selectQuery, null);
        return c;
    }
//    4
    public void insertTHANA(ContentValues cv){
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_THANA, null, cv);
    }

    public Cursor getTHANA(int selfID){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_THANA +" WHERE "+ SELF_ID +" = "+selfID;
        Cursor c = db.rawQuery(selectQuery, null);
        return c;
    }
    public Cursor getTHANA(){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_THANA;
        Cursor c = db.rawQuery(selectQuery, null);
        return c;
    }
    public Cursor getTHANA(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_THANA + " WHERE "+ NAME + " = '"+ name +"' ";
        Cursor c = db.rawQuery(selectQuery, null);
        return c;
    }
//    5
    public void insertCITY(ContentValues cv){
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_CITY, null, cv);
    }

    public Cursor getCITY(int selfID){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_CITY +" WHERE "+ SELF_ID +" = "+ selfID;
        Cursor c = db.rawQuery(selectQuery, null);
        return c;
    }
    public Cursor getCITY(){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_CITY;
        Cursor c = db.rawQuery(selectQuery, null);
        return c;
    }
    public Cursor getCITY(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_CITY + " WHERE "+ NAME + " = '"+ name +"' ";
        Cursor c = db.rawQuery(selectQuery, null);
        return c;
    }
//    6 Facility
    public void insertFacility(ContentValues cv){
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_FACILITIES, null, cv);
    }
    public Cursor getFacility(String selfID ,int flag){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_FACILITIES +" WHERE "+ SELF_ID +" = "+ selfID;
        Cursor c = db.rawQuery(selectQuery, null);
        return c;
    }
    public Cursor getFacility(){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_FACILITIES;
        Cursor c = db.rawQuery(selectQuery, null);
        return c;
    }
    public Cursor getFacility(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_FACILITIES + " WHERE "+ NAME + " = '"+ name +"' ";
        Cursor c = db.rawQuery(selectQuery, null);
        return c;
    }
    //    7 LAB
    public void insertLab(ContentValues cv){
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_LAB, null, cv);
    }
    public Cursor getLab(){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_LAB;
        Cursor c = db.rawQuery(selectQuery, null);
        return c;
    }
    public Cursor getLab(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_LAB + " WHERE "+ NAME + " = '"+ name +"' ";
        Cursor c = db.rawQuery(selectQuery, null);
        return c;
    }
}
