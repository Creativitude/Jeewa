package com.creativitude.jeewa.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by naveen on 27/05/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {


    private String CREATE_TABLE;
    private String TABLE_NAME;

    public DatabaseHelper(Context context, String db_name, String create_table, String table_name) {
        super(context, db_name, null, 1);
        this.CREATE_TABLE = create_table;
        this.TABLE_NAME = table_name;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(this.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(sqLiteDatabase);
    }

    public long createUser(String email, String password) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("email", email);
        values.put("password", password);

        return db.insert(this.TABLE_NAME, null, values);

    }
}
