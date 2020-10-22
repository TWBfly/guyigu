package com.guyigu.myapplication.ui.activity

import com.blankj.utilcode.util.LogUtils
import com.guyigu.myapplication.R
import com.guyigu.myapplication.base.BaseActivity
import com.guyigu.myapplication.ui.vm.RedMessage
import io.rong.imkit.RongIM
import io.rong.imlib.IRongCallback
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.Message
import kotlinx.android.synthetic.main.activity_redset.*

/**
 * Created by tang on 2020/10/22
 */
class RedSetActivity : BaseActivity() {
    override fun contentLayoutId(): Int = R.layout.activity_redset

    override fun initView() {

        send_red.setOnClickListener {
            val conversationType = Conversation.ConversationType.PRIVATE
            val targetId = "2"
            // PushContent 内容, 不可为 null
            // PushContent 内容, 不可为 null
            val pushContent = "=pushContent自定义消息="
            val pushData = "=pushData自定义消息="
            val messageContent = RedMessage.obtain(targetId, "222", "http://www.baidu.com", "1", "liming", "=测试自定义消息==")
            val message = Message.obtain(targetId, conversationType, messageContent)
            RongIM.getInstance().sendMessage(message,
                pushContent, pushData, object : IRongCallback.ISendMessageCallback {
                    override fun onAttached(message: Message) {
                        LogUtils.i("onAttached==$message")
                    }

                    override fun onSuccess(message: Message) {
                        LogUtils.i("onSuccess==$message")
                        finish()
                    }

                    override fun onError(message: Message, errorCode: RongIMClient.ErrorCode) {
                        LogUtils.i("onError==$message==errorCode=$errorCode")
                    }
                })
        }

    }

    override fun initDestroy() {
    }
}