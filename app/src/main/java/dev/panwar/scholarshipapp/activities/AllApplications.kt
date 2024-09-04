package dev.panwar.scholarshipapp.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.radiobutton.MaterialRadioButton
import dev.panwar.scholarshipapp.api.RetrofitInstance
import dev.panwar.scholarshipapp.databinding.ActivityAllApplicationsBinding
import dev.panwar.scholarshipapp.response.TrackApplicationResponse
import dev.panwar.scholarshipapp.ui.adapters.AllApplicationsAdapter
import dev.panwar.scholarshipapp.ui.adapters.PrincipalApplicationAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AllApplications : BaseActivity() {

    private var binding:ActivityAllApplicationsBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityAllApplicationsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.tvTitle?.setOnClickListener {
            onBackPressed()
        }

        makeAPIcall("accepted")
        buttonUi(binding?.btnAccepted!!)

        binding?.btnAccepted?.setOnClickListener {
            makeAPIcall("accepted")
            buttonUi(binding?.btnAccepted!!)
        }

        binding?.btnPending?.setOnClickListener {
            makeAPIcall("pending")
            buttonUi(binding?.btnPending!!)
        }

        binding?.btnRejected?.setOnClickListener {
            makeAPIcall("rejected")
            buttonUi(binding?.btnRejected!!)
        }





    }

    private fun makeAPIcall(s: String) {
        if (s == "accepted"){
            showProgressDialog("Please Wait")
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val response=RetrofitInstance.api.getAcceptedApplication()
                    if (response.isSuccessful){
                        val applicationList=response.body()
                        if (applicationList!=null){
                            setUI(applicationList)
                        }
                    }else{
                        Toast.makeText(this@AllApplications,"Failed to Fetch",Toast.LENGTH_SHORT).show()
                    }
                }catch (e:Exception){
                    Log.d("api exception: All Aplications",e.message.toString())
                }
                finally {
                    hideProgressDialogue()
                }

            }
        }else if (s=="pending"){
            showProgressDialog("Please Wait")
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val response=RetrofitInstance.api.getPendingApplication()
                    if (response.isSuccessful){
                        val applicationList=response.body()
                        if (applicationList!=null){
                            setUI(applicationList)
                        }
                    }else{
                        Toast.makeText(this@AllApplications,"Failed to Fetch",Toast.LENGTH_SHORT).show()
                    }
                }catch (e:Exception){
                    Log.d("api exception: All Aplications",e.message.toString())
                }
                finally {
                    hideProgressDialogue()
                }

            }

        }else if(s=="rejected"){
            showProgressDialog("Please Wait")
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val response=RetrofitInstance.api.getRejectedApplication()
                    if (response.isSuccessful){
                        val applicationList=response.body()
                        if (applicationList!=null){
                            setUI(applicationList)
                        }
                    }else{
                        Toast.makeText(this@AllApplications,"Failed to Fetch",Toast.LENGTH_SHORT).show()
                    }
                }catch (e:Exception){
                    Log.d("api exception: All Aplications",e.message.toString())
                }
                finally {
                    hideProgressDialogue()
                }

            }
        }

    }

    private fun setUI(applicationList: List<TrackApplicationResponse>) {
        val recycler=binding?.rvApplications
        recycler?.layoutManager= LinearLayoutManager(this@AllApplications)
        val adapter= AllApplicationsAdapter(applicationList)
        recycler?.adapter=adapter
    }

    private fun buttonUi(radioButton: MaterialRadioButton) {
        binding?.btnAccepted?.setChecked(false)
        binding?.btnPending?.setChecked(false)
        binding?.btnRejected?.setChecked(false)

        radioButton.isChecked = true
    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}