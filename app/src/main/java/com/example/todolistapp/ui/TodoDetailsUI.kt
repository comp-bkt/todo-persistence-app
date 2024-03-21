package com.example.todolistapp.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.todolistapp.R
import com.example.todolistapp.database.Todo
import com.example.todolistapp.ui.theme.TodoListAppTheme
import java.util.UUID

@Composable
fun TodoDetails(id: UUID) {
    val context = LocalContext.current
    val todoModel: TodoViewModel = viewModel()

    val todo = todoModel.getTodo(id)?.observeAsState(null)

    val input:MutableState<String> = if(todo?.value!=null) {
        rememberSaveable {
            mutableStateOf(
                todo.value?.title.toString()
                )
            }
        }
        else {
        rememberSaveable {
            mutableStateOf(
            " "
            )
        }
    }

    BackHandler {
        todoModel.updateTodo(todo!!.value!!.copy(title = input.value.toString()));
        (context as Activity).finish()
    }

    Column {
        Row() {
            CameraTaker(todo = todo?.value,
                context = context,
                todoModel
            )
            Column() {
                Text(
                    text = stringResource(id = R.string.todo_title_label),
                    modifier = Modifier.padding(8.dp)
                )
                TextField (
                    value = input.value,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = { t ->
                        input.value = t
                        todo!!.value!!.title = t
                    }
                )
            }
        }
        Spacer(modifier = Modifier.size(8.dp))
        Divider(
            thickness = 2.dp,
            color = Color.Blue
        )
        TodoDetailSection(todo = todo?.value,
            onIsCompleteChanged = {
                todoModel.updateTodo(todo?.value!!.copy(isComplete = it))
            }
        )
    }
}

@Composable
fun CameraTaker(
    todo: Todo?,
    context: Context,
    todoModel: TodoViewModel,
    ) {
    val pictureLoaded = rememberSaveable {mutableStateOf(false)}

    val uri = if(todo?.id!=null) {
        todoModel.updateCapturedImageUri(todo!!, context)
        pictureLoaded.value = true
        remember { mutableStateOf (FileProvider.getUriForFile(
            context,
            "com.example.todolistapp.fileprovider", todoModel.getPhotoFile(todo, context))
            )
        }
    }
    else {
       rememberSaveable { mutableStateOf (Uri.EMPTY) }
    }

    val todoTakePicture =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {result->
            if (result) {
                context.revokeUriPermission(uri.value, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                todoModel.updatePictureTaken(true)
                if (todo != null) {
                    todoModel.updateCapturedImageUri(todo, context)
                }
            }
        }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {isGranted->
        if (isGranted) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            todoModel.updatePictureTaken(false)
            todoTakePicture.launch(uri.value)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    val modifier = Modifier
        .size(150.dp)
        .border(BorderStroke(1.dp, Color.Black))
        .background(Color.Yellow)


    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if(todoModel.pictureTaken) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current).
                                            data(todoModel.capturedImageUri).build(),
                contentDescription = "image",
                contentScale = ContentScale.FillBounds,
                modifier = modifier,
                onSuccess = {
                    Toast.makeText(context, "Image Refreshed", Toast.LENGTH_SHORT).show()
                }
            )
        }
        else{
            Image(
                rememberAsyncImagePainter(model = todoModel.capturedImageUri),
                contentDescription = "image",
                contentScale = ContentScale.FillBounds,
                modifier = modifier
            )
        }
        Button(onClick = {
            val permissionCheckResult =
                ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                todoModel.updatePictureTaken(false)
                todoTakePicture.launch(uri.value)
            } else {
                // Request a permission
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        },
            modifier = Modifier.fillMaxWidth(0.5f)){
            Image(painter = painterResource(id = android.R.drawable.ic_menu_camera),
                contentDescription = "Camera Button" )
        }
    }
}

@Composable
fun TodoDetailSection(
    todo: Todo?,
    onIsCompleteChanged: (Boolean) -> Unit = {},
) {
    val checkedState by remember { mutableStateOf(false) }
    Column () {
        Text(
            text = stringResource(id = R.string.todo_detail_label)
        )
        Button(onClick = { /*TODO*/ },
            modifier = Modifier.fillMaxWidth(),
            shape = RectangleShape,
        ) {
            Text(
                text = todo?.date.toString(),
                modifier = Modifier.padding(8.dp)
            )
        }
        Checkbox(
            checked = todo?.isComplete ?: checkedState,
            onCheckedChange = onIsCompleteChanged
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Todo Detail Section"
)
@Composable
fun TodoDetailSectionPreview() {
    TodoListAppTheme {
        TodoDetailSection(Todo(title="Hello World"))
    }
}
