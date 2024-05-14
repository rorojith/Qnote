package com.vikesh.projectcontroller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    private var studentList: ArrayList<StudentModel> = ArrayList()
    private var onClickItem: ((StudentModel) -> Unit)? = null
    private var onClickDeleteItem: ((StudentModel) -> Unit)? = null

    fun addItems(items: ArrayList<StudentModel>) {
        this.studentList = items
        notifyDataSetChanged()
    }

    fun setOnClickItem(callback: (StudentModel) -> Unit) {
        this.onClickItem = callback
    }

    fun setOnClickDeleteItem(callback: (StudentModel) -> Unit) {
        this.onClickDeleteItem = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_item_std, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = studentList[position]
        holder.bindView(student)
        holder.itemView.setOnClickListener { onClickItem?.invoke(student) }
        holder.btnDelete.setOnClickListener { onClickDeleteItem?.invoke(student) }
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvId: TextView = itemView.findViewById(R.id.tvId)
        private val tvName: TextView = itemView.findViewById(R.id.tvName)
        private val tvTask: TextView = itemView.findViewById(R.id.tvTask)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)

        fun bindView(student: StudentModel) {
            tvId.text = student.id.toString()
            tvName.text = student.name
            tvTask.text = student.task
        }
    }
}
