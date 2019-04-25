package com.lenovo.Initiator.utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitUtils {

    companion object {
        private var retrofit: Retrofit? = null
        fun getRetrofit(): Retrofit {
            if (retrofit == null) {
                synchronized(RetrofitUtils::class.java) {
                    if (retrofit == null) {
                        // retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl("").build()
                    }
                }
            }
            return retrofit!!
        }

        fun <T> createRequest(clas: Class<T>): T {
            var clas = getRetrofit().create(clas)
            return clas
        }
    }

}