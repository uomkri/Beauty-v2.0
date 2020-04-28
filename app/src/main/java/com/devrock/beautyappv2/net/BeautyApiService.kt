package com.devrock.beautyappv2.net

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.POST
import retrofit2.http.Query

private const val BASE_URL = "https://beauty.judoekb.ru/api"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface BeautyApiService {
    @POST("send_code")
    fun sendCode(
        @Query("phone") phone: String
    ): Deferred<Status>
}

object BeautyApi {
    val retrofitService: BeautyApiService by lazy {
        retrofit.create(BeautyApiService::class.java)
    }
}