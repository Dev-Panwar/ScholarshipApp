package dev.panwar.scholarshipapp.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import dev.panwar.scholarshipapp.R
import dev.panwar.scholarshipapp.api.RetrofitInstance
import dev.panwar.scholarshipapp.databinding.ActivityFundsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FundsActivity : BaseActivity() {

    private var binding:ActivityFundsBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityFundsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.tvTitle?.setOnClickListener {
            onBackPressed()
        }

        setupUi()

    }

    private fun setupUi() {
        CoroutineScope(Dispatchers.Main).launch {
            showProgressDialog("Please wait..")
            try {
                val result=RetrofitInstance.api.getFunds()
                if (result.isSuccessful){
                    binding?.cvFunds?.visibility=View.VISIBLE
                    binding?.tvTotalAmount?.text="₹${result.body()?.totalAmount.toString()}"
                    binding?.tvTotalSanctioned?.text="₹${result.body()?.totalAmountSanctioned.toString()}"
                    binding?.tvTotalRemaining?.text="₹${result.body()?.remainingAmount.toString()}"
                    binding?.tvTotalDifference?.text="₹${result.body()?.difference.toString()}"
                }
            }catch (e:Exception){
                Log.d("exception Funds",e.message.toString())
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