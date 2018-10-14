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

    fun removeAt(position: Int) {
        quizzes.removeAt(position)
        notifyItemRemoved(position)
    }

    fun restoreItem(quiz: Quiz, position: Int) {
        quizzes.add(position, quiz)
        notifyItemInserted(position)
    }

    private fun getLayoutInflater(): LayoutInflater {
        return LayoutInflater.from(context)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bindView(quiz: Quiz) {
//            val title = itemView.item_title
//            val description = itemView.item_description

            val title = itemView.mainText
            val description = itemView.subText

            title.text = quiz.name
            description.text = "${quiz.questions.size} perguntas"
        }

    }

}

