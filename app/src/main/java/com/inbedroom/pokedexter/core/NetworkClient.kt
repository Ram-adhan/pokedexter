package com.inbedroom.pokedexter.core

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.inbedroom.pokedexter.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NetworkClient {
    companion object {
        private const val BASE_URL = "https://pokeapi.co/api/v2/"

        fun provideRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(createClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        private fun createClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(20, TimeUnit.SECONDS)
                .callTimeout(20, TimeUnit.SECONDS)
                .build()
        }

        private val loggingInterceptor = HttpLoggingInterceptor().apply {
            this.level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

        fun <T : Any> processError(response: Response<T>): Throwable {
            val body = response.errorBody()?.string()
            return if (body.isNullOrBlank()) {
                Throwable("Unknown Error")
            } else {
                val typeToken = object : TypeToken<Map<String, String>>() {}.type
                val mapper = Gson().fromJson<Map<String, String>>(body, typeToken)
                mapper.keys.firstOrNull()?.let { key ->
                    Throwable("$key : ${mapper[key]}")
                } ?: Throwable("Unknown Error")
            }
        }
    }
}