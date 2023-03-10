package com.honeyappdeveloper.dairy

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.honeyappdeveloper.dairy.databinding.ActivityInputDateBinding
import java.text.SimpleDateFormat
import java.util.*

class InputDate : AppCompatActivity() {
    private lateinit var tvTodayDate: TextView
    val d_dairy: HashMap<String, String> = HashMap<String, String>()

    lateinit var binding: ActivityInputDateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputDateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tvTodayDate = findViewById(R.id.tv_today_date)
        tvTodayDate.setOnClickListener { showDatePickerDialog() }
        binding.button.setOnClickListener {
           val intent = Intent(this@InputDate,WorkView::class.java)
            intent.putExtra("date",d_dairy["TodayDate"])
            startActivity(intent)
        }


    }

    // Show DatePickerDialog to select today's date
    // Show DatePickerDialog to select today's date
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->

                val date = getDateFromComponents(year, month, dayOfMonth)
                d_dairy.put("TodayDate", date.toString())
                tvTodayDate.text = date
                Toast.makeText(this, "Today's date: $date", Toast.LENGTH_SHORT).show()

            },
            year,
            month,
            dayOfMonth
        )
        datePickerDialog.show()
    }

    private fun getDateFromComponents(year: Int, month: Int, dayOfMonth: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.US)
        return dateFormat.format(calendar.time)
    }

}