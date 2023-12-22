package com.example.klin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.klin.databinding.ActivityMainBinding

class MainActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setAllListener()
    }

    private fun setAllListener() {
        binding.tvSignUp.setOnClickListener {
            val intent = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.edEmail.text.toString()
            val password = binding.edPassword.text.toString()
            val intent = Intent(this@MainActivity, HomeActivity::class.java)
            intent.putExtra("email", email)
            intent.putExtra("password", password)
            startActivity(intent)
        }

        binding.tvForgotPassword.setOnClickListener {
            val email = binding.edEmail.text.toString()
            val intent = Intent(this@MainActivity, ForgotPasswordActivity::class.java)
            intent.putExtra("email", email)
            startActivity(intent)
        }
    }

}