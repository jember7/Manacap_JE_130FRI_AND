package com.example.menu

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class CustomDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Dialog Fragment")
            .setMessage("This is a Dialog Fragment.")
            .setPositiveButton("Go to Fragment") { _, _ ->
                (activity as MainActivity).navigateToFragment(FirstFragment())
            }
            .setNegativeButton("Exit") { _, _ ->
                activity?.finish()
            }
            .create()
    }
}
