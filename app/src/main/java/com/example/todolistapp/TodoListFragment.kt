package com.example.todolistapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import database.Todo

class TodoListFragment : Fragment() {
    private var mTodoRecyclerView: RecyclerView? = null
    private var mTodoAdapter: TodoAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_todo_list, container, false)
        mTodoRecyclerView = view.findViewById(R.id.todo_recycler_view)
        mTodoRecyclerView!!.layoutManager = LinearLayoutManager(activity)
        updateUI()
        return view
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun updateUI() {
        val todoModel: TodoModel? = TodoModel.get(activity)
        val todoList = todoModel!!.todoList
        if (mTodoAdapter == null) {
            mTodoAdapter = TodoAdapter(todoList)
            mTodoRecyclerView!!.adapter = mTodoAdapter
        } else {
            mTodoAdapter!!.setTodoList(todoList)
            mTodoAdapter!!.notifyDataSetChanged()
        }
    }

    inner class TodoHolder(inflater: LayoutInflater, parent: ViewGroup?) : ViewHolder(inflater.inflate(R.layout.list_item_todo, parent, false)), View.OnClickListener {
        private var mTodo: Todo? = null
        private val mTextViewTitle: TextView
        private val mTextViewDate: TextView

        init {
            itemView.setOnClickListener(this)
            mTextViewTitle = itemView.findViewById(R.id.todo_title)
            mTextViewDate = itemView.findViewById(R.id.todo_date)
        }

        override fun onClick(view: View) {
            // have a Toast for now
            Toast.makeText(
                    activity,
                    mTodo!!.title + " clicked",
                    Toast.LENGTH_SHORT)
                    .show()
            val intent: Intent = TodoActivity.Companion.newIntent(activity, mTodo!!.id)
            startActivity(intent)
        }

        fun bind(todo: Todo?) {
            mTodo = todo
            mTextViewTitle.text = mTodo!!.title
            mTextViewDate.text = mTodo!!.date.toString()
        }
    }

    inner class TodoAdapter(private var mTodoList: List<Todo?>?) : RecyclerView.Adapter<TodoHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoHolder {
            val layoutInflater = LayoutInflater.from(activity)
            return TodoHolder(layoutInflater, parent)
        }

        override fun onBindViewHolder(holder: TodoHolder, position: Int) {
            val todo = mTodoList!![position]
            holder.bind(todo)
        }

        override fun getItemCount(): Int {
            return mTodoList!!.size
        }

        fun setTodoList(todoList: List<Todo?>?) {
            mTodoList = todoList
        }
    }
}