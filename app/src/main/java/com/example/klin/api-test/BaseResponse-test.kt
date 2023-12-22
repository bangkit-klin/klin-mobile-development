package com.example.retrofitexample

import com.google.gson.annotations.SerializedName

data class `BaseResponse-test`<T> (
    @SerializedName("data")
    var data: T? = null
)