package com.qmakercorp.qmaker.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.qmakercorp.qmaker.R
import com.qmakercorp.qmaker.components.ItemTouchHelperAdapter
import com.qmakercorp.qmaker.components.OnStartDragListener
import com.qmakercorp.qmaker.data.model.Question
import kotlinx.android.synthetic.main.questions_item.view.*
import java.util.*


class QuestionsListAdapter(private val context: Context,
                           var questions: MutableList<Question>,
                           private val mDragStartListener: OnStartDragListener,
                           val onclick: (Int) -> Unit,
                           val onRemoved: (Int) -> Unit):
        Adapter<QuestionsListAdapter.QuestionsViewHolder>(), ItemTouchHelperAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionsViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.questions_item, parent, false)
        return QuestionsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return questions.count()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: QuestionsViewHolder, position: Int) {
        val question = questions[position]
        holder.bindView(question, position)
        holder.handleView.setOnTouchListener { v, event ->
            if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                mDragStartListener.onStartDrag(holder)
            }
            false
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        Collections.swap(questions, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onItemDismiss(position: Int) {
        onRemoved(position)
        questions.removeAt(position)
        notifyItemRemoved(position)
    }

    fun removeAt(position: Int) {
        questions.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class QuestionsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

//        private val title = itemView.mainText
//        private val description = itemView.mainText

        val handleView: ImageView = itemView.handle

        fun bindView(question: Question, position: Int) {
//            title.text = question.order.toString()
            itemView.mainText.text = question.description
            itemView.rowFG.setOnClickListener { onclick(position) }
        }

    }

}

