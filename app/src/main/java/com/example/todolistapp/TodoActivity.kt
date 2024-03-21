package com.example.todolistapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.todolistapp.ui.TodoDetails
import com.example.todolistapp.ui.theme.TodoListAppTheme
import java.util.UUID

class TodoActivity : ComponentActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val todoId = intent.getSerializableExtra(EXTRA_TODO_ID, UUID::class.java)
        setContent {
            TodoListAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TodoDetails(todoId!!)
                }
            }
        }
    }

    companion object {
        const val EXTRA_TODO_ID = "todo_id"
        fun newIntent(packageContext: Context?, todoId: UUID?): Intent {
            val intent = Intent(packageContext, TodoActivity::class.java)
            intent.putExtra(EXTRA_TODO_ID, todoId)
            return intent
        }
    }
}