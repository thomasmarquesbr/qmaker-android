package com.qmakercorp.qmaker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.qmakercorp.qmaker.R
import com.qmakercorp.qmaker.data.model.QuizResult
import kotlinx.android.synthetic.main.quizzes_result_item.view.*
import java.text.DecimalFormat


class QuizzesResultAdapter(private val context: Context,
                           var quizResultList: MutableList<QuizResult>,
                           val onclick: (Int) -> Unit): RecyclerView.Adapter<QuizzesResultAdapter.QuizzesResultViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizzesResultViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.quizzes_result_item, parent, false)
        return QuizzesResultViewHolder(view)
    }

    override fun getItemCount(): Int {
        return quizResultList.count()
    }

    override fun onBindViewHolder(holder: QuizzesResultViewHolder, position: Int) {
        val quiz = quizResultList[position]
        holder.bindView(quiz, position)
    }

    inner class QuizzesResultViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bindView(quiz: QuizResult, position: Int) {
            itemView.mainText.text = quiz.name
            itemView.subText.text = "${quiz.students.size} ${context.getString(R.string.answered)}"
            val format = DecimalFormat("0.#")
            itemView.tv_result.text = "${format.format(quiz.resultValue)}%"
            itemView.ll_row.setOnClickListener { onclick(position) }
            itemView.tv_result.textColors
            if (quiz.resultValue >= 60.0)
                itemView.tv_result.setTextColor(ContextCompat.getColor(context, R.color.swipeoption_green))
            else
                itemView.tv_result.setTextColor(ContextCompat.getColor(context, R.color.swipeoption_purple))
        }

    }

}

