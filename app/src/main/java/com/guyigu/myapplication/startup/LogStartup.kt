package com.guyigu.myapplication.startup

import android.content.Context
import com.blankj.utilcode.util.LogUtils
import com.guyigu.myapplication.BuildConfig
import com.rousetime.android_startup.AndroidStartup

/**
 * Created by tang on 2020/10/18
 */
class LogStartup : AndroidStartup<String>() {
    override fun callCreateOnMainThread(): Boolean = false

    override fun create(context: Context): String? {
        LogUtils.getConfig().apply {
            isLogSwitch = BuildConfig.DEBUG
            globalTag = "twb"
        }
        return this.javaClass.simpleName
    }

    override fun waitOnMainThread(): Boolean = false
}