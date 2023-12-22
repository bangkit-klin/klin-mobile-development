package com.example.klin

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.klin.databinding.ActivityHomeBinding
import com.squareup.picasso.Picasso

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        setAllListener()
    }

    private fun setAllListener() {
        binding.btnTambah.setOnClickListener {
            val intent = Intent(this@HomeActivity, AddOrderActivity::class.java)
            startActivity(intent)
        }

        binding.ivSetting.setOnClickListener {
            val intent = Intent(this@HomeActivity, SettingActivity::class.java)
            startActivity(intent)
        }

        binding.tvPaket.setOnClickListener {
            val intent = Intent(this@HomeActivity, PaketActivity::class.java)
            startActivity(intent)
        }
    }

    private fun init(){
//        gambarProfile = findViewById(R.id.imageRegister)
//        namaUserProfile = findViewById(R.id.nama_user_home)
//
//        searchButton = findViewById(R.id.imageView13)
//        historyButton = findViewById(R.id.imageView14)
////        settingButton = findViewById(R.id.imageView15)
//        tambahButton = findViewById(R.id.tambah_button)

        Picasso.get().load(getEncryptedSharedPrefences()?.getString("users_picture", "").toString()).into(binding.ivAccountProfile);
        binding.tvUsername.setText(getEncryptedSharedPrefences()?.getString("users_name", "").toString())
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