package com.guyigu.myapplication.startup

import android.app.Application
import android.content.Context
import com.hjq.toast.ToastUtils
import com.rousetime.android_startup.AndroidStartup

/**
 * Created by tang on 2020/10/18
 */
class ToastStartup : AndroidStartup<String>() {
    override fun callCreateOnMainThread(): Boolean = true

    override fun create(context: Context): String? {
        ToastUtils.init(context as Application)
        return this.javaClass.simpleName
    }

    override fun waitOnMainThread(): Boolean = false
}