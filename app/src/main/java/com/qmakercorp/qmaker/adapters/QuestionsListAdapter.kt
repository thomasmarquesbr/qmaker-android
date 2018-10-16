package com.qmakercorp.qmaker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.qmakercorp.qmaker.R
import com.qmakercorp.qmaker.data.model.Question
import kotlinx.android.synthetic.main.questions_item.view.*


class QuestionsListAdapter(private val context: Context,
                           var questions: MutableList<Question>):
        Adapter<QuestionsListAdapter.QuestionsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionsViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.questions_item, parent, false)
        return QuestionsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return questions.count()
    }

    override fun onBindViewHolder(holder: QuestionsViewHolder, position: Int) {
        val question = questions[position]
        holder.bindView(question)
    }

    fun removeAt(position: Int) {
        questions.removeAt(position)
        notifyItemRemoved(position)
    }

    class QuestionsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

//        private val title = itemView.mainText
//        private val description = itemView.mainText

        fun bindView(question: Question) {
//            title.text = question.order.toString()
            itemView.mainText.text = question.description
        }

    }

}

