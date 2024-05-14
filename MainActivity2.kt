package com.vikesh.projectcontroller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity2 : AppCompatActivity() {

    private lateinit var edName: EditText
    private lateinit var edTask: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnView: Button
    private lateinit var btnUpdate: Button
    private lateinit var sqLiteHelper: SQLiteHelper

    private lateinit var recyclerView: RecyclerView
    private var adapter: StudentAdapter? = null
    private var selectedStudent: StudentModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        initView()
        initRecyclerView()
        sqLiteHelper = SQLiteHelper(this)

        btnAdd.setOnClickListener { addStudent() }
        btnView.setOnClickListener { getStudents() }
        btnUpdate.setOnClickListener { updateStudent() }

        adapter?.setOnClickItem { student ->
            Toast.makeText(this, student.name, Toast.LENGTH_SHORT).show()
            edName.setText(student.name)
            edTask.setText(student.task)
            selectedStudent = student
        }

        adapter?.setOnClickDeleteItem { student ->
            deleteStudent(student.id)
        }
    }

    private fun getStudents() {
        val stdList = sqLiteHelper.getAllStudents()
        adapter?.addItems(stdList)
    }

    private fun addStudent() {
        val name = edName.text.toString()
        val task = edTask.text.toString()

        if (name.isEmpty() || task.isEmpty()) {
            Toast.makeText(this, "Please enter required fields", Toast.LENGTH_SHORT).show()
        } else {
            val student = StudentModel(name = name, task = task)
            val success = sqLiteHelper.insertStudent(student)

            if (success > -1) {
                Toast.makeText(this, "note Added", Toast.LENGTH_SHORT).show()
                clearEditText()
                getStudents()
            } else {
                Toast.makeText(this, "Failed to add note", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateStudent() {
        val name = edName.text.toString()
        val task = edTask.text.toString()

        if (selectedStudent == null) return

        val updatedStudent = StudentModel(
            id = selectedStudent!!.id,
            name = name,
            task = task
        )

        val success = sqLiteHelper.updateStudent(updatedStudent)

        if (success > -1) {
            clearEditText()
            getStudents()
        } else {
            Toast.makeText(this, "Failed to update note", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteStudent(id: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to delete this note?")
        builder.setCancelable(true)
        builder.setPositiveButton("Yes") { dialog, _ ->
            sqLiteHelper.deleteStudentById(id)
            getStudents()
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()
    }

    private fun clearEditText() {
        edName.setText("")
        edTask.setText("")
        edName.requestFocus()
    }

    private fun initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = StudentAdapter()
        recyclerView.adapter = adapter
    }

    private fun initView() {
        edName = findViewById(R.id.edName)
        edTask = findViewById(R.id.edTask)
        btnAdd = findViewById(R.id.btnAdd)
        btnView = findViewById(R.id.btnView)
        btnUpdate = findViewById(R.id.btnUpdate)
    }
}
