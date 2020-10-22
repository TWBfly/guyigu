package com.guyigu.myapplication.model.api

import com.blankj.utilcode.util.GsonUtils
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.guyigu.myapplication.MyApp
import com.guyigu.myapplication.util.BaseUrl
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by tang on 2020/10/18
 */
class RetrofitFactory private constructor() {
    companion object {
        val instance: RetrofitFactory by lazy { RetrofitFactory() }
    }

    private val interceptor: Interceptor
    private val retrofit: Retrofit

    init {
        //通用拦截
        interceptor = Interceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .removeHeader("Content-Type")
                .header("Content-Type", "application/json")
                .addHeader("charset", "UTF-8")
                .addHeader("Authorization", "")
                .method(chain.request().method, chain.request().body)
                .build()

            chain.proceed(request)
        }

        //Retrofit实例化
        retrofit = Retrofit.Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(initClient())
            .build()
    }

    private fun initClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(initLogInterceptor())
            .addInterceptor(interceptor)
            .addInterceptor(ChuckerInterceptor(MyApp.instance))
            .addNetworkInterceptor(TokenInterceptor())
            .retryOnConnectionFailure(true) //设置重连
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    /**
     * 日志拦截器
     */
    private fun initLogInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    /**
     *  具体服务实例化
     */
    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }

    fun createJsonRequest(paramsMap: HashMap<String, Any>): RequestBody {
        return GsonUtils.toJson(paramsMap).toRequestBody("application/json;charset=utf-8".toMediaType())
    }
}