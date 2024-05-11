package com.example.task

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TaskDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "todoapp.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "alltasks"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_PRIORITY = "priority"
        private const val COLUMN_DEADLINE = "deadline"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME(" +
                "$COLUMN_ID INTEGER PRIMARY KEY," +
                "$COLUMN_TITLE TEXT," +
                "$COLUMN_DESCRIPTION TEXT," +
                "$COLUMN_PRIORITY TEXT," +
                "$COLUMN_DEADLINE TEXT" +
                ")"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun insertTask(task: Task) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, task.title)
            put(COLUMN_DESCRIPTION, task.description)
            put(COLUMN_PRIORITY, task.priority)
            put(COLUMN_DEADLINE, task.deadline)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllTasks(): List<Task> {
        val taskList = mutableListOf<Task>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
            val priority = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRIORITY))
            val deadline = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DEADLINE))

            val task = Task(id, title, description, priority, deadline)
            taskList.add(task)
        }

        cursor.close()
        db.close()
        return taskList
    }

    fun updateTask(task: Task) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, task.title)
            put(COLUMN_DESCRIPTION, task.description)
            put(COLUMN_PRIORITY, task.priority)
            put(COLUMN_DEADLINE, task.deadline)
        }
        val whereClause = "$COLUMN_ID=?"
        val whereArgs = arrayOf(task.id.toString())
        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }

    fun getTaskById(taskId: Int): Task {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID=$taskId"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
        val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
        val priority = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRIORITY))
        val deadline = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DEADLINE))

        cursor.close()
        db.close()
        return Task(id, title, description, priority, deadline)
    }

    fun deleteTask(taskId: Int) {
        val db = writableDatabase
        val whereClause = "$COLUMN_ID=?"
        val whereArgs = arrayOf(taskId.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }
}
