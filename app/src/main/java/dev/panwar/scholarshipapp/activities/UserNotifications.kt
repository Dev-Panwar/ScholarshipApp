package dev.panwar.scholarshipapp.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dev.panwar.scholarshipapp.R
import dev.panwar.scholarshipapp.api.RetrofitInstance
import dev.panwar.scholarshipapp.databinding.ActivityUserNotificationsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserNotifications : BaseActivity() {

    private var binding:ActivityUserNotificationsBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityUserNotificationsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.tvTitle?.setOnClickListener {
            onBackPressed()
        }

        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val application_ID = sharedPreferences.getString("APPLICATION_ID", "")
        val userId=sharedPreferences.getString("USER_ID", "")

        showProgressDialog("Please Wait..")
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response=RetrofitInstance.api.getUserNotifications()
                if (response.isSuccessful){
                    val notificationList=response.body()
                    if (notificationList!=null){
                        val myNotification=notificationList.find { it.userId==userId }
                        if (myNotification!=null){
                            binding?.cvNotification?.visibility=View.VISIBLE
                            binding?.tvNoNotifications?.visibility=View.GONE
                            binding?.tvNotificationTitle?.text=myNotification.title
                            binding?.tvMessage?.text=myNotification.message
                        }else{
                            binding?.cvNotification?.visibility=View.GONE
                            binding?.tvNoNotifications?.visibility=View.VISIBLE
                        }
                    }
                }
            }catch (e:Exception){
                binding?.cvNotification?.visibility=View.GONE
                binding?.tvNoNotifications?.visibility=View.VISIBLE
                Toast.makeText(this@UserNotifications,"No Notifications Found",Toast.LENGTH_SHORT).show()
                Log.d("Error fetching notifications",e.message.toString())
            }finally {
                hideProgressDialogue()
            }


        }


    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}