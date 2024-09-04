package dev.panwar.scholarshipapp.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import dev.panwar.scholarshipapp.R
import dev.panwar.scholarshipapp.api.RetrofitInstance
import dev.panwar.scholarshipapp.databinding.ActivityHodapplicationsBinding
import dev.panwar.scholarshipapp.ui.adapters.HodApplicationsAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HODApplications : BaseActivity() {

    private var binding:ActivityHodapplicationsBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityHodapplicationsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setupUi()

        binding?.tvTitle?.setOnClickListener {
            onBackPressed()
        }

    }



    private fun setupUi() {
        showProgressDialog("Please Wait...")
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val result = RetrofitInstance.api.getApplicationStatus()
                val applicationList = result.body()
                if (result.isSuccessful) {
                    if (applicationList != null) {
                        val recyclerView=binding?.rvHodApplications
                        recyclerView?.layoutManager= LinearLayoutManager(this@HODApplications)
                        val adapter= HodApplicationsAdapter(this@HODApplications,this@HODApplications,applicationList)
                        recyclerView?.adapter=adapter
                    } else {
//                        no applications
                        setUpNoApplicationsLayout()
                    }
                }

            } catch (e: Exception) {
                Log.e("TrackScholarship", "Error fetching applications", e)
                Toast.makeText(this@HODApplications, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                hideProgressDialogue()
            }
        }

    }

    private fun setUpNoApplicationsLayout() {

    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}