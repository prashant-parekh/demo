package com.example;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class DBAdapter {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_FNAME = "fname";
    public static final String KEY_LNAME = "lname";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_COMPANY = "company";
    public static final String KEY_CITY = "city";
    public static final String KEY_PINCODE = "pincode";
    public static final String KEY_MOBILENO = "mobileno";

    private static final String TAG = "DBAdapter";

    private static final String DATABASE_NAME = "MyDB";
    private static final String DATABASE_TABLE = "contacts";
    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_CREATE = "create table contacts (_id integer primary key autoincrement, "
            + "fname text not null,lname text not null,email text not null,company text not null,city text not null,pincode text not null,mobileno text not null);";

    private final Context context;

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, oldVersion + " to " + newVersion
                    + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS contacts");
            onCreate(db);
        }
    }

    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        DBHelper.close();
    }

    public long insertContact(String fname,String lname,String email,String company,String city,String pincode,String mobilno) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_FNAME, fname);
        initialValues.put(KEY_LNAME, lname);
        initialValues.put(KEY_EMAIL, email);
        initialValues.put(KEY_COMPANY, company);
        initialValues.put(KEY_CITY, city);
        initialValues.put(KEY_PINCODE, pincode);
        initialValues.put(KEY_MOBILENO, mobilno);

        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean deleteContact(long rowId) {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public Cursor getAllContacts() {
        return db.query(DATABASE_TABLE, null, null, null, null, null, null);
    }

    public Cursor getContact(long rowId) throws SQLException {
        Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {
                        KEY_ROWID, KEY_FNAME, KEY_EMAIL }, KEY_ROWID + "=" + rowId,
                null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean updateContact(long rowId, String name, String email) {
        ContentValues args = new ContentValues();
        args.put(KEY_FNAME, name);
        args.put(KEY_EMAIL, email);
        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}
