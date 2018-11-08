package com.qmakercorp.qmaker.ui.main.fragments.tabs

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.nikhilpanju.recyclerviewenhanced.OnActivityTouchListener
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener
import com.qmakercorp.qmaker.R
import com.qmakercorp.qmaker.adapters.QuestionsListAdapter
import com.qmakercorp.qmaker.components.Alert
import com.qmakercorp.qmaker.data.dao.QuizzesDao
import com.qmakercorp.qmaker.extensions.generateQRCode
import kotlinx.android.synthetic.main.fragment_quizzes.*
import kotlinx.android.synthetic.main.qrcode_layout.*
import java.io.File
import java.io.FileOutputStream
import androidx.core.content.FileProvider
import com.google.android.material.snackbar.Snackbar
import com.qmakercorp.qmaker.BuildConfig
import com.qmakercorp.qmaker.adapters.QuizzesListAdapter
import com.qmakercorp.qmaker.data.dao.QuizDao
import com.qmakercorp.qmaker.data.model.Quiz
import kotlinx.android.synthetic.main.new_quizz_dialog.*


class QuizzesFragment : Fragment(), RecyclerTouchListener.RecyclerTouchListenerHelper {

    private lateinit var quizzesAdapter: QuizzesListAdapter
    private lateinit var onTouchListener: RecyclerTouchListener
    private var touchListener: OnActivityTouchListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_quizzes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
    }

    override fun onResume() {
        super.onResume()
        startLoading()
        QuizzesDao().getQuizzes {
            stopLoading()
            if (it.size > 0) {
                quizzesAdapter.quizzes = it
                quizzesAdapter.notifyDataSetChanged()
            } else
                showInfo(R.string.empty_list_quizzes)
        }
        rv_quizzes.addOnItemTouchListener(onTouchListener)
    }

    override fun onPause() {
        super.onPause()
        rv_quizzes.removeOnItemTouchListener(onTouchListener)
    }

    override fun setOnActivityTouchListener(listener: OnActivityTouchListener?) {
        this.touchListener = listener
    }

    /** PRIVATE **/

    private fun showInfo(resIdMessage: Int) {
        progress_bar?.let{ it.visibility = View.GONE }
        rv_quizzes.visibility = View.GONE
        tv_info.setText(resIdMessage)
        tv_info.visibility = View.VISIBLE
    }

    private fun startLoading() {
        progress_bar.visibility = View.VISIBLE
        rv_quizzes.visibility = View.GONE
        tv_info.visibility = View.GONE
    }

    private fun stopLoading() {
        progress_bar?.let { it.visibility = View.GONE }
        rv_quizzes?.let { it.visibility = View.VISIBLE }
    }

    private fun initializeViews() {
        (activity as AppCompatActivity).supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(false)
            it.title = getString(R.string.my_quizzes)
        }
        fab.setOnClickListener { didTapNewQuiz(it) }
        initializeRecyclerView()
    }

    private fun didTapNewQuiz(view: View) {
        val dialog = Dialog(view.context)
        with(dialog) {
            setContentView(R.layout.new_question_dialog)
            button_cancel.setOnClickListener { dialog.dismiss() }
            button_add.setText(R.string.save)
            button_add.setOnClickListener {
                val id = QuizDao().generateId()
                val name = et_question.text.toString()
                val quiz = Quiz(id, name, id.substring(0,6).toUpperCase())
                dialog.dismiss()
                if (!name.isEmpty()) {
                    QuizzesDao().saveQuiz(quiz) { success ->
                        if (success) {
                            Snackbar.make(view, R.string.quiz_saved, Snackbar.LENGTH_LONG).show()
                            didTapQuiz(quiz)
                        }
                    }
                }
            }
            show()
        }
    }

    private fun initializeRecyclerView() {
        context?.let { context ->
            setupAutoHideFabOnScrollList()
            quizzesAdapter = QuizzesListAdapter(context, mutableListOf())
            rv_quizzes.adapter = quizzesAdapter
            rv_quizzes.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            onTouchListener = RecyclerTouchListener(activity, rv_quizzes)
                    .setIndependentViews(R.id.button_qrcode)
                    .setClickable(object : RecyclerTouchListener.OnRowClickListener {
                        override fun onRowClicked(position: Int) {
                            didTapQuiz(quizzesAdapter.quizzes[position])
                        }
                        override fun onIndependentViewClicked(independentViewID: Int, position: Int) {
                            didTapQRCode(quizzesAdapter.quizzes[position], context)
                        }
                    }).setSwipeOptionViews(R.id.publish, R.id.delete)
                    .setSwipeable(R.id.rowFG, R.id.rowBG) { viewID, position ->
                        when (viewID) {
                            R.id.publish -> onClickPublishRow(context, position)
                            R.id.delete -> onClickDeleteRow(context, position)
                        }
                    }
        }
    }

    private fun didTapQRCode(quiz: Quiz, context: Context) {
        val dialog = Dialog(context)
        with(dialog) {
            window?.requestFeature(Window.FEATURE_NO_TITLE)
            setContentView(layoutInflater.inflate(R.layout.qrcode_layout, null))
            iv_qrcode.setImageBitmap(BarcodeEncoder().generateQRCode(quiz.code))
            ib_share.setOnClickListener {
                dialog.dismiss()
                shareQuiz(quiz, context)
            }
            button_close.setOnClickListener { dialog.dismiss() }
            setCanceledOnTouchOutside(true)
            show()
        }

    }

    private fun shareQuiz(quiz: Quiz, context: Context) {
        val bitmap = BarcodeEncoder().generateQRCode(quiz.code)
        val cachePath = File(context.cacheDir, "images")
        cachePath.mkdirs() // don't forget to make the directory
        val stream = FileOutputStream(cachePath.toString() + "/image.png") // overwrites this image every time
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.close()
        val imagePath = File(context.cacheDir, "images")
        val newFile = File(imagePath, "image.png")
        val contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", newFile)
        if (contentUri != null) {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, context.contentResolver.getType(contentUri))
            shareIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.text_share_quiz))
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
            shareIntent.type = "image/png"
            startActivity(Intent.createChooser(shareIntent, context.getString(R.string.chose_app)))
        }
    }


    private fun onClickPublishRow(context: Context, position: Int) {
        QuizzesDao().publishQuiz(quizzesAdapter.quizzes[position]) { result ->
            val message = if (result) R.string.quiz_updated else R.string.error_update_quiz
            view?.let { Snackbar.make(it, message, Snackbar.LENGTH_LONG).show() }
        }
    }

    private fun onClickDeleteRow(context: Context, position: Int) {
        Alert(context, getString(R.string.remove_quiz_alert_title), getString(R.string.remove_quiz_alert_message))
                .show(onClickYes = {
                    removeItemList(position)
                }, onClickNo = { })
    }

    private fun setupAutoHideFabOnScrollList() {
        rv_quizzes.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 || dy < 0)
                    fab.fabTextVisibility = View.GONE
            }
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                    fab.fabTextVisibility = View.VISIBLE
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    private fun didTapQuiz(quiz: Quiz) {
        val bundle = Bundle()
        bundle.putParcelable("quiz", quiz)
        view?.findNavController()?.navigate(R.id.action_tab2_to_quizFragment, bundle)
    }

    private fun removeItemList(position: Int) {
        val quiz = quizzesAdapter.quizzes[position]
        QuizzesDao().removeQuiz(quiz) { result ->
            val message = if (result)
                R.string.quiz_removed
            else
                R.string.quiz_remove_error
            view?.let { Snackbar.make(it, message, Snackbar.LENGTH_LONG).show() }
        }
    }

}
