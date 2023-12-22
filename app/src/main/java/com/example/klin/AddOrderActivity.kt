package com.example.klin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.klin.databinding.ActivityAddOrderBinding

class AddOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddOrderBinding
    private lateinit var jumlahTshirt: TextView
    private lateinit var decreaseButton: Button
    private lateinit var increaseButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        jumlahTshirt = findViewById(R.id.jumlah_tshirt)

        binding.button5.setOnClickListener{
            var currentValue = jumlahTshirt.text.toString().toInt()
            currentValue--
            jumlahTshirt.text = currentValue.toString()
        }
        binding.button8.setOnClickListener{
            var currentValue = jumlahTshirt.text.toString().toInt()
            currentValue++
            jumlahTshirt.text = currentValue.toString()
        }

        setAllListener()
//
//        val buttonPoto = findViewById<Button>(R.id.poto_button)
//        val backOrderlist2 = findViewById<ImageView>(R.id.imageView17)
//
//        backOrderlist2.setOnClickListener{
//
//            val intent = Intent(this, HomeActivity::class.java)
//            startActivity(intent)
//        }
//
//        buttonPoto.setOnClickListener{
//
//            val intent = Intent(this, addAddress::class.java)
//            startActivity(intent)
//        }

    }

    private fun setAllListener(){

        binding.btnPhoto.setOnClickListener{
            val intent = Intent(this@AddOrderActivity, CameraActivity::class.java)
            startActivity(intent)
        }
        binding.imageView17.setOnClickListener{
            val intent = Intent(this@AddOrderActivity, HomeActivity::class.java )
            startActivity(intent)
        }
        binding.imageView18.setOnClickListener{
            val intent = Intent(this@AddOrderActivity, HomeActivity::class.java )
            startActivity(intent)
        }
        binding.imageView29.setOnClickListener{
            val intent = Intent(this@AddOrderActivity, SettingActivity::class.java )
            startActivity(intent)
        }
    }


}