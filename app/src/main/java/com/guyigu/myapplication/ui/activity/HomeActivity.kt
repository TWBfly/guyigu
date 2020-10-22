package com.guyigu.myapplication.ui.activity

import android.content.Intent
import com.guyigu.myapplication.R
import com.guyigu.myapplication.base.BaseActivity
import com.guyigu.myapplication.util.token_

/**
 * Created by tang on 2020/10/19
 */
class HomeActivity : BaseActivity() {
    private lateinit var mContext: HomeActivity

    override fun contentLayoutId(): Int = R.layout.activity_home

    override fun initView() {
        mContext = this
        val token = kv?.decodeString(token_)
        if (token.isNullOrBlank()) {
            startActivity(Intent(mContext, LoginActivity::class.java))
        } else {
            startActivity(Intent(mContext, MainActivity::class.java))
        }
        finish()
    }

    override fun initDestroy() {
    }
}