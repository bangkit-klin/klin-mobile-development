package com.example.klin

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class MainSplash: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashx)

        Handler().postDelayed(Runnable {
            startActivity(Intent(this, SplashActivity::class.java))
            finish()
        }, 2000)
    }
}