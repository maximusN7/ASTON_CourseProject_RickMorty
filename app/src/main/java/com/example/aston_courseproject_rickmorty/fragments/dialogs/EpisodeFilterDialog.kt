package com.example.aston_courseproject_rickmorty.fragments.dialogs

import android.app.Dialog
import android.content.Context
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import com.example.aston_courseproject_rickmorty.R

class EpisodeFilterDialog(val context: Context, private val applyClickListener: ApplyClickListener) {

    private lateinit var filterDialog: Dialog

    private fun loadFilterDialog() {
        filterDialog = Dialog(context, R.style.Dialog)
        filterDialog.setTitle(context.resources.getString(R.string.filter_episode))
        filterDialog.setContentView(R.layout.dialog_episode_filter)

        val buttonApply = filterDialog.findViewById<Button>(R.id.buttonApply)
        buttonApply.setOnClickListener {
            applyClickListener.onApplyClick(filterDialog)
        }
    }

    fun showDialog(episodeCodeState: Filter) {
        loadFilterDialog()
        setCurrentState(episodeCodeState)
        filterDialog.show()
    }

    private fun setCurrentState(episodeCodeState: Filter) {
        val checkEpisodeCode = filterDialog.findViewById<CheckBox>(R.id.checkBoxEpisodeCode)
        val editEpisodeCode = filterDialog.findViewById<EditText>(R.id.editTextEpisodeCode)

        checkEpisodeCode.isChecked = episodeCodeState.isApplied
        editEpisodeCode.setText(episodeCodeState.stringToFilter)
    }


    interface ApplyClickListener {

        fun onApplyClick(dialog: Dialog)
    }
}