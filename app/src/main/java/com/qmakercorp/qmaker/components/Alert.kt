package com.qmakercorp.qmaker.components

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import androidx.appcompat.app.AlertDialog
import com.qmakercorp.qmaker.R
import android.widget.LinearLayout
import com.qmakercorp.qmaker.ui.main.MainActivity
import android.widget.EditText
import androidx.constraintlayout.widget.Placeholder
import android.util.TypedValue
import androidx.core.view.marginLeft
import androidx.core.view.setMargins


class Alert(val context: Context,
            title: String?,
            message: String,
            styleResId: Int) {

    private val alertDialog = AlertDialog.Builder(context, styleResId)

    init {
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
    }

    constructor(context: Context, title: String?, message: String) : this(context, title, message, R.style.dialog_light)

    constructor(context: Context, message: String) : this(context, null, message, R.style.dialog_light)

    constructor(context: Context, message: String, styleResId: Int) : this(context, null, message, styleResId)

    fun show() {
        alertDialog
                .setPositiveButton(R.string.ok, null)
                .create()
                .show()
    }

    fun show(onClickOk: ()->Unit) {
        alertDialog
                .setPositiveButton(R.string.ok) { _, _ ->
                    onClickOk()
                }.create()
                .show()
    }

    fun show(onClickYes: ()->Unit, onClickNo: ()->Unit) {
        alertDialog
                .setPositiveButton(R.string.yes) { _, _ ->
                    onClickYes()
                }.setNegativeButton(R.string.no) { _, _ ->
                    onClickNo()
                }.create()
                .show()
    }

    @SuppressLint("RestrictedApi")
    fun showWithEdittext(placeholder: String?, onPositiveClick: (EditText)->Unit, onNegativeClick: (EditText)->Unit) {
        val editText = EditText(context)
        editText.hint = placeholder
        val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
        editText.layoutParams = lp
        alertDialog
                .setView(editText, 48, 16, 48, 16)
                .setPositiveButton(R.string.ok) { _, _ ->
                    onPositiveClick(editText)
                }.setNegativeButton(R.string.cancel) { _, _ ->
                    onNegativeClick(editText)
                }.show()
    }

}