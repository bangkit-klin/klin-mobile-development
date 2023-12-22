package com.example.klin

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.klin.databinding.ActivityRegisterBinding
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class RegisterActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    private var progressDialog: ProgressDialog? = null
    private val PICK_IMAGE = 1
    private var imageFile: File? = null

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setAllListener()
        init()

        binding.imageRegister.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE)
        }
    }

    private fun setAllListener() {
        binding.btnRegister.setOnClickListener {
            val email = binding.edEmail.text.toString()
            val name = binding.edName.text.toString()
            val password = binding.edPassword.text.toString()
            val phone = binding.edPhone.text.toString()
            registerUser(email, phone, name, password)
        }
    }

    private fun init() {
        apiService = RetrofitHelper.getInstance().create(ApiService::class.java)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                val imageUri = data?.data
                CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this)
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri
                imageFile = File(resultUri.path)
                binding.imageRegister.setImageURI(resultUri)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                // handle error
            }
        }
    }

    private fun registerUser(email: String, phone: String, name: String, password: String) {
        lifecycleScope.launch {
            progressDialog = ProgressDialog.show(this@RegisterActivity, null, "Please Wait....", true)

            // Convert string values to RequestBody
            val nameRequestBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
            val emailRequestBody = email.toRequestBody("text/plain".toMediaTypeOrNull())
            val passwordRequestBody = password.toRequestBody("text/plain".toMediaTypeOrNull())
            val phoneRequestBody = phone.toRequestBody("text/plain".toMediaTypeOrNull())
            val roleRequestBody  = "costumer".toRequestBody("text/plain".toMediaTypeOrNull())

            // Create a MultipartBody.Part for the image file
            val imageRequestBody = imageFile?.let {
                val imageFileRequestBody =
                    it.asRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("users_picture", it.name, imageFileRequestBody)
            }

            val result = apiService.signup(
                "${BuildConfig.X_API_Key}",
                nameRequestBody,
                emailRequestBody,
                passwordRequestBody,
                phoneRequestBody,
                roleRequestBody,
                imageRequestBody
            )

            if (result.isSuccessful) {
                Toast.makeText(this@RegisterActivity, "Sign Up Success :", Toast.LENGTH_SHORT).show()
                Log.e("API", "signup success: ${result.body()}")
                if (result.code() == 200) {
                    result.body()?.let { response ->
                        val userObject = response.getAsJsonObject()
                    }
//                    finish()
                    startActivity(Intent(this@RegisterActivity, Login::class.java))
                }
            } else {
                Log.e("API", "signup failed: ${result.message()}")
            }
            progressDialog?.dismiss()
        }
    }
}