package com.guyigu.myapplication.model.api

/**
 * Created by tang on 2020/10/18
 */
class ApiService private constructor() {
     val api: Api = RetrofitFactory.instance.create(Api::class.java)

    private object ApiServiceHolder {
        val INSTANCE = ApiService()
    }

    companion object {
        val instance: ApiService
            get() = ApiServiceHolder.INSTANCE
    }

}