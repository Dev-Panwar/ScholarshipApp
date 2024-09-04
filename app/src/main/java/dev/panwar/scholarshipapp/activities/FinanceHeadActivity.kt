package dev.panwar.scholarshipapp.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import dev.panwar.scholarshipapp.R
import dev.panwar.scholarshipapp.databinding.ActivityFinanceHeadBinding
import dev.panwar.scholarshipapp.intro.IntroActivity

class FinanceHeadActivity : BaseActivity() {

    private var binding:ActivityFinanceHeadBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityFinanceHeadBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("USER_NAME", "")
        val userId=sharedPreferences.getString("USER_ID", "")
        val application_ID=sharedPreferences.getString("APPLICATION_ID", "")

        if (application_ID != null) {
            Log.d("application_ID",application_ID)
        }

        if (username!=null){
            binding?.username?.text=username
        }

        binding?.btnSeeApplications?.setOnClickListener {
            startActivity(Intent(this,FinanceHeadApplications::class.java))
        }

        binding?.btnLogout?.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, IntroActivity::class.java))
            finish()
        }

        binding?.btnSeeFunds?.setOnClickListener {
            startActivity(Intent(this, FundsActivity::class.java))
        }

        binding?.btnViewApplicants?.setOnClickListener {
            startActivity(Intent(this, AllApplications::class.java))
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}