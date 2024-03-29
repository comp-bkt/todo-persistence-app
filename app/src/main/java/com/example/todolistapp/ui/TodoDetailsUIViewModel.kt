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

class TodoDetailsUIViewModel(application:Application) : AndroidViewModel(application) {
    private val mRepository: TodoRepository = TodoRepository.get(application)!!

    var todo: LiveData<Todo>? = null
    var capturedImageUri by mutableStateOf<Uri>(Uri.EMPTY)
        private set

    var pictureTaken by mutableStateOf<Boolean>(false)
        private set

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