package database

import android.database.Cursor
import android.database.CursorWrapper
import com.example.todolistapp.Todo
import database.TodoDbSchema.TodoTable.Cols
import java.util.*

class TodoCursorWrapper(cursor: Cursor?) : CursorWrapper(cursor) {
    val todo: Todo
        get() {
            val uuidString = getString(getColumnIndex(Cols.UUID))
            val title = getString(getColumnIndex(Cols.TITLE))
            val detail = getString(getColumnIndex(Cols.DETAIL))
            val date = getLong(getColumnIndex(Cols.DATE))
            val isComplete = getInt(getColumnIndex(Cols.IS_COMPLETE))
            val todo = Todo(UUID.fromString(uuidString))
            todo.title = title
            todo.detail = detail
            todo.date = Date(date)
            todo.isComplete = if (isComplete == 1) true else false
            return todo
        }
}