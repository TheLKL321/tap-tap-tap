package com.thelkl.taptaptap.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.thelkl.taptaptap.utils.ENDGAME_DIALOG_FRAGMENT_IFHIGHSCORE_KEY
import com.thelkl.taptaptap.utils.ENDGAME_DIALOG_FRAGMENT_TAPS_KEY

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
        builder.setMessage("You scored " + (arguments?.getInt(ENDGAME_DIALOG_FRAGMENT_TAPS_KEY) ?: "<NO ARGUMENTS PASSED>") + " taps"
                +  if (arguments?.getBoolean(ENDGAME_DIALOG_FRAGMENT_IFHIGHSCORE_KEY) == true) "\nHIGHSCORE!" else "")
            .setPositiveButton("Cool!"
            ) { _, _ -> listener.onDialogPositiveClick(this@EndgameDialogFragment) }
            .setNegativeButton("Try again"
            ) { _, _ -> listener.onDialogNegativeClick(this@EndgameDialogFragment) }
        return builder.create()
    }
}