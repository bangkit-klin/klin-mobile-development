package com.example.klin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.DatePickerDialog
import android.view.View
import android.widget.TextView
import java.util.*
import android.content.Intent
import android.widget.Button

class schedule_pickup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_pickup)

        val confirmOrderbutton = findViewById<Button>(R.id.confirm_button)
        confirmOrderbutton.setOnClickListener{
            val intent = Intent(this, addAddress::class.java)
            startActivity(intent)
        }

        val scheduleDateTextView = findViewById<TextView>(R.id.scheduleDateTextView)
        scheduleDateTextView.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                scheduleDateTextView.text = selectedDate
            }, year, month, day)

            datePickerDialog.show()
        }
    }
}