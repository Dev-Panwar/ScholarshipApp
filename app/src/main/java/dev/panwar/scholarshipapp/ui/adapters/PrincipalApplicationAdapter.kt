package dev.panwar.scholarshipapp.ui.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.RecyclerView
import dev.panwar.scholarshipapp.R
import dev.panwar.scholarshipapp.activities.PrincipalApplications
import dev.panwar.scholarshipapp.api.RetrofitInstance
import dev.panwar.scholarshipapp.request.PrincipalApprovalRequest
import dev.panwar.scholarshipapp.response.TrackApplicationResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PrincipalApplicationAdapter (val activity: Activity, var context: Context, var list:List<TrackApplicationResponse>) : RecyclerView.Adapter<PrincipalApplicationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_applications, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentApplication=list[position]

        if (currentApplication.approvedByPrincipal==true){
            holder.ll_not_approved.visibility= View.GONE
            holder.btnApproved.visibility= View.VISIBLE
        }else if (currentApplication.approvedByPrincipal==false){
            holder.ll_not_approved.visibility= View.VISIBLE
            holder.btnApproved.visibility= View.GONE
        }


        holder.tvApplicationId.text=currentApplication.id
        holder.tvUserName.text=currentApplication.name
        holder.tvRollNum.text=currentApplication.rollNo
        holder.tvBranch.text=currentApplication.branch
        holder.tvDate.text=currentApplication.createdAt.substring(0,10)

        holder.btnViewAadhar.setOnClickListener {
            openUrl(currentApplication.aadharCard)
        }

        holder.btnViewIncome.setOnClickListener {
            openUrl(currentApplication.incomeCertificate)
        }

        holder.btnViewMarksheet.setOnClickListener {
            openUrl(currentApplication.marksheet)
        }

        holder.btnApprove.setOnClickListener {
            if (holder.etFeedback.text?.isEmpty()!=true){
                makeAPICall(currentApplication.id,holder.etFeedback.text.toString(),true,position)
            }else{
                Toast.makeText(context,"Please Enter Feedback", Toast.LENGTH_SHORT).show()
            }

        }

        holder.btnDecline.setOnClickListener {
            if (holder.etFeedback.text?.isEmpty()!=true){
                makeAPICall(currentApplication.id,holder.etFeedback.text.toString(),false,position)
            }else{
                Toast.makeText(context,"Please Enter Feedback", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun makeAPICall(
        Application_ID: String,
        feedback: String,
        approve: Boolean,
        position: Int
    ) {
        (activity as PrincipalApplications).showProgressDialog("Please Wait")
        val request = PrincipalApprovalRequest(approve = approve, principalFeedback = feedback)
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val result = RetrofitInstance.api.uploadPrincipalApproval(Application_ID,request)
                if (result.isSuccessful){
                    if (result.body()!!.application.approvedByPrincipal==true){
                        list[position].approvedByPrincipal = approve
                        // Notify the adapter about the item change
                        notifyDataSetChanged()
                    }
                    Log.d("approveResult",result.body().toString())
                    Toast.makeText(context,"Feedback Sent!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace() // Handle the error
                Toast.makeText(context,"Failed to send Feedback!", Toast.LENGTH_SHORT).show()
            }finally {
                (activity as PrincipalApplications).hideProgressDialogue()
            }
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvApplicationId: TextView = itemView.findViewById(R.id.tv_application_id)
        val tvUserName: TextView = itemView.findViewById(R.id.tv_user_name)
        val tvRollNum: TextView = itemView.findViewById(R.id.tv_roll_num)
        val tvBranch: TextView = itemView.findViewById(R.id.tv_branch)
        val tvDate: TextView = itemView.findViewById(R.id.tv_date)
        val btnViewAadhar: AppCompatButton = itemView.findViewById(R.id.btn_view_aadhar)
        val btnViewIncome: AppCompatButton = itemView.findViewById(R.id.btn_view_Income)
        val btnViewMarksheet: AppCompatButton = itemView.findViewById(R.id.btn_view_marksheet)
        val etFeedback: AppCompatEditText = itemView.findViewById(R.id.et_feedback)
        val btnApprove: AppCompatButton = itemView.findViewById(R.id.btn_approve)
        val btnDecline: AppCompatButton = itemView.findViewById(R.id.btn_decline)
        val ll_not_approved: LinearLayout =itemView.findViewById(R.id.ll_not_approved)
        val btnApproved: AppCompatButton =itemView.findViewById(R.id.btn_approved)
    }

    fun openUrl(url:String){
        val intent= Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }
}
