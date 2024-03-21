package com.example.todolistapp.ui

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.todolistapp.database.AppDatabase
import com.example.todolistapp.database.Todo
import com.example.todolistapp.database.TodoDao
import java.util.UUID

public class TodoRepository private constructor (application: Application)  {

    private val mTodoDao: TodoDao?
    val todoList:  LiveData<List<Todo>>?

    init {
        val mDatabase: AppDatabase = AppDatabase.getInstance(application)
        //mDatabase = AppDatabase.getInstance(context!!)
        mTodoDao = mDatabase.todoDao()
        todoList = getAll()
    }

    suspend fun updateTodo(todo: Todo?) {
        mTodoDao!!.updateTodo(todo!!)
    }

    /* return mTodoList;
        return new ArrayList<>(); */
    fun getAll() : LiveData<List<Todo>> {
        //todoList = mTodoDao.getAll()
        return mTodoDao!!.getAll()
    }


    fun getTodo(id: UUID?): LiveData<Todo> {
        return mTodoDao!!.getTodoFor(id!!.toString())
    }

    suspend fun insert(todo: Todo) {
        mTodoDao!!.insert(todo)
    }


    companion object {

        private var sTodoModel: TodoRepository? = null

        operator fun get(application: Application): TodoRepository? {
            if (sTodoModel == null) {
                sTodoModel = TodoRepository(application = application)
            }
            return sTodoModel
        }
    }
}