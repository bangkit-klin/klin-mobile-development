package com.example.klin

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class SettingActivity : AppCompatActivity() {

    private lateinit var logoutButton: TextView
    private lateinit var gambarProfile: CircleImageView
    private lateinit var namaProfile: TextView
    private lateinit var emailProfile: TextView

    private lateinit var homeButton: ImageView
    private lateinit var registerMitra: TextView


    private fun init() {
        logoutButton = findViewById(R.id.logout)
        gambarProfile = findViewById(R.id.imageRegister3)
        namaProfile = findViewById(R.id.textView26)
        emailProfile = findViewById(R.id.textView27)
        registerMitra = findViewById(R.id.textView9)

        homeButton = findViewById(R.id.imageView16)

        Picasso.get().load(getEncryptedSharedPrefences()?.getString("users_picture", "").toString()).into(gambarProfile);
        namaProfile.setText(getEncryptedSharedPrefences()?.getString("users_name", "").toString())
        emailProfile.setText(getEncryptedSharedPrefences()?.getString("users_email", "").toString())

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        init()

        logoutButton.setOnClickListener {
            finish()
            getEncryptedSharedPrefences()?.edit()
                ?.putString("users_email", "")
                ?.putString("users_role", "")
                ?.putString("users_name", "")
                ?.putString("users_id", "")
                ?.putString("users_phone", "")
                ?.putString("firebase_uid", "")
                ?.putString("users_picture", "")
                ?.apply()
            startActivity(Intent(this, Login::class.java))
        }

        homeButton.setOnClickListener {
            finish()
            startActivity(Intent(this, HomeActivity::class.java))
        }

        registerMitra.setOnClickListener{
            finish()
            startActivity(Intent(this, register_mitra::class.java))
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