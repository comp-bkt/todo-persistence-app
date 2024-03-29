package com.example.todolistapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.todolistapp.database.Todo
import kotlinx.coroutines.launch

class TodoListUIViewModel(application:Application) : AndroidViewModel(application) {
    private val mRepository: TodoRepository = TodoRepository.get(application)!!

    // Using LiveData and caching what getTodos returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel
    var todos: LiveData<List<Todo>>? = null

    init {
        todos = mRepository.todoList
        if(todos?.value?.isEmpty() == true) {
            // uncomment block for test data into the database
            for (i in 0..29) {
                val todo = Todo()
                todo.title = "Todo title $i"
                todo.detail = "Detail for task " + todo.id.toString()
                todo.isComplete = false
                insert(todo)
            }
        }
    }

    fun insert(todo: Todo) {
        viewModelScope.launch {
            mRepository.insert(todo)
        }
    }

}