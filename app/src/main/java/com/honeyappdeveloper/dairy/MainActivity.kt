package com.honeyappdeveloper.dairy

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.honeyappdeveloper.dairy.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {
    private lateinit var tvTodayDate: TextView
    private lateinit var etStartTime: EditText
    private lateinit var etStopTime: EditText
    private lateinit var rgCategory: RadioGroup
    private lateinit var etActivity: EditText
    private lateinit var db: FirebaseFirestore
    private lateinit var firebase: FirebaseApp
    private lateinit var activityMainBinding: ActivityMainBinding
    val d_dairy: HashMap<String, String> = HashMap<String, String>()
    val catagory = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)


        FirebaseApp.initializeApp(this@MainActivity)
        // Initialize views
        tvTodayDate = findViewById(R.id.tv_today_date)
        etStartTime = findViewById(R.id.et_start_time)
        etStopTime = findViewById(R.id.et_stop_time)
        rgCategory = findViewById(R.id.rg_category)
        etActivity = findViewById(R.id.et_activity)
// Set click listeners
        tvTodayDate.setOnClickListener { showDatePickerDialog() }
        etStartTime.setOnClickListener { StartshowTimePickerDialog(etStartTime) }
        etStopTime.setOnClickListener { StopshowTimePickerDialog(etStopTime) }

        // Get selected category and activity and show toast
        val btnSubmit: Button = findViewById(R.id.button3)
        btnSubmit.setOnClickListener { showSelection() }


    }

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

    private fun StartshowTimePickerDialog(etTime: EditText) {
        val calendar = Calendar.getInstance()
        var hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        var minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                val time = getTimeFromComponents(hourOfDay, minute)
                d_dairy.put("StartTime", time.toString())
                etTime.setText(time)
                Toast.makeText(this, "${etTime.hint}: $time", Toast.LENGTH_SHORT).show()
            },
            hourOfDay,
            minute,
            true
        )
        timePickerDialog.show()
    }

    private fun StopshowTimePickerDialog(etTime: EditText) {
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                val time = getTimeFromComponents(hourOfDay, minute)
                d_dairy.put("StopTime", time.toString())
                etTime.setText(time)
                Toast.makeText(this, "${etTime.hint}: $time", Toast.LENGTH_SHORT).show()
            },
            hourOfDay,
            minute,
            true
        )
        timePickerDialog.show()
    }


    private fun showSelection() {
        val selectedCategoryId = rgCategory.checkedRadioButtonId
        val selectedCategory = findViewById<RadioButton>(selectedCategoryId).text.toString()
        val activity = etActivity.text.toString()
        d_dairy.put("Activity", activity)

        d_dairy.put("Category", selectedCategory)
        Toast.makeText(
            this,
            "Selected category: $selectedCategory\nActivity: $activity",
            Toast.LENGTH_SHORT
        ).show()

        TimeDuration(selectedCategory)
        d_dairy.forEach {
            Toast.makeText(
                this,
                "${it.key}\n${it.value}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun TimeDuration(selectedCategory: String) {
        var start = d_dairy["StartTime"]
        var stop = d_dairy["StopTime"]
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.US)
        val start_time = timeFormat.parse(start)
        val stop_time = timeFormat.parse(stop)
        val hour = (stop_time.time - start_time.time) % 86400000 / 3600000
        val minute = (stop_time.time - start_time.time) % 86400000 % 3600000 / 60000
        d_dairy.put("TimeDuration", "$hour:$minute")
        putData(selectedCategory)
        Toast.makeText(this@MainActivity, "hour : ${hour} minute : ${minute}", Toast.LENGTH_SHORT)
            .show()

    }

    private fun getDateFromComponents(year: Int, month: Int, dayOfMonth: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.US)
        return dateFormat.format(calendar.time)
    }

    private fun getTimeFromComponents(hourOfDay: Int, minute: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.US)
        return timeFormat.format(calendar.time)
    }

    private fun putData(selectedCategory: String) {
        db = FirebaseFirestore.getInstance()
        val id = "${d_dairy["StartTime"]}-${d_dairy["StopTime"]}"

        db.collection("daily_Dairy").document("Mahmudulhasan").collection("dairy")
            .document(d_dairy["TodayDate"].toString()).collection("dairy").document(id)
            .set(d_dairy).addOnSuccessListener {
                db.collection("daily_Dairy").document("Mahmudulhasan").collection("dairy")
                    .document(d_dairy["TodayDate"].toString()).collection(selectedCategory)
                    .document(id)
                    .set(d_dairy).addOnSuccessListener {
                        activityMainBinding.rgCategory.clearCheck()
                        activityMainBinding.etActivity.setText("")
                        Toast.makeText(this@MainActivity, "Data saved", Toast.LENGTH_SHORT).show()
                    }
            }.addOnFailureListener {
                Toast.makeText(
                    this@MainActivity,
                    it.localizedMessage.toString(),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

    }
}