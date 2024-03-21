package com.example.todolistapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolistapp.R
import com.example.todolistapp.TodoActivity
import com.example.todolistapp.database.Todo
import com.example.todolistapp.ui.theme.TodoListAppTheme


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoList(
    todoViewModel: TodoViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val todoList =  todoViewModel.todos!!.observeAsState(emptyList())
    val scrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                },
                actions = {
                    IconButton(onClick = {
                        val todo = Todo(title ="New Entry", detail = "New Detail")
                        todoViewModel.insert(todo)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Localized description"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding, modifier = modifier
        ) {
            items(todoList.value) {todo->
                TodoItem(todo)
            }
        }
    }
}

@Composable
fun TodoItem(todo: Todo) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.clickable {
            val intent: Intent = TodoActivity.newIntent(context, todo.id)
            context.startActivity(intent)
        }
    ) {
        Text(
            text = todo.title!!,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = todo.date.toString(),
            modifier = Modifier.padding(8.dp)
        )
        Divider(
            thickness = 2.dp,
            color = Color.Blue
        )
    }
}
@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Todo Card"
)
@Composable
fun TodoCardPreview() {
    TodoListAppTheme {
        TodoItem(Todo(title="Hello World"))
    }
}