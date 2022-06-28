package com.example.aston_courseproject_rickmorty.fragments.dialogs

import android.app.Dialog
import android.content.Context
import android.widget.Button
import com.example.aston_courseproject_rickmorty.R

class EpisodeFilterDialog(val context: Context) {

    private lateinit var filterDialog: Dialog

    private fun loadFilterDialog() {
        filterDialog = Dialog(context, R.style.Dialog)
        filterDialog.setTitle(context.resources.getString(R.string.filter_episode))
        filterDialog.setContentView(R.layout.dialog_episode_filter)

        val buttonApply = filterDialog.findViewById<Button>(R.id.buttonApply)
        buttonApply.setOnClickListener {
            onDialogClose()
        }
    }

    fun showDialog() {
        loadFilterDialog()
        filterDialog.show()
    }

    private fun onDialogClose() {
        //TODO: create filter apply logic
        filterDialog.dismiss()
    }
}