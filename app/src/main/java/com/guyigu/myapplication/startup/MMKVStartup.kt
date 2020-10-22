package com.guyigu.myapplication.startup

import android.content.Context
import com.blankj.utilcode.util.LogUtils
import com.guyigu.myapplication.BuildConfig
import com.rousetime.android_startup.AndroidStartup
import com.tencent.mmkv.MMKV

/**
 * Created by tang on 2020/10/18
 */
class MMKVStartup : AndroidStartup<String>() {
    override fun callCreateOnMainThread(): Boolean = false

    override fun create(context: Context): String? {
        MMKV.initialize(context)
        return this.javaClass.simpleName
    }

    override fun waitOnMainThread(): Boolean = false
}