package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import database.TodoDbSchema.TodoTable;

public class TodoBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "todo.db";

    public TodoBaseHelper( Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TodoTable.NAME + "(" +
                        TodoTable.Cols.UUID + " TEXT, " +
                        TodoTable.Cols.TITLE + " TEXT, " +
                        TodoTable.Cols.DETAIL + " TEXT, " +
                        TodoTable.Cols.DATE + " DATE, " +
                        TodoTable.Cols.IS_COMPLETE + " INTEGER)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
