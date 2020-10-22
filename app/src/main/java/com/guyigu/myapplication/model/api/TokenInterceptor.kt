package com.guyigu.myapplication.model.api

import com.blankj.utilcode.util.LogUtils
import com.guyigu.myapplication.util.loginToken
import com.tencent.mmkv.MMKV
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * Created by tang on 2020/10/20
 */
class TokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = MMKV.defaultMMKV().decodeString(loginToken)
        return if (token.isNullOrEmpty()) {
            val originalRequest: Request = chain.request()
            chain.proceed(originalRequest)
        } else {
            LogUtils.i("TokenInterceptor==$token")
            val originalRequest = chain.request()
            val updateRequest = originalRequest.newBuilder().header("Authorization", token).build()
            chain.proceed(updateRequest)
        }
    }
}