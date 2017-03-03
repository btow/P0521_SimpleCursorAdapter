package com.example.samsung.p0521_simplecursoradapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by samsung on 02.03.2017.
 */

public class DB {
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "mydb", DB_TABLE = "mytab",
            COLUMN_ID = "_id", COLUMN_IMG = "img", COLUMN_TXT = "txt";
    private static final String
            DB_CREATE = "create table " + DB_TABLE + "("
                    + COLUMN_ID + " integer primary key autoincrement, "
                    + COLUMN_IMG + " integer, " + COLUMN_TXT + " text);";
    private final Context mCtx;
    private DBHelper mDBHelper;
    private SQLiteDatabase mDb;

    public DB (Context ctx) {
        mCtx = ctx;
    }

    //метод открытия подключения
    public void openDb() {
        mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
        mDb = mDBHelper.getWritableDatabase();
    }

    //метод для закрыьия подключения
    public void closeDb() {
        if (mDBHelper != null) mDBHelper.close();
    }

    //метод для получения всех данных из таблицы DB_TABLE
    public Cursor getAllData() {
        return mDb.query(DB_TABLE, null, null, null, null, null, null);
    }

    //Метод для добавления записи в таблицу DB_TABLE
    public void addRec(String txt, int img) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TXT, txt);
        contentValues.put(COLUMN_IMG, img);
        mDb.beginTransaction();
        try {
            mDb.insert(DB_TABLE, null, contentValues);
            mDb.setTransactionSuccessful();
        } finally {
            mDb.endTransaction();
        }
    }

    //Метод для удаления записи из таблицы DB_TABLE
    public void delRec(long id) {
        mDb.beginTransaction();
        try {
            mDb.delete(DB_TABLE, COLUMN_ID + " = " + id, null);
            mDb.setTransactionSuccessful();
        } finally {
            mDb.endTransaction();
        }
    }

    //клас для создания и управления БД
    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name,
                        SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        //метод для создания и заполнения БД
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.beginTransaction();
            try {
                db.execSQL(DB_CREATE);

                ContentValues contentValues = new ContentValues();
                for (int index = 1; index < 5; index++) {
                    contentValues.put(COLUMN_TXT, "Sometext " + index);
                    contentValues.put(COLUMN_IMG, R.mipmap.ic_launcher);
                    db.insert(DB_TABLE, null, contentValues);
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
