package com.example.todolistapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import database.*
import java.io.File
import java.util.*

internal class TodoModel private constructor(context: Context?) {

    private val mDatabase:AppDatabase

    init {
        mDatabase = AppDatabase.getInstance(context!!)

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
        mDatabase.todoDao().updateTodo(todo!!)
    }

    /* return mTodoList;
        return new ArrayList<>(); */
    val todoList: List<Todo>
        get() {
            return mDatabase.todoDao().getAll()
        }

    fun getTodo(id: UUID?): Todo? {
        return mDatabase.todoDao().getTodoFor(id!!.toString())
    }

    private fun addTodo(todo: Todo) {
        mDatabase.todoDao().insert(todo)
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
    }
}