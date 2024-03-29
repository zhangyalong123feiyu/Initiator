package com.lenovo.Initiator.protocol

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface DownloadApi {
    @Streaming                                                       // downloadApk
    @GET
    fun downloadApk(@Url url: String): Call<ResponseBody>
}