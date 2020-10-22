package com.guyigu.myapplication.ui.activity

import android.content.Intent
import androidx.activity.viewModels
import com.blankj.utilcode.util.LogUtils
import com.guyigu.myapplication.R
import com.guyigu.myapplication.base.BaseActivity
import com.guyigu.myapplication.ui.viewmodel.LoginViewModel
import com.guyigu.myapplication.util.token_
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Created by tang on 2020/10/19
 */
class LoginActivity : BaseActivity() {
    private lateinit var mContext: LoginActivity

    override fun contentLayoutId(): Int = R.layout.activity_login

    override fun initView() {
        mContext = this

        val model: LoginViewModel by viewModels()
        login_btn.setOnClickListener {
            model.login(name.text.toString(),password.text.toString()).observe(this, {
                kv?.encode(token_, it)
                LogUtils.e("token==$it")
                startActivity(Intent(mContext, MainActivity::class.java))
            })
        }
    }

    override fun initDestroy() {
    }
}