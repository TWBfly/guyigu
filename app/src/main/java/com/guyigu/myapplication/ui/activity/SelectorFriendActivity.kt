package com.guyigu.myapplication.ui.activity

import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.LogUtils
import com.guyigu.myapplication.R
import com.guyigu.myapplication.base.BaseActivity
import com.guyigu.myapplication.ui.adapter.FriendListAdapter
import com.guyigu.myapplication.ui.viewmodel.FriendViewModel
import com.guyigu.myapplication.ui.viewmodel.GroupViewModel
import io.rong.imkit.RongIM
import io.rong.imlib.IRongCallback
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.Message
import io.rong.message.TextMessage
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_selector_friend.*

/**
 * Created by tang on 2020/10/26
 */
class SelectorFriendActivity : BaseActivity() {
    private lateinit var mAdapter: FriendListAdapter
    private lateinit var mContext: SelectorFriendActivity
    val model: FriendViewModel by viewModels()
    val mModel: GroupViewModel by viewModels()

    override fun contentLayoutId(): Int = R.layout.activity_selector_friend

    override fun initView() {
        mContext = this
        right_tv.visibility = View.VISIBLE
        title_name.text = "选择好友"
        select_friend_recycle.layoutManager = LinearLayoutManager(mContext)
        mAdapter = FriendListAdapter()
        select_friend_recycle.adapter = mAdapter

        //确定发起群聊
        right_tv.setOnClickListener {
            LogUtils.e("==确定发起群聊==")
        }

        initData()
    }


    private fun initData() {
        model.getFriendList().observe(mContext, {
            mAdapter.setNewInstance(it)
        })

        //创建群
        mModel.addGroup("123456").observe(mContext, {
            //获取我创建的群组
            mModel.getMyGroupList().observe(mContext, {
                val groupId = it[it.size - 1].groupId
                //加入群组
                mModel.joinGroup(groupId, 2).observe(mContext, {
                    sendMessage(groupId)
                })

            })
        })


    }

    private fun sendMessage(groupId: Int) {
        val content = "建群成功=="

        val conversationType = Conversation.ConversationType.GROUP
        val targetId: String = groupId.toString()

        val messageContent = TextMessage.obtain(content)
        val message = Message.obtain(targetId, conversationType, messageContent)
        RongIM.getInstance().sendMessage(message, null, null, object : IRongCallback.ISendMessageCallback {
            /**
             * 消息发送前回调, 回调时消息已存储数据库
             * @param message 已存库的消息体
             */
            override fun onAttached(message: Message) {
                LogUtils.e("群聊 onAttached Message=$message")
            }

            /**
             * 消息发送成功。
             * @param message 发送成功后的消息体
             */
            override fun onSuccess(message: Message) {
                LogUtils.e("群聊 onSuccess Message=$message")
            }

            /**
             * 消息发送失败
             * @param message   发送失败的消息体
             * @param errorCode 具体的错误
             */
            override fun onError(message: Message, errorCode: RongIMClient.ErrorCode) {
                LogUtils.e("群聊 onError Message=$message errorCode=$errorCode")
            }
        })

    }


    override fun initDestroy() {
    }
}