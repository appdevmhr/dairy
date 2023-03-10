package com.honeyappdeveloper.dairy

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.honeyappdeveloper.dairy.databinding.ActivityDailySummaryBinding
import java.text.SimpleDateFormat
import java.util.*

class DailySummary : AppCompatActivity() {
    private lateinit var tvTodayDate: TextView
    private var list = mutableListOf(
        "সাংগঠনিক কাজ",
        "সাংগঠনিক অধ্যয়ন",
        "পাঠ্যপুস্তক অধ্যয়ন",
        "ঘুম ও বিশ্রাম",
        "দাওয়াতি কাজ",
        "ব্যক্তিগত কাজ"
    )
    val duration = mutableListOf<String>("1:0", "0:30", "1:30", "3:20")
    val d_dairy: HashMap<String, String> = HashMap<String, String>()
    lateinit var db: FirebaseFirestore
    lateinit var binding: ActivityDailySummaryBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDailySummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tvTodayDate = findViewById(R.id.tv_today_date)
        tvTodayDate.setOnClickListener { showDatePickerDialog() }


    }

    //     Show DatePickerDialog to select today's date
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->

                val date = getDateFromComponents(year, month, dayOfMonth)
                tvTodayDate.text = date
                find_duration(date)

            },
            year,
            month,
            dayOfMonth
        )
        datePickerDialog.show()
    }

    private fun find_duration(date: String) {
        val songoKajList = mutableListOf<String>()
        val songoOdaList = mutableListOf<String>()
        val acadiOdaiList = mutableListOf<String>()
        val pesoKajList = mutableListOf<String>()
        val sleepList = mutableListOf<String>()
        val dawahKajList = mutableListOf<String>()


        sk(songoKajList, list[0], date) { skk ->
            binding.OnnaOnaSonKajValue.text = skk
        }
        sk(songoOdaList, list[1], date) { soo ->
            binding.sonOdayonValue.text = soo

        }
        sk(acadiOdaiList, list[2], date) {
            binding.academicOdauyonValue.text = it
        }
        sk(sleepList, list[3], date) {
            binding.sleepValue.text = it
        }
        sk(dawahKajList, list[4], date) { dkkk ->
            binding.dawahValue.text = dkkk

        }
        sk(pesoKajList, list[5], date) {
            binding.personalWorkValue.text = it
        }


//        binding.OnnaOnaSonKajValue.text = sumtime(sumTotalDuration(songoKajList).toString(),sumTotalDuration(songoOdaList).toString())
//        binding.sonKajValue.text = sumtime(sumTotalDuration(songoKajList).toString(),sumTotalDuration(dawahKajList).toString())


    }

    private fun sk(
        songoKajList: MutableList<String>,
        s: String,
        date: String,
        callback: (String) -> Unit
    ) {
        db = FirebaseFirestore.getInstance()
        db.collection("daily_Dairy").document("Mahmudulhasan").collection("dairy")
            .document("$date").collection(s).get().addOnSuccessListener { querySnapshot ->


                Toast.makeText(
                    this@DailySummary,
                    "it.localizedMessage.toString()",
                    Toast.LENGTH_SHORT
                )
                    .show()
                if (querySnapshot.isEmpty) {
                    Toast.makeText(this@DailySummary, "no data", Toast.LENGTH_SHORT).show()
                } else {
                    for (document in querySnapshot.documents) {
                        val work = document.data?.get("TimeDuration")
                        work?.let {
                            songoKajList.add(it as String)
                        }
                    }

                    callback(sumTotalDuration(songoKajList))
                    Toast.makeText(
                        this@DailySummary,
                        songoKajList[0].toString(),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }.addOnFailureListener {
                Toast.makeText(
                    this@DailySummary,
                    it.localizedMessage.toString(),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }


    }

    private fun sumTotalDuration(songoKajList: MutableList<String>): String {
        val minutesList = songoKajList.map {
            val (hours, minutes) = it.split(":")
            hours.toInt() * 60 + minutes.toInt()
        }
        val totalMinutes = minutesList.sum()

        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return "$hours:$minutes"

    }


    private fun sumtime(s: String, s1: String): CharSequence? {
        val bSl = s.split(":")
        val aSl = s1.split(":")
        val aHours = aSl[0].toInt()
        val aMinutes = aSl[1].toInt()
        val Hours = bSl[0].toInt()
        val Minutes = bSl[1].toInt()
        val bbtotalMinutes = aHours * 60 + aMinutes + Hours * 60 + Minutes
        val bbtotalHours = bbtotalMinutes / 60
        val bbtotalRemainderMinutes = bbtotalMinutes % 60
        val bbtotalDuration = "%d:%02d".format(bbtotalHours, bbtotalRemainderMinutes)
        return bbtotalDuration
    }

    private fun getDateFromComponents(year: Int, month: Int, dayOfMonth: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.US)
        return dateFormat.format(calendar.time)
    }
}