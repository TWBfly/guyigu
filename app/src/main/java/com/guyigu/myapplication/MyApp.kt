package com.guyigu.myapplication

import android.app.Application
import com.blankj.utilcode.util.LogUtils
import com.guyigu.myapplication.startup.LogStartup
import com.guyigu.myapplication.startup.MMKVStartup
import com.guyigu.myapplication.startup.ToastStartup
import com.rousetime.android_startup.StartupManager
import io.rong.imkit.RongIM
import io.rong.imlib.RongIMClient
import io.rong.imlib.RongIMClient.OnReceiveMessageWrapperListener
import io.rong.imlib.model.Message
import kotlin.properties.Delegates


/**
 * Created by tang on 2020/10/16
 */
class MyApp : Application() {
    companion object {
        var instance: MyApp by Delegates.notNull()
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        initStartup()
        initRongYun()
    }

    private fun initStartup() {
        StartupManager.Builder()
            .addStartup(LogStartup())
            .addStartup(ToastStartup())
            .addStartup(MMKVStartup())
            .build(this)
            .start()
            .await()
    }

    private fun initRongYun() {
        val appKey = "0vnjpoad0ingz"
        RongIM.init(this, appKey)

        RongIM.setOnReceiveMessageListener(object : OnReceiveMessageWrapperListener() {
            /**
             * 接收实时或者离线消息。
             * 注意:
             * 1. 针对接收离线消息时，服务端会将 200 条消息打成一个包发到客户端，客户端对这包数据进行解析。
             * 2. hasPackage 标识是否还有剩余的消息包，left 标识这包消息解析完逐条抛送给 App 层后，剩余多少条。
             * 如何判断离线消息收完：
             * 1. hasPackage 和 left 都为 0；
             * 2. hasPackage 为 0 标识当前正在接收最后一包（200条）消息，left 为 0 标识最后一包的最后一条消息也已接收完毕。
             *
             * @param message    接收到的消息对象
             * @param left       每个数据包数据逐条上抛后，还剩余的条数
             * @param hasPackage 是否在服务端还存在未下发的消息包
             * @param offline    消息是否离线消息
             * @return 是否处理消息。 如果 App 处理了此消息，返回 true; 否则返回 false 由 SDK 处理。
             */
            override fun onReceived(message: Message?, left: Int, hasPackage: Boolean, offline: Boolean): Boolean {
                LogUtils.i("message="+message+"==left=="+left+"==hasPackage=="+hasPackage+"==offline=="+offline)
                return false
            }
        })

        RongIM.setConnectionStatusListener {
            if (it == RongIMClient.ConnectionStatusListener.ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT) {
                //当前用户账号在其他端登录，请提示用户并做出对应处理
                LogUtils.i("当前用户账号在其他端登录，请提示用户并做出对应处理=====$it")
            } else if (it == RongIMClient.ConnectionStatusListener.ConnectionStatus.CONN_USER_BLOCKED) {
                //用户被封禁，请提示用户并做出对应处理
                LogUtils.i("用户被封禁，请提示用户并做出对应处理=====$it")
            }

        }
    }
}