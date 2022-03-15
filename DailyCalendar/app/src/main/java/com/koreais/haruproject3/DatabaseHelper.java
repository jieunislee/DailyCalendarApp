package com.koreais.haruproject3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = "calendar_table";
    private static final String COL1 = "ID";
    private static final String COL2 = "date";
    private static final String COL3 = "todo";

    String selectedDate;


    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" + COL1 +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                              COL2 + " TEXT, " + COL3 + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public boolean addData(String date, String todo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, date);
        contentValues.put(COL3, todo);

        Log.d(TAG, "addData: Adding " + date + " to " + TABLE_NAME );
        Log.d(TAG, "addData: Adding " + todo + " to " + TABLE_NAME );


        long result = db.insert(TABLE_NAME, null, contentValues);

        // if data as inserted incorrectly it will return -1
        if(result == -1){
            return false;
        } else {
            return true;
        }
    }

    /**
     * Returns all the data from database where date is equal to the clicked date on calendar
     * @return date
     */
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE date LIKE '%" + selectedDate + "%'";
        Log.d(TAG,selectedDate);
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void passDate(String dateForDB){
        selectedDate = dateForDB;
    }

    /**
     * Returns only the ID that matches the name passed in
     * @param todo
     * @return
     */
    public Cursor getItemID(String todo){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL1 + " FROM " + TABLE_NAME + " WHERE " + COL3 + " = '" + todo + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getItemDate(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Updates the name field
     * @param newName
     * @param id
     * @param oldName
     */
    public void updateName(String newName, int id, String oldName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL3 +
                " = '" + newName + "' WHERE " + COL1 + " = '" + id + "'" +
                " AND " + COL3 + " = '" + oldName + "'";
        Log.d(TAG, "updateName: query: " + query);
        Log.d(TAG, "updateName: Setting todo to " + newName);
        db.execSQL(query);
    }

    /**
     * Delete from database
     * @param id
     * @param todo
     */
    public void deleteName(int id, String todo){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL1 + " = '" + id + "'" +
                " AND " + COL3 + " = '" + todo + "'";
        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + todo + " from database.");
        db.execSQL(query);
    }


}
