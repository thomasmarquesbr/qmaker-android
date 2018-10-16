package com.qmakercorp.qmaker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.qmakercorp.qmaker.R
import com.qmakercorp.qmaker.data.model.Answer
import kotlinx.android.synthetic.main.answers_item.view.*

class AnswersAdapter(private val context: Context,
                     var answers: MutableList<Answer>):
        Adapter<AnswersAdapter.AnswersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswersViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.answers_item, parent, false)
        return AnswersViewHolder(view)
    }

    override fun getItemCount(): Int {
        return answers.count()
    }

    override fun onBindViewHolder(holder: AnswersViewHolder, position: Int) {
        val answer = answers[position]
        holder.bindView(answer)
    }

    fun removeAt(position: Int) {
        answers.removeAt(position)
        notifyItemRemoved(position)
    }


    class AnswersViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

//        val description = itemView.mainText

        fun bindView(answer: Answer) {
//            title.text = question.order.toString()
            itemView.mainText.text = answer.description
        }

    }

}