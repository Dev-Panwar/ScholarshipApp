package dev.panwar.scholarshipapp.activities

import android.R
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import dev.panwar.scholarshipapp.api.RetrofitInstance
import dev.panwar.scholarshipapp.databinding.ActivityTrackScholarshipBinding
import dev.panwar.scholarshipapp.response.TrackApplicationResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class TrackScholarship : BaseActivity() {

    private var binding: ActivityTrackScholarshipBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackScholarshipBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnGoToStudentSection?.setOnClickListener {
            startActivity(Intent(this,StudentActivity::class.java))
            finish()
        }

        binding?.tvTitle?.setOnClickListener {
            onBackPressed()
        }

        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val application_ID = sharedPreferences.getString("APPLICATION_ID", "")

        if (application_ID != null && application_ID.isNotEmpty()) {
            Log.d("application_ID", application_ID)
            binding?.llAlreadyApplied?.visibility = View.VISIBLE
            binding?.referenceNum?.text = application_ID
            makeApiCall(application_ID)
        }else{
            binding?.llStatus?.visibility=View.GONE
            binding?.tvNotApplied?.visibility=View.VISIBLE
        }

    }

    private fun makeApiCall(application_ID: String) {
        showProgressDialog("Please Wait...")

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val result = RetrofitInstance.api.getApplicationStatus()
                val applicationList = result.body()
                if (applicationList != null) {
                    val matchingApplication = applicationList.find { it.id == application_ID }

                    if (matchingApplication != null) {
                        setUI(matchingApplication)
                        Log.d("response", matchingApplication.toString())
                    } else {
                        Toast.makeText(this@TrackScholarship, "No matching application found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@TrackScholarship, "Failed to fetch applications", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("TrackScholarship", "Error fetching application status", e)
                Toast.makeText(this@TrackScholarship, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                hideProgressDialogue()
            }
        }
    }


    private fun setUI(application: TrackApplicationResponse) {
        // Update the UI with the data from the matching application
//        binding?.applicationStatus?.text = application.status
        Log.d("response track",application.toString())
        var status=1
        if (application.approvedByHod==true){
            status=2
        }
        if (application.approvedByPrincipal==true){
            status=3
        }
        if (application.approvedByFinanceHead==true){
            status=4
        }

       for (i in 1..status){
           if (status==1){
               binding?.viewState1?.backgroundTintList=ContextCompat.getColorStateList(this, R.color.holo_blue_bright)
               binding?.viewProgress1?.setBackgroundColor(resources.getColor(R.color.holo_blue_bright))
           }

           if (status==2){
               binding?.viewState1?.backgroundTintList=ContextCompat.getColorStateList(this, R.color.holo_blue_bright)
               binding?.viewProgress1?.setBackgroundColor(resources.getColor(R.color.holo_blue_bright))
               binding?.viewState2?.backgroundTintList=ContextCompat.getColorStateList(this, R.color.holo_blue_bright)
               binding?.viewProgress2?.setBackgroundColor(resources.getColor(R.color.holo_blue_bright))
           }

           if (status==3){
               binding?.viewState1?.backgroundTintList=ContextCompat.getColorStateList(this, R.color.holo_blue_bright)
               binding?.viewProgress1?.setBackgroundColor(resources.getColor(R.color.holo_blue_bright))
               binding?.viewState2?.backgroundTintList=ContextCompat.getColorStateList(this, R.color.holo_blue_bright)
               binding?.viewProgress2?.setBackgroundColor(resources.getColor(R.color.holo_blue_bright))
               binding?.viewState3?.backgroundTintList=ContextCompat.getColorStateList(this, R.color.holo_blue_bright)
               binding?.viewProgress3?.setBackgroundColor(resources.getColor(R.color.holo_blue_bright))
           }

           if (status==4){
               binding?.viewState1?.backgroundTintList=ContextCompat.getColorStateList(this, R.color.holo_blue_bright)
               binding?.viewProgress1?.setBackgroundColor(resources.getColor(R.color.holo_blue_bright))
               binding?.viewState2?.backgroundTintList=ContextCompat.getColorStateList(this, R.color.holo_blue_bright)
               binding?.viewProgress2?.setBackgroundColor(resources.getColor(R.color.holo_blue_bright))
               binding?.viewState3?.backgroundTintList=ContextCompat.getColorStateList(this, R.color.holo_blue_bright)
               binding?.viewProgress3?.setBackgroundColor(resources.getColor(R.color.holo_blue_bright))
               binding?.viewState4?.backgroundTintList=ContextCompat.getColorStateList(this, R.color.holo_blue_bright)
           }
        }

        // Add more UI updates here
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
