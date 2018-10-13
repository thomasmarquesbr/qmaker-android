package com.qmakercorp.qmaker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.qmakercorp.qmaker.R
import com.qmakercorp.qmaker.data.model.Quiz
import kotlinx.android.synthetic.main.quizzes_item.view.*

class QuizzesListAdapter(private val context: Context,
                         var quizzes: MutableList<Quiz>):
        Adapter<QuizzesListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.quizzes_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return quizzes.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val quiz = quizzes[position]
        holder.bindView(quiz)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bindView(quiz: Quiz) {
            val title = itemView.item_title
            val description = itemView.item_description

            title.text = quiz.name
            description.text = "${quiz.questions.size} perguntas"
        }

    }

}

