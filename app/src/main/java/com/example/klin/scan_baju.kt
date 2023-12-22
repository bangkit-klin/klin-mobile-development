package com.example.klin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class scan_baju : AppCompatActivity() {

    private lateinit var buttonBack: ImageView

    private fun init(){
        buttonBack = findViewById(R.id.imageView5)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_baju)
        init()

        buttonBack.setOnClickListener {
            finish()
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}