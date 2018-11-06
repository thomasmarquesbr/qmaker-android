package com.qmakercorp.qmaker.ui.main.fragments.students

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.qmakercorp.qmaker.R
import com.qmakercorp.qmaker.adapters.StudentsAdapter
import com.qmakercorp.qmaker.data.model.QuizResult
import kotlinx.android.synthetic.main.fragment_students.*

class StudentsFragment : Fragment() {

    private lateinit var quizResult: QuizResult
    private lateinit var adapter: StudentsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_students, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let { it ->
            quizResult = it.getParcelable("quiz_result") as QuizResult
        }
        initializeViews()
    }

    /** PRIVATE **/

    private fun initializeViews() {
        (activity as AppCompatActivity).supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeButtonEnabled(true)
            it.title = getString(R.string.students)
        }
        context?.let { context ->
            adapter = StudentsAdapter(context, quizResult.students)
            rv_results.adapter = adapter
            rv_results.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

}
