package database

class TodoDbSchema {
    object TodoTable {
        const val NAME = "todos"

        object Cols {
            const val UUID = "uuid"
            const val TITLE = "title"
            const val DETAIL = "detail"
            const val DATE = "date"
            const val IS_COMPLETE = "isComplete"
        }
    }
}