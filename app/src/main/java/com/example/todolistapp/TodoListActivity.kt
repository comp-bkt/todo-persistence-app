package com.example.todolistapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class TodoListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)
        val fm = supportFragmentManager
        val fragment = fm.findFragmentById(R.id.fragment_container)
        if (fragment == null) {
            val todoListFragment = TodoListFragment()
            fm.beginTransaction()
                    .add(R.id.fragment_container, todoListFragment)
                    .commit()
        }
    }
}