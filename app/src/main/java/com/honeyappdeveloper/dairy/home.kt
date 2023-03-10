package com.honeyappdeveloper.dairy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.honeyappdeveloper.dairy.databinding.ActivityHomeBinding

class home : AppCompatActivity() {
    lateinit var binding:ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            val intent = Intent(this@home,MainActivity::class.java)
            startActivity(intent)
        }
        binding.button2.setOnClickListener {
            val intent = Intent(this@home,InputDate::class.java)
            startActivity(intent)
        }
        binding.button4.setOnClickListener {
            val intent = Intent(this@home,DailySummary::class.java)
            startActivity(intent)
        }
    }
}