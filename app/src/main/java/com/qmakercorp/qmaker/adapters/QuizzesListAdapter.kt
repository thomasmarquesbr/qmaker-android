package com.qmakercorp.qmaker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.qmakercorp.qmaker.R
import com.qmakercorp.qmaker.data.model.Quiz
import kotlinx.android.synthetic.main.quizzes_item.view.*


class QuizzesListAdapter(private val context: Context,
                         var quizzes: MutableList<Quiz>):
        Adapter<QuizzesListAdapter.QuizzesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizzesViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.quizzes_item, parent, false)
        return QuizzesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return quizzes.count()
    }

    override fun onBindViewHolder(holder: QuizzesViewHolder, position: Int) {
        val quiz = quizzes[position]
        holder.bindView(quiz)
        holder.isAvailable = holder.hasDescription()
    }

    fun removeAt(position: Int) {
        quizzes.removeAt(position)
        notifyItemRemoved(position)
    }

    class QuizzesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val title = itemView.mainText
        private val description = itemView.subText
        var isAvailable: Boolean = true
            set(value) {
                if (value) {
                    itemView.publish.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.swipeoption_green))
                    itemView.iv_publish.setBackgroundResource(R.drawable.checkbox_marked_circle_outline)
                    itemView.tv_publish.text = itemView.context.getString(R.string.available)
                    itemView.rowFG.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.white))
                    itemView.button_qrcode.visibility = View.VISIBLE
                } else {
                    itemView.publish.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.gray))
                    itemView.iv_publish.setBackgroundResource(R.drawable.checkbox_blank_circle_outline)
                    itemView.tv_publish.text = itemView.context.getString(R.string.unavailable)
                    itemView.rowFG.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.lightGray))
                    itemView.button_qrcode.visibility = View.GONE
                }
                field = value
            }

        fun bindView(quiz: Quiz) {
            title.text = quiz.name
            description.text = quiz.code
        }

        fun hasDescription(): Boolean {
            return !description.text.isEmpty()
        }

    }

}

