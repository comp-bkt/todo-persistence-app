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
    private val mDatabase:SQLiteDatabase

    init {
        mDatabase = TodoBaseHelper(context!!.applicationContext)
            .writableDatabase

        // uncomment block for test data into the database
        for (i in 0..29) {
            val todo = Todo()
            todo.title = "Todo title $i"
            todo.detail = "Detail for task " + todo.id.toString()
            todo.isComplete = false
            addTodo(todo)
        }
    }

    fun updateTodo(todo: Todo?) {
        val uuidString = todo!!.id.toString()
        val values = getContentValues(todo)

        /* stop sql injection, pass uuidString to new String
        so, it is treated as string rather than code */
        mDatabase.update(
            TodoDbSchema.TodoTable.NAME, values,
            TodoDbSchema.TodoTable.Cols.UUID + " = ?", arrayOf(uuidString)
        )
    }

    private fun queryTodoList(whereClause: String?, whereArgs: Array<String>?): TodoCursorWrapper {
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
    }

    /* return mTodoList;
        return new ArrayList<>(); */
    val todoList: List<Todo>
        get() {
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
            TodoDbSchema.TodoTable.Cols.UUID + " = ?", arrayOf(id.toString())
        ).use { cursor ->
            if (cursor.count == 0) {
                return null
            }
            cursor.moveToFirst()
            return cursor.todo
        }
    }

    private fun addTodo(todo: Todo) {
        val values = getContentValues(todo)
        mDatabase.insert(TodoDbSchema.TodoTable.NAME, null, values)
    }

    fun getPhotoFile(todo: Todo?, context: Context?): File {
        val filesDir = context!!.filesDir
        return File(filesDir, todo!!.photoFilename)
    }

    companion object {

        private var sTodoModel: TodoModel? = null

        operator fun get(context: Context?): TodoModel? {
            if (sTodoModel == null) {
                sTodoModel = TodoModel(context!!.applicationContext)
            }
            return sTodoModel
        }

        private fun getContentValues(todo: Todo?): ContentValues {
            val values = ContentValues()
            values.put(TodoDbSchema.TodoTable.Cols.UUID, todo!!.id.toString())
            values.put(TodoDbSchema.TodoTable.Cols.TITLE, todo.title)
            values.put(TodoDbSchema.TodoTable.Cols.DETAIL, todo.detail)
            values.put(TodoDbSchema.TodoTable.Cols.DATE, todo.date.time)
            values.put(
                TodoDbSchema.TodoTable.Cols.IS_COMPLETE,
                if (todo.isComplete == true) true else false
            )
            return values
        }
    }
}