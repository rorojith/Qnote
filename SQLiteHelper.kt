package com.vikesh.projectcontroller

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "notes.db"
        private const val TABLE_NOTES = "tbl_note"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_TASK = "task"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableStudent = ("CREATE TABLE $TABLE_NOTES (" +
                "$COLUMN_ID INTEGER PRIMARY KEY," +
                "$COLUMN_NAME TEXT," +
                "$COLUMN_TASK TEXT)")
        db.execSQL(createTableStudent)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NOTES")
        onCreate(db)
    }

    fun insertStudent(student: StudentModel): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, student.name)
        values.put(COLUMN_TASK, student.task)
        val success = db.insert(TABLE_NOTES, null, values)
        db.close()
        return success
    }

    fun getAllStudents(): ArrayList<StudentModel> {
        val studentList = ArrayList<StudentModel>()
        val selectQuery = "SELECT * FROM $TABLE_NOTES"
        val db = this.readableDatabase
        val cursor: Cursor?
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var id: Int
        var name: String
        var task: String
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                task = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK))
                val student = StudentModel(id, name, task)
                studentList.add(student)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return studentList
    }

    fun updateStudent(student: StudentModel): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, student.name)
        values.put(COLUMN_TASK, student.task)
        val success = db.update(TABLE_NOTES, values, "$COLUMN_ID=?", arrayOf(student.id.toString()))
        db.close()
        return success
    }

    fun deleteStudentById(id: Int): Int {
        val db = this.writableDatabase
        val success = db.delete(TABLE_NOTES, "$COLUMN_ID=?", arrayOf(id.toString()))
        db.close()
        return success
    }
}
