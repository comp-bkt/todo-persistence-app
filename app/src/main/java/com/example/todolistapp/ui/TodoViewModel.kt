package com.example.todolistapp.ui

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.todolistapp.database.Todo
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID

class TodoViewModel(application:Application) : AndroidViewModel(application) {
    private val mRepository: TodoRepository = TodoRepository.get(application)!!

    // Using LiveData and caching what getTodos returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel
    var todos: LiveData<List<Todo>>? = null
    var todo: LiveData<Todo>? = null
    var capturedImageUri by mutableStateOf<Uri>(Uri.EMPTY)
        private set

    var pictureTaken by mutableStateOf<Boolean>(false)
        private set

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

    fun getTodo(id: UUID?) : LiveData<Todo>?{
        return mRepository.getTodo(id)
    }

    fun getPhotoFile(todo: Todo?, context: Context?): File {
        val filesDir = context!!.filesDir
        return File(filesDir, todo!!.photoFilename)
    }

    fun updateTodo(todo: Todo?) {
        viewModelScope.launch {
            mRepository.updateTodo(todo!!)
        }
    }

    fun updateCapturedImageUri(todo:Todo, context:Context){
        capturedImageUri = Uri.fromFile(getPhotoFile(todo, context))
    }

    fun updatePictureTaken(taken:Boolean){
        pictureTaken = taken
    }

}