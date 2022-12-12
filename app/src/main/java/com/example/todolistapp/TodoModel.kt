package com.example.todolistapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import database.TodoBaseHelper
import database.TodoCursorWrapper
import database.TodoDbSchema
import java.io.File
import java.util.*

internal class TodoModel private constructor(context: Context?) {
    //    private ArrayList<Todo> mTodoList;
    private val mDatabase: SQLiteDatabase

    init {
        mContext = context!!.applicationContext
        mDatabase = TodoBaseHelper(mContext)
                .writableDatabase

//        mTodoList = new ArrayList<>();

        // refactor to pattern for data plugins
        // simulate some data for testing

// uncomment block for test data into the database
        for (i in 0..29) {
            val todo = Todo()
            todo.title = "Todo title $i"
            todo.detail = "Detail for task " + todo.id.toString()
            todo.isComplete = false
            addTodo(todo)
            //           mTodoList.add(todo);
        }
    }

    fun updateTodo(todo: Todo?) {
        val uuidString = todo!!.id.toString()
        val values = getContentValues(todo)

        /* stop sql injection, pass uuidString to new String
        so, it is treated as string rather than code */
        mDatabase.update(TodoDbSchema.TodoTable.NAME, values,
                TodoDbSchema.TodoTable.Cols.UUID + " = ?", arrayOf(uuidString))
    }

    private fun queryTodoList(whereClause: String?, whereArgs: Array<String>?): TodoCursorWrapper {
        //   private Cursor queryTodoList(String whereClause, String[] whereArgs) {
        val cursor = mDatabase.query(
                TodoDbSchema.TodoTable.NAME,
                null,  // null for all columns
                whereClause,
                whereArgs,
                null,
                null,
                null
        )
        return TodoCursorWrapper(cursor)
        //      return cursor;
    }

    /* return mTodoList;
        return new ArrayList<>(); */
    val todoList: List<Todo>
        get() {
            /* return mTodoList;
        return new ArrayList<>(); */
            val todoList: MutableList<Todo> = ArrayList()
            queryTodoList(null, null).use { cursor ->
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    todoList.add(cursor.todo)
                    cursor.moveToNext()
                }
            }
            return todoList
        }

    fun getTodo(id: UUID?): Todo? {
        queryTodoList(
                TodoDbSchema.TodoTable.Cols.UUID + " = ?", arrayOf(id.toString())).use { cursor ->
            if (cursor.count == 0) {
                return null
            }
            cursor.moveToFirst()
            return cursor.todo
        }
    }

    private fun addTodo(todo: Todo) {
//        mTodoList.add(todo);
        val values = getContentValues(todo)
        mDatabase.insert(TodoDbSchema.TodoTable.NAME, null, values)
    }

    fun getPhotoFile(todo: Todo?): File {
        val filesDir = mContext.filesDir
        return File(filesDir, todo!!.photoFilename)
    }

    companion object {
        private var sTodoModel: TodoModel? = null
        private lateinit var mContext: Context
        operator fun get(context: Context?): TodoModel? {
            mContext = context!!.applicationContext
            if (sTodoModel == null) {
                sTodoModel = TodoModel(context)
            }
            return sTodoModel
        }

        private fun getContentValues(todo: Todo?): ContentValues {
            val values = ContentValues()
            values.put(TodoDbSchema.TodoTable.Cols.UUID, todo!!.id.toString())
            values.put(TodoDbSchema.TodoTable.Cols.TITLE, todo.title)
            values.put(TodoDbSchema.TodoTable.Cols.DETAIL, todo.detail)
            values.put(TodoDbSchema.TodoTable.Cols.DATE, todo.date.time)
            values.put(TodoDbSchema.TodoTable.Cols.IS_COMPLETE, if (todo.isComplete == true) true else false)
            return values
        }
    }
}