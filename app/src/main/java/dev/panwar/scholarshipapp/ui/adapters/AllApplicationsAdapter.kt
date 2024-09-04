package dev.panwar.scholarshipapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.panwar.scholarshipapp.R
import dev.panwar.scholarshipapp.ui.adapters.AllApplicationsAdapter.ViewHolder
import androidx.cardview.widget.CardView
import android.widget.TextView
import dev.panwar.scholarshipapp.response.TrackApplicationResponse

class AllApplicationsAdapter(var list:List<TrackApplicationResponse>) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater
                        .from(parent.context)
                        .inflate(R.layout.item_applications_view, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentApplication=list[position]
        holder.tvApplicationId.text=currentApplication.id
        holder.tvUserName.text=currentApplication.name
        holder.tvRollNum.text=currentApplication.rollNo
        holder.tvBranch.text=currentApplication.branch
        holder.tvDate.text=currentApplication.createdAt.substring(0,10)
        holder.tvAmount.text="₹${currentApplication.amountSanction.toString()}"
        holder.tvStatus.text=currentApplication.status
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		val cvApplications: CardView = itemView.findViewById(R.id.cv_applications)
		val tvApplicationId: TextView = itemView.findViewById(R.id.tv_application_id)
		val tvUserName: TextView = itemView.findViewById(R.id.tv_user_name)
		val tvRollNum: TextView = itemView.findViewById(R.id.tv_roll_num)
		val tvBranch: TextView = itemView.findViewById(R.id.tv_branch)
		val tvDate: TextView = itemView.findViewById(R.id.tv_date)
		val tvAmount: TextView = itemView.findViewById(R.id.tv_amount)
		val tvStatus: TextView = itemView.findViewById(R.id.tv_status)
    }
}
