package com.honeyappdeveloper.dairy
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.honeyappdeveloper.dairy.databinding.SimpleWorkviewBinding

class WorkViewAdapter(private var workList: List<WorkViewModel>) :
    RecyclerView.Adapter<WorkViewAdapter.WorkViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkViewHolder {
        val binding = SimpleWorkviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WorkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkViewHolder, position: Int) {
        val currentWork = workList[position]
        holder.bind(currentWork)
    }

    override fun getItemCount() = workList.size

    inner class WorkViewHolder(private val binding: SimpleWorkviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(work: WorkViewModel) {
            binding.tvTodayDate.text = work.TodayDate
            binding.tvStartTime.text = work.StartTime
            binding.tvStopTime.text = work.StopTime
            binding.tvCategory.text = work.Category
            binding.tvActivity.text = work.Activity
            binding.tvTimeDuration.text = work.TimeDuration
            binding.remove.setOnClickListener{
               val db = FirebaseFirestore.getInstance()
                db.collection("daily_Dairy").document("Mahmudulhasan").collection("dairy")
                    .document(work.TodayDate.toString()).collection("dairy").document("${work.StartTime}-${work.StopTime}").delete()
                db.collection("daily_Dairy").document("Mahmudulhasan").collection("dairy")
                    .document(work.TodayDate.toString()).collection(work.Category.toString())
                    .document("${work.StartTime}-${work.StopTime}").delete()

            }
        }
    }
}
