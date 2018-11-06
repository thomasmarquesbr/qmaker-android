package com.qmakercorp.qmaker.ui.main.fragments.tabs


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.qmakercorp.qmaker.R
import com.qmakercorp.qmaker.adapters.QuizzesResultAdapter
import com.qmakercorp.qmaker.data.dao.QuizzesResultDao
import com.qmakercorp.qmaker.data.model.QuizResult
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var adapter: QuizzesResultAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
    }

    override fun onResume() {
        super.onResume()
        startLoading()
        QuizzesResultDao().getQuizzes {
            stopLoading()
            if (it.size > 0) {
                it.sortBy { it.name }
                adapter.quizResultList = it
                adapter.notifyDataSetChanged()
            } else
                showInfo(R.string.empty_list_quizzes)
        }
    }

    /** PRIVATE **/

    private fun initializeViews() {
        (activity as AppCompatActivity).supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(false)
            it.title = getString(R.string.dashboard_resume)
        }
        context?.let { context ->
            adapter = QuizzesResultAdapter(context, mutableListOf()) { position ->
                callStudentsFragment(adapter.quizResultList[position])
            }
            rv_results.adapter = adapter
            rv_results.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

    private fun callStudentsFragment(quizResult: QuizResult) {
        if (quizResult.students.size > 0) {
            val bundle = Bundle()
            bundle.putParcelable("quiz_result", quizResult)
            view?.findNavController()?.navigate(R.id.action_tab1_to_studentsFragment, bundle)
        }
    }

    private fun showInfo(resIdMessage: Int) {
        progress_bar.visibility = View.GONE
        rv_results.visibility = View.GONE
        tv_info.setText(resIdMessage)
        tv_info.visibility = View.VISIBLE
    }

    private fun startLoading() {
        progress_bar.visibility = View.VISIBLE
        rv_results.visibility = View.GONE
        tv_info.visibility = View.GONE
    }

    private fun stopLoading() {
        progress_bar?.let { it.visibility = View.GONE }
        rv_results?.let { it.visibility = View.VISIBLE }
    }

}
