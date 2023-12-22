package com.example.klin

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.android.material.button.MaterialButton
import com.google.gson.JsonObject
import kotlinx.coroutines.launch

class Login : AppCompatActivity() {

    private lateinit var editTextTextEmailAddress: EditText
    private lateinit var editTextTextPassword: EditText
    private lateinit var buttonsignin: MaterialButton
    private lateinit var buttonsignup: TextView

    private lateinit var apiService: ApiService
    private var progressDialog: ProgressDialog? = null


    private fun init(){
        editTextTextEmailAddress = findViewById(R.id.editTextTextEmailAddress)
        editTextTextPassword = findViewById(R.id.editTextTextPassword)
        buttonsignin = findViewById(R.id.buttonlogin)
        buttonsignup = findViewById(R.id.buttonsignup)

        apiService = RetrofitHelper.getInstance().create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()

        buttonsignin.setOnClickListener{
            val email = editTextTextEmailAddress.text.toString()
            val password = editTextTextPassword.text.toString()
            loginUser(email, password)
        }

        buttonsignup.setOnClickListener{
            finish()
            startActivity(Intent(this@Login, RegisterActivity::class.java))
        }

    }

    private fun loginUser(email: String, password: String) {
        lifecycleScope.launch {
            progressDialog = ProgressDialog.show(this@Login, null, "Please Wait....", true)
            val body = JsonObject().apply{
                addProperty("users_email", "${email}")
                addProperty("users_password", "${password}")
            }
            val result = apiService.signin("${BuildConfig.X_API_Key}",body)
            if (result.isSuccessful){
                Log.e("API", "signin success: ${result.body()}")
                if(result.code()==200){
                    result.body()?.let { response ->
                        val userObject = response.getAsJsonObject("user")
                        saveUserData(userObject)
                    }
                    finish()
                    startActivity(Intent(this@Login, HomeActivity::class.java))
                }
            }else{
                Log.e("API", "signin failed: ${result.message()}")
            }
            progressDialog?.dismiss()
        }
    }

    private fun saveUserData(userObject: JsonObject) {
        val userEmail = userObject.getAsJsonPrimitive("users_email")?.asString
        val userRole = userObject.getAsJsonPrimitive("users_role")?.asString
        val userName = userObject.getAsJsonPrimitive("users_name")?.asString
        val userId = userObject.getAsJsonPrimitive("users_id")?.asString
        val userPhone = userObject.getAsJsonPrimitive("users_phone")?.asString
        val firebaseUid = userObject.getAsJsonPrimitive("firebase_uid")?.asString
        val userPicture = userObject.getAsJsonPrimitive("users_picture")?.asString

        if (userName != null) {
            finish()
            getEncryptedSharedPrefences()?.edit()
                ?.putString("users_email", userEmail)
                ?.putString("users_role", userRole)
                ?.putString("users_name", userName)
                ?.putString("users_id", userId)
                ?.putString("users_phone", userPhone)
                ?.putString("firebase_uid", firebaseUid)
                ?.putString("users_picture", userPicture)
                ?.apply()
            startActivity(Intent(this@Login, HomeActivity::class.java))
        } else {
            // Handle the case where "users_name" is null
            Log.e("API", "users_name is null in the API response")
        }
    }

    fun getEncryptedSharedPrefences(): SharedPreferences? {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        return EncryptedSharedPreferences.create(
            "secret_shared_prefs_file",
            masterKeyAlias,
            this,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}