package com.inbedroom.pokedexter.core

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.inbedroom.pokedexter.BuildConfig
import com.inbedroom.pokedexter.data.pokemonservice.PokemonRepository
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

        fun <T : Any> getResultOrFailure(response: Response<T>): ResponseResult<T> {
            return if (response.isSuccessful && response.body() != null) {
                ResponseResult.Success(data = response.body()!!)
            } else {
                ResponseResult.Error(message = response.errorBody()?.string().orEmpty())
            }
        }
    }
}