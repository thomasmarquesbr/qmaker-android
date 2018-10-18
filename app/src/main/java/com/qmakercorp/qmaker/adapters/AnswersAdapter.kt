package com.qmakercorp.qmaker.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.qmakercorp.qmaker.R
import com.qmakercorp.qmaker.components.ItemTouchHelperAdapter
import com.qmakercorp.qmaker.components.ItemTouchHelperViewHolder
import com.qmakercorp.qmaker.components.OnStartDragListener
import com.qmakercorp.qmaker.data.model.Answer
import kotlinx.android.synthetic.main.answers_item.view.*
import java.util.*

class AnswersAdapter(private val context: Context,
                     var answers: MutableList<Answer>,
                     private val mDragStartListener: OnStartDragListener,
                     val onclick: (Int) -> Unit):
        Adapter<AnswersAdapter.AnswersViewHolder>(), ItemTouchHelperAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswersViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.answers_item, parent, false)
        return AnswersViewHolder(view)
    }

    override fun getItemCount(): Int {
        return answers.count()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: AnswersViewHolder, position: Int) {
        val answer = answers[position]
        holder.bindView(answer)
        holder.handleView.setOnTouchListener { v, event ->
            if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                mDragStartListener.onStartDrag(holder)
            }
            false
        }
    }

    override fun onItemDismiss(position: Int) {
        answers.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        answers[fromPosition].id = toPosition
        answers[toPosition].id = fromPosition
        Collections.swap(answers, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    fun removeAt(position: Int) {
        answers.removeAt(position)
        notifyItemRemoved(position)
    }


    inner class AnswersViewHolder(itemView: View):
            RecyclerView.ViewHolder(itemView), ItemTouchHelperViewHolder {

//        val description = itemView.mainText
        val handleView: ImageView = itemView.handle

        fun bindView(answer: Answer) {
//            title.text = question.order.toString()
            itemView.mainText.text = answer.description
            if (answer.isTrue) {
                itemView.subText.text = itemView.context.getString(R.string.isTrue)
                itemView.subText.setTextColor(
                        ContextCompat.getColor(itemView.context, R.color.swipeoption_green))
            } else {
                itemView.subText.text = itemView.context.getString(R.string.isFalse)
                itemView.subText.setTextColor(
                        ContextCompat.getColor(itemView.context, R.color.swipeoption_purple))
            }
            itemView.rowFG.setOnClickListener { onclick(answer.id) }
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(0)
        }

    }

}