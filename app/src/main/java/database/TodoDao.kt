package database

import androidx.room.*
import java.util.*


@Dao
interface TodoDao {
    @Query("SELECT * FROM todo")
    fun getAll(): List<Todo>

    @Query("SELECT * FROM todo WHERE UUID=:id")
    fun getTodoFor(id: String) : Todo

    @Insert
    fun insert(todo:Todo)

    @Update
    fun updateTodo(todo: Todo)

    @Delete
    fun delete(todo: Todo)
}