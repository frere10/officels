package rw.akimana.officels.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

import rw.akimana.officels.Models.IpAddress;

public class DatabaseHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "d2dstore.db";
    private static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
            IpAddress.IpAttributes.TABLE_NAME + " ( " +
            IpAddress.IpAttributes.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            IpAddress.IpAttributes.COL_PROTOCAL + " TEXT, " +
            IpAddress.IpAttributes.COL_IPADDRESS + " TEXT)";
    private static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + IpAddress.IpAttributes.TABLE_NAME;
    private static final String SELECT_ALL_FROM_IP = "SELECT * FROM "+ IpAddress.IpAttributes.TABLE_NAME;
    private static final String SELECT_IP_BY_ID = "SELECT * FROM "+ IpAddress.IpAttributes.TABLE_NAME+ " WHERE "+ IpAddress.IpAttributes.COL_ID + " = ? ";

    private SQLiteOpenHelper openHelper;

    public DatabaseHelper(Context context){
        openHelper = new SQLiteDBOpenHelper(context);
    }
    class SQLiteDBOpenHelper extends SQLiteOpenHelper {

        public SQLiteDBOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_TABLE);
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }
    public Cursor find(){
        SQLiteDatabase db = openHelper.getReadableDatabase();
        if (db == null){
            return null;
        }
        if (!tableExists(db, IpAddress.IpAttributes.TABLE_NAME)){
            crateTable();
        }
        return db.rawQuery(SELECT_ALL_FROM_IP, null);
    }

    public ContentValues findById(long id){
        SQLiteDatabase db = openHelper.getReadableDatabase();
        if (db == null){
            return null;
        }
        ContentValues row = new ContentValues();
        Cursor cursor = db.rawQuery(SELECT_IP_BY_ID, new String[]{String.valueOf(id)});
        if(cursor.moveToNext()){
            row.put(IpAddress.IpAttributes.COL_ID, cursor.getString(0));
            row.put(IpAddress.IpAttributes.COL_PROTOCAL, cursor.getString(1));
            row.put(IpAddress.IpAttributes.COL_IPADDRESS, cursor.getString(2));
        }
        cursor.close();
        db.close();
        return row;
    }

    public HashMap<String, String> findIpDetails() {
        HashMap<String, String> data = new HashMap<String, String>();

        SQLiteDatabase db = openHelper.getReadableDatabase();
        if (!tableExists(db, IpAddress.IpAttributes.TABLE_NAME)){
            crateTable();
            data.put(IpAddress.IpAttributes.COL_ID, "10");
            data.put(IpAddress.IpAttributes.COL_PROTOCAL, "http");
            data.put(IpAddress.IpAttributes.COL_IPADDRESS, "10.0.2.2");
            Log.d("----Creating----", "Fetching data from static: " + data.toString());
            return data;
        }
        else {
            Cursor cursor = db.rawQuery(SELECT_ALL_FROM_IP, null);
            // Move to first row
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                data.put(IpAddress.IpAttributes.COL_ID, cursor.getString(0));
                data.put(IpAddress.IpAttributes.COL_PROTOCAL, cursor.getString(1));
                data.put(IpAddress.IpAttributes.COL_IPADDRESS, cursor.getString(2));
            }
            else return null;
            cursor.close();
            db.close();
            // return user
            Log.d("----Retrieving----", "Fetching user from Sqlite: " + data.toString());

            return data;
        }
    }

    public long save(String protocal, String ip_address){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        if (db == null){
            return 0;
        }
        ContentValues values = new ContentValues();
        values.put(IpAddress.IpAttributes.COL_PROTOCAL, protocal);
        values.put(IpAddress.IpAttributes.COL_IPADDRESS, ip_address);

        db.insert(IpAddress.IpAttributes.TABLE_NAME, null, values);
        db.close();
        return 1;
    }

    public void delete(long id){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        if (db == null){
            return;
        }
        db.delete(IpAddress.IpAttributes.TABLE_NAME, IpAddress.IpAttributes.COL_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public long update(long id, String protocal, String ip_address){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        if (db == null){
            return 0;
        }
        ContentValues values = new ContentValues();
        values.put(IpAddress.IpAttributes.COL_PROTOCAL, protocal);
        values.put(IpAddress.IpAttributes.COL_IPADDRESS, ip_address);
        db.update(IpAddress.IpAttributes.TABLE_NAME, values, IpAddress.IpAttributes.COL_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return 1;
    }
    public long deleteTable(String table_name){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        if(db == null){
            return 0;
        }
        db.execSQL("DROP TABLE IF EXISTS "+table_name);
        db.close();
        return 1;
    }
    public long crateTable(){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        if(db == null){
            return 0;
        }
        db.execSQL(SQL_CREATE_TABLE);
        db.close();
        return 1;
    }
    public boolean tableExists(SQLiteDatabase db, String tableName)
    {
        String select_query = "SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?";
        if (tableName == null || db == null || !db.isOpen())
        {
            return false;
        }
        Cursor cursor = db.rawQuery(select_query, new String[] {"table", tableName});
        if (!cursor.moveToFirst())
        {
            cursor.close();
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }
}
