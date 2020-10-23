package com.guyigu.myapplication.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.LogUtils
import com.guyigu.myapplication.R
import com.guyigu.myapplication.model.db.AppDatabase
import com.guyigu.myapplication.ui.activity.LoginActivity
import com.gyf.immersionbar.ImmersionBar
import com.hjq.toast.ToastUtils
import com.tencent.mmkv.MMKV
import io.rong.imkit.RongIM
import io.rong.imlib.RongIMClient
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

        RongIM.setConnectionStatusListener {
            if (it == RongIMClient.ConnectionStatusListener.ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT) {
                //当前用户账号在其他端登录，请提示用户并做出对应处理
                LogUtils.i("当前用户账号在其他端登录，请提示用户并做出对应处理=====$it")
                startActivity(Intent(this,LoginActivity::class.java))
            } else if (it == RongIMClient.ConnectionStatusListener.ConnectionStatus.CONN_USER_BLOCKED) {
                //用户被封禁，请提示用户并做出对应处理
                LogUtils.i("用户被封禁，请提示用户并做出对应处理=====$it")
            }

        }
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