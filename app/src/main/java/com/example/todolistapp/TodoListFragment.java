package com.example.todolistapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class TodoListFragment extends Fragment {

    private RecyclerView mTodoRecyclerView;
    TodoAdapter mTodoAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_todo_list, container, false);

        mTodoRecyclerView = (RecyclerView) view.findViewById(R.id.todo_recycler_view);
        mTodoRecyclerView.setLayoutManager( new LinearLayoutManager(getActivity()) );

        updateUI();

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }


    private void updateUI(){
/*
        ArrayList todoList = new ArrayList<>();
        TodoModel todoModel = TodoModel.get(getContext());
        todoList = todoModel.getTodoList();
 */
        TodoModel todoModel = TodoModel.get(getActivity());
        List<Todo> todoList = todoModel.getTodoList();


        if (mTodoAdapter == null) {
            mTodoAdapter = new TodoAdapter(todoList);
            mTodoRecyclerView.setAdapter(mTodoAdapter);
        } else {
            mTodoAdapter.setTodoList(todoList);
            mTodoAdapter.notifyDataSetChanged();
        }

    }

    public class TodoHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Todo mTodo;
        private TextView mTextViewTitle;
        private TextView mTextViewDate;

        public TodoHolder(LayoutInflater inflater, ViewGroup parent) {

            super(inflater.inflate(R.layout.list_item_todo, parent, false));

            itemView.setOnClickListener(this);

            mTextViewTitle = (TextView) itemView.findViewById(R.id.todo_title);
            mTextViewDate = (TextView) itemView.findViewById(R.id.todo_date);

        }

        @Override
        public void onClick(View view) {
            // have a Toast for now
            Toast.makeText(
                    getActivity(),
                    mTodo.getTitle() + " clicked",
                    Toast.LENGTH_SHORT)
                    .show();

            Intent intent = TodoActivity.newIntent(getActivity(), mTodo.getId());
            startActivity(intent);

        }

        public void bind(Todo todo){
            mTodo = todo;
            mTextViewTitle.setText(mTodo.getTitle());
            mTextViewDate.setText(mTodo.getDate().toString());
        }

    }

    public class TodoAdapter extends RecyclerView.Adapter<TodoListFragment.TodoHolder> {

        private List<Todo> mTodoList;

        public TodoAdapter(List<Todo> todos) {
            mTodoList = todos;
        }

        @NonNull
        @Override
        public TodoListFragment.TodoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new TodoHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(TodoHolder holder, int position) {
            Todo todo = mTodoList.get(position);
            holder.bind(todo);
        }

        @Override
        public int getItemCount() {
            return mTodoList.size();
        }

        public void setTodoList(List<Todo> todoList) {
            mTodoList = todoList;
        }
    }
}
