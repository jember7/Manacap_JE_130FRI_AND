package com.example.bottomnav

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var nameEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var genderRadioGroup: RadioGroup
    private lateinit var subscribeCheckBox: CheckBox
    private lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameEditText = view.findViewById(R.id.editTextName)
        ageEditText = view.findViewById(R.id.editTextAge)
        genderRadioGroup = view.findViewById(R.id.radioGroupGender)
        subscribeCheckBox = view.findViewById(R.id.checkBoxSubscribe)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        loadProfileData()

        view.findViewById<View>(R.id.buttonSave).setOnClickListener {
            saveProfileData()
        }
    }

    private fun loadProfileData() {
        val name = sharedPreferences.getString("name", "")
        val age = sharedPreferences.getString("age", "")
        val gender = sharedPreferences.getString("gender", "")
        val isSubscribed = sharedPreferences.getBoolean("isSubscribed", false)

        nameEditText.setText(name)
        ageEditText.setText(age)
        subscribeCheckBox.isChecked = isSubscribed

        when (gender) {
            "Male" -> genderRadioGroup.check(R.id.radio_male)
            "Female" -> genderRadioGroup.check(R.id.radio_female)
        }
    }

    private fun saveProfileData() {
        val name = nameEditText.text.toString()
        val age = ageEditText.text.toString()
        val selectedGenderId = genderRadioGroup.checkedRadioButtonId
        val gender = if (selectedGenderId != -1) {
            view?.findViewById<RadioButton>(selectedGenderId)?.text.toString()
        } else {
            ""
        }
        val isSubscribed = subscribeCheckBox.isChecked

        if (name.isEmpty() || age.isEmpty() || gender.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        with(sharedPreferences.edit()) {
            putString("name", name)
            putString("age", age)
            putString("gender", gender)
            putBoolean("isSubscribed", isSubscribed)
            apply()
        }
        Toast.makeText(requireContext(), "Profile saved", Toast.LENGTH_SHORT).show()
    }
}
