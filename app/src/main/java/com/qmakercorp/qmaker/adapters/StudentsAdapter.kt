package com.qmakercorp.qmaker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView
import com.qmakercorp.qmaker.R
import com.qmakercorp.qmaker.data.model.Student
import kotlinx.android.synthetic.main.students_item.view.*
import java.text.DecimalFormat

class StudentsAdapter(private val context: Context,
                      val students: MutableList<Student>) : Adapter<StudentsAdapter.StudentsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentsViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.students_item, parent, false)
        return StudentsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return students.count()
    }

    override fun onBindViewHolder(holder: StudentsViewHolder, position: Int) {
        val student = students[position]
        holder.bindView(student)
    }

    fun removeAt(position: Int) {
        students.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class StudentsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bindView(student: Student) {
            itemView.mainText.text = student.name
            itemView.subText.text = student.classroom
            val format = DecimalFormat("0.#")
            itemView.tv_result.text = "${format.format(student.media)}%"
            itemView.tv_result.textColors
            if (student.media >= 60.0)
                itemView.tv_result.setTextColor(ContextCompat.getColor(context, R.color.swipeoption_green))
            else
                itemView.tv_result.setTextColor(ContextCompat.getColor(context, R.color.swipeoption_purple))
        }

    }
}