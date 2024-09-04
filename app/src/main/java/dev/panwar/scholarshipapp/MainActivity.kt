package dev.panwar.scholarshipapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dev.panwar.scholarshipapp.activities.BaseActivity
import dev.panwar.scholarshipapp.activities.FinanceHeadActivity
import dev.panwar.scholarshipapp.activities.HODActivity
import dev.panwar.scholarshipapp.activities.PrincipalActivity
import dev.panwar.scholarshipapp.activities.StudentActivity
import dev.panwar.scholarshipapp.firebase.FireStoreClass
import dev.panwar.scholarshipapp.model.User

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)
        showProgressDialog("Please Wait...")
        FireStoreClass().loadUserData(this)
    }

    fun updateUI(loggedInUser: User) {
        if (loggedInUser.role=="STUDENT"){
            val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("USER_ID", loggedInUser.id)
                putString("USER_NAME",loggedInUser.name)
                putString("APPLICATION_ID",loggedInUser.applicationNumber)
                apply()
            }
            Log.d("userID STUDENT: Main Activity",loggedInUser.id)
            hideProgressDialogue()
            startActivity(Intent(this,StudentActivity::class.java))
            finish()
        }
        if (loggedInUser.role=="HOD") {
            val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("USER_ID", loggedInUser.id)
                putString("USER_NAME",loggedInUser.name)
                apply()
            }
            Log.d("userID HOD: Main Activity", loggedInUser.id)
            hideProgressDialogue()
            startActivity(Intent(this, HODActivity::class.java))
            finish()
        }

        if (loggedInUser.role=="PRINCIPAL") {
            val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("USER_ID", loggedInUser.id)
                putString("USER_NAME",loggedInUser.name)
                apply()
            }
            Log.d("userID PRINCIPAL: Main Activity", loggedInUser.id)
            hideProgressDialogue()
            startActivity(Intent(this, PrincipalActivity::class.java))
            finish()
        }

        if (loggedInUser.role=="FINANCE_HEAD") {
            val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("USER_ID", loggedInUser.id)
                putString("USER_NAME",loggedInUser.name)
                apply()
            }
            Log.d("userID PRINCIPAL: Main Activity", loggedInUser.id)
            hideProgressDialogue()
            startActivity(Intent(this, FinanceHeadActivity::class.java))
            finish()
        }
    }
}