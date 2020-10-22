package com.guyigu.myapplication.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.guyigu.myapplication.MyApp
import com.guyigu.myapplication.R
import com.guyigu.myapplication.model.db.AppDatabase
import com.guyigu.myapplication.model.manager.AppManager
import com.gyf.immersionbar.ImmersionBar
import com.hjq.toast.ToastUtils
import com.tencent.mmkv.MMKV
import kotlinx.android.synthetic.main.activity_base.*


/**
 * Created by tang on 2020/10/17
 */
abstract class BaseActivity : AppCompatActivity() {

    protected lateinit var db: AppDatabase
    var kv: MMKV? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        frame_layout.addView(LayoutInflater.from(this).inflate(contentLayoutId(), null))
        db = AppDatabase.getInstance(this)
        kv = MMKV.defaultMMKV()
        initView()
        initImmersionBar()
//        AppManager.instance.addActivity(this)
    }

    abstract fun contentLayoutId(): Int

    abstract fun initView()

    abstract fun initDestroy()

    fun showToast(s: String) {
        ToastUtils.show(s)
    }

    private fun initImmersionBar() {
        ImmersionBar.with(this).init()
        if (ImmersionBar.isSupportStatusBarDarkFont()) {
            ImmersionBar.with(this).statusBarDarkFont(true).fitsSystemWindows(true).init()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        initDestroy()
//        AppManager.instance.finishAllActivity()
    }
}