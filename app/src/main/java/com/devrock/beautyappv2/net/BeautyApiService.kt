package com.devrock.beautyappv2.net

import com.devrock.beautyappv2.BuildConfig
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "https://beauty.judoekb.ru/api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

val logging = HttpLoggingInterceptor()
val httpClient = OkHttpClient.Builder().addInterceptor(logging.apply {
    level = if(BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
}).build()



private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .client(httpClient)
    .build()

interface BeautyApiService {

    @POST("auth/send_code")
    fun authSendCode(
        @Query("phone") phone: String
    ): Deferred<Status>

    @POST("auth/confirm")
    fun authPhoneConfirm(
        @Body   body: AuthConfirmBody
    ): Deferred<SessionResponse>

    @POST("account/update")
    fun accountUpdate(
        @Header("Authorization") session: String,
        @Body body: AccountBody
    ): Deferred<Status>

    @POST("account/setPhoto")
    fun accountSetPhoto(
        @HeaderMap headers: Map<String, String>,
        @Body body: String
    ): Deferred<Status>

    @GET("salons/list")
    fun getSalonsList(
        @Header("Authorization") session: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double,
        @Query("order") order: String
    ): Deferred<SalonListResponse>
}

object BeautyApi {
    val retrofitService: BeautyApiService by lazy {
        retrofit.create(BeautyApiService::class.java)
    }
}