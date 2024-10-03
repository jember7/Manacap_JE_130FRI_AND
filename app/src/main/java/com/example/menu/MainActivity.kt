package com.example.menu


import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        supportActionBar?.title = "MENU"
        supportActionBar?.subtitle = "Welcome"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (menu is MenuBuilder) {
            try {
                val method = menu.javaClass.getDeclaredMethod("setOptionalIconsVisible", Boolean::class.javaPrimitiveType)
                method.isAccessible = true
                method.invoke(menu, true)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_fragment -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, FirstFragment())
                    .commit()
                true
            }
            R.id.action_dialog -> {
                val dialog = CustomDialogFragment()  // Create your DialogFragment
                dialog.show(supportFragmentManager, "MyDialog")
                true
            }
            R.id.action_exit -> {
                true
            }
            R.id.action_exit_confirm -> {
                finish()
                true
            }
            R.id.action_exit_cancel -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



    fun navigateToFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }


   fun showCustomDialog() {
        val dialog = CustomDialogFragment()
        dialog.show(supportFragmentManager, "CustomDialog")
    }


    }
}
