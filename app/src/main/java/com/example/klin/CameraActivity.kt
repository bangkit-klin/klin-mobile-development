package com.example.klin

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CameraActivity : AppCompatActivity() {

    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private lateinit var viewFinder: PreviewView
    private lateinit var captureImage: ImageView
    private lateinit var switchCamera: ImageView
    private var imageCapture: ImageCapture? = null
    private lateinit var apiService: ApiService
    private val coroutineJob = Job()
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)


    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 1001
        const val EXTRA_CAMERAX_IMAGE = "Camerax Image"
        const val CAMERAX_RESULT = 200
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_testing_camera)

        // Check if the camera permission is granted
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_DENIED
        ) {
            // Request the camera permission
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            // Camera permission is already granted, proceed with using the camera
            initCamera()
        }

        init()
        startCamera()

        switchCamera.setOnClickListener {
            cameraSelector =
                if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
                else CameraSelector.DEFAULT_BACK_CAMERA
            startCamera()
        }

        captureImage.setOnClickListener {
            takePhoto()
        }
    }

    private fun initCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(Runnable {
            try {
                // Initialize the camera provider
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                // Create a Preview use case
                val preview = Preview.Builder().build()

                // Set up the image capture use case
                imageCapture = ImageCapture.Builder().build()

                // Unbind all use cases before binding them
                cameraProvider.unbindAll()

                // Bind the use cases to the camera
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )

            } catch (e: Exception) {
                // Handle any errors that occur during camera initialization
                Toast.makeText(this@CameraActivity, "Failed to open camera", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun createCustomTempFile(context: Context): File {
        val filesDir = context.externalCacheDir
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val currentTime = Calendar.getInstance().time
        val timeStamp = dateFormat.format(currentTime)
        return File.createTempFile(timeStamp, ".jpg", filesDir)
    }

    private fun init() {
        viewFinder = findViewById(R.id.viewFinder)
        captureImage = findViewById(R.id.captureImage)
        switchCamera = findViewById(R.id.switchCamera)

        apiService = RetrofitHelper.getInstance().create(ApiService::class.java)
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                Toast.makeText(this@CameraActivity, "Failed to open camera", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = createCustomTempFile(application)
        val outputOption = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOption,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Toast.makeText(this@CameraActivity, "Photo captured successfully", Toast.LENGTH_SHORT).show()

                    // Convert the captured photo to MultipartBody.Part
                    val imageRequestBody = outputFileResults.savedUri?.let {
                        val imageFile = File(it.path)
                        val imageFileRequestBody =
                            imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                        MultipartBody.Part.createFormData("image", imageFile.name, imageFileRequestBody)
                    }

                    // Now you can use 'imageRequestBody' in your API request or perform any other actions.
                    processCapturedImage(imageRequestBody)

                    // Close the camera activity or perform other actions as needed
                    finish()
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(this@CameraActivity, "Failed to capture photo", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun processCapturedImage(imageRequestBody: MultipartBody.Part?) {
        lifecycleScope.launch(Dispatchers.IO + coroutineJob) {
            try {
                val response = apiService.predict("${BuildConfig.X_API_Key}", imageRequestBody)
                if (response.isSuccessful) {
                    // Handle successful upload
                    Log.d("Upload", "Image uploaded successfully: ${response.body()}")
                } else {
                    // Handle upload failure
                    Log.e("Upload", "Image upload failed: ${response.message()}")
                }
            } catch (e: Exception) {
                // Handle exceptions
                Log.e("Upload", "Exception during image upload: $e")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!isFinishing) {
            coroutineScope.cancel()
        }
    }
}
