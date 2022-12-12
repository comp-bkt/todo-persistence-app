package database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import database.TodoDbSchema.TodoTable
import database.TodoDbSchema.TodoTable.Cols

class TodoBaseHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table " + TodoTable.NAME + "(" +
                Cols.UUID + " TEXT, " +
                Cols.TITLE + " TEXT, " +
                Cols.DETAIL + " TEXT, " +
                Cols.DATE + " DATE, " +
                Cols.IS_COMPLETE + " BOOLEAN)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    companion object {
        private const val VERSION = 1
        private const val DATABASE_NAME = "todo.db"
    }
}