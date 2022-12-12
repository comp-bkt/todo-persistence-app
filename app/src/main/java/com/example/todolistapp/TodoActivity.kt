package com.example.todolistapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.todolistapp.TodoActivity
import java.util.*

class TodoActivity : AppCompatActivity() {
    /*
    To decouple the fragment and make it reusable, the TodoFragment has a newInstance method
    that receives a todoId and returns the fragment
     */
    protected fun createFragment(): Fragment {
        val todoId = intent.getSerializableExtra(EXTRA_TODO_ID) as UUID?
        return TodoFragment.newInstance(todoId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)
        val fm = supportFragmentManager
        val fragment = fm.findFragmentById(R.id.fragment_container)
        if (fragment == null) {
            val todoFragment = createFragment()
            fm.beginTransaction()
                    .add(R.id.fragment_container, todoFragment)
                    .commit()
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