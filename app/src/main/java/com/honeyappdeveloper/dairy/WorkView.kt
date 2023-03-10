package com.honeyappdeveloper.dairy

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.honeyappdeveloper.dairy.databinding.ActivityWorkViewBinding

class WorkView : AppCompatActivity() {
    lateinit var binding: ActivityWorkViewBinding
    private lateinit var adapter: WorkViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val list = mutableListOf("সাংগঠনিক কাজ","সাংগঠনিক অধ্যয়ন","পাঠ্যপুস্তক অধ্যয়ন","ঘুম ও বিশ্রাম","দাওয়াতি কাজ","ব্যক্তিগত কাজ")
        val date = intent.getStringExtra("date")
        adapter = WorkViewAdapter(listOf())
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@WorkView)
            adapter = this@WorkView.adapter
        }


        val db = FirebaseFirestore.getInstance()
        db.collection("daily_Dairy").document("Mahmudulhasan").collection("dairy")
            .document("$date").collection("dairy").get().addOnSuccessListener { querySnapshot ->
                val workList = mutableListOf<WorkViewModel>()
                workList.add(
                    WorkViewModel(
                        "Date",
                        "Start",
                        "Stop",
                        "Category",
                        "Activity",
                        "Duration"
                    )
                )
                Toast.makeText(this@WorkView, "it.localizedMessage.toString()", Toast.LENGTH_SHORT)
                    .show()
                if (querySnapshot.isEmpty) {
                    Toast.makeText(this@WorkView, "no data", Toast.LENGTH_SHORT).show()
                } else {
                    for (document in querySnapshot.documents) {
                        val work = document.toObject(WorkViewModel::class.java)
                        work?.let {
                            workList.add(it)
                        }
                    }
                    adapter = WorkViewAdapter(workList)
                    binding.recyclerView.adapter = adapter
                }
            }.addOnFailureListener {
                Toast.makeText(this@WorkView, it.localizedMessage.toString(), Toast.LENGTH_SHORT)
                    .show()
            }

    }
}