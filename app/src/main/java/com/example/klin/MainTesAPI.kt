package com.example.klin

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.retrofitexample.`ApiService-test`
import com.example.retrofitexample.`RetrofitHelper-test`
import com.google.gson.JsonObject
import kotlinx.coroutines.launch

class MainTesAPI: AppCompatActivity(){
    private lateinit var apiService: `ApiService-test`
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tes_api)
        apiService = `RetrofitHelper-test`.getInstance().create(`ApiService-test`::class.java)

        findViewById<Button>(R.id.btnGet).setOnClickListener{
            getUserByID()
        }

        findViewById<Button>(R.id.btnUpdate).setOnClickListener {
            updateUser()
        }

        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            deleteUser()
        }

        findViewById<Button>(R.id.btnPost).setOnClickListener {
            createUser()
        }

    }

    private fun createUser() {
        lifecycleScope.launch {
            showLoading("Creating, Please Wait......")
            val body = JsonObject().apply {
                addProperty("name", "morpheus")
                addProperty("job", "leader")
            }
            val result = apiService.createUser(body)
            if(result.isSuccessful){
                Log.e("testes", "CreateUser Success: ${result.body()}")
            }else{
                Log.e("testes", "CreateUser Failed: ${result.message()}")
            }
            progressDialog?.dismiss()
        }
    }

    private fun deleteUser() {
        lifecycleScope.launch {
            showLoading("Deleting, Please Wait.....")
            val result = apiService.deleteUser("2")
            if (result.isSuccessful){
                Log.e("testes", "DeleteUser Success: ${result.body()}")
            }else{
                Log.e("testes", "DeleteUser Failed: ${result.message()}")
            }
            progressDialog?.dismiss()
        }
    }

    private fun updateUser() {
        lifecycleScope.launch {
            showLoading("Updating, Please Wait.....")
            val body = JsonObject().apply {
                addProperty("name", "morpheus")
                addProperty("job", "zion resident")
            }
            val result = apiService.updateUser("2",body)
            if (result.isSuccessful) {
                Log.e("testes", "UpdateUser Success: ${result.body()}")
            }else{
                Log.e("testes", "UpdateUser Failed: ${result.message()}")
            }
            progressDialog?.dismiss()
        }
    }

    private fun getUserByID() {
        lifecycleScope.launch {
            showLoading("Getting, Please Wait....")
            val result = apiService.getUserByID("2")
            if (result.isSuccessful){
                Log.e("testes", "getUserByID Success: ${result.body()?.data}")
            }else{
                Log.e("testes", "getUserByID Failed: ${result.message()}")
            }
            progressDialog?.dismiss()
        }
    }

    private fun showLoading(msg:String){
        progressDialog = ProgressDialog.show(this,null, msg, true)
    }

}