package com.thelkl.taptaptap

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog

class EndgameDialogFragment : DialogFragment() {
    interface EndgameDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    private lateinit var listener: EndgameDialogListener

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val callingActivity: Activity = context as Activity //will always be an Activity

        try {
            listener = callingActivity as EndgameDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$activity must implement EndgameDialogListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity as Context)
        builder.setMessage("You scored " + (arguments?.getInt(ENDGAME_DIALOG_FRAGMENT_TAPS_KEY) ?: "NO ARGUMENTS PASSED") + " taps")
            .setPositiveButton("Cool!"
            ) { _, _ -> listener.onDialogPositiveClick(this@EndgameDialogFragment) }
            .setNegativeButton("Try again"
            ) { _, _ -> listener.onDialogNegativeClick(this@EndgameDialogFragment) }
        return builder.create()
    }
}