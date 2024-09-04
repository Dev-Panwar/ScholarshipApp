package dev.panwar.scholarshipapp.activities


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import com.google.firebase.auth.FirebaseAuth
import dev.panwar.scholarshipapp.databinding.ActivityStudentBinding
import dev.panwar.scholarshipapp.firebase.FireStoreClass
import dev.panwar.scholarshipapp.intro.IntroActivity
import dev.panwar.scholarshipapp.model.User
class StudentActivity : BaseActivity() {

private lateinit var binding: ActivityStudentBinding

lateinit var user:User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
     binding = ActivityStudentBinding.inflate(layoutInflater)
     setContentView(binding.root)

//        FireStoreClass().loadUserData(this)

        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("USER_NAME", "")
        val userId=sharedPreferences.getString("USER_ID", "")
        val application_ID=sharedPreferences.getString("APPLICATION_ID", "")

        if (application_ID != null) {
            Log.d("application_ID",application_ID)
        }

        if (username!=null){
            binding.username.text=username

        }

        if (userId!=null){
            Log.d("userID: Student Activity",userId)
        }

        binding.btnNotifications.setOnClickListener {
            startActivity(Intent(this,UserNotifications::class.java))
        }


        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this,IntroActivity::class.java))
            finish()
        }

        binding.btnApply.setOnClickListener {
            startActivity(Intent(this,ApplyScholarship::class.java))
        }

        binding.btnTrack.setOnClickListener {
            startActivity(Intent(this,TrackScholarship::class.java))
        }


    }

    fun setUserData(loggedInUser: User) {
        user=loggedInUser
    }

}