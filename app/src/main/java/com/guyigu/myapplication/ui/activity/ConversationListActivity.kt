package com.guyigu.myapplication.ui.activity

import android.net.Uri
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.guyigu.myapplication.R
import com.guyigu.myapplication.base.BaseActivity
import io.rong.imkit.fragment.ConversationListFragment
import io.rong.imlib.model.Conversation

/**
 * Created by tang on 2020/10/27
 */
class ConversationListActivity : BaseActivity() {
    override fun contentLayoutId(): Int = R.layout.activity_conversation_list

    override fun initView() {
        val conversationListFragment = ConversationListFragment()
        // 此处设置 Uri. 通过 appendQueryParameter 去设置所要支持的会话类型. 例如
        // .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(),"false")
        // 表示支持单聊会话, false 表示不聚合显示, true 则为聚合显示
        val uri = Uri.parse("rong://" + applicationInfo.packageName).buildUpon()
            .appendPath("conversationlist")
            .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
            .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false") //群组
            .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false") //公共服务号
            .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false") //订阅号
            .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true") //系统
            .build()

        conversationListFragment.uri = uri
        val manager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = manager.beginTransaction()
        transaction.replace(R.id.container, conversationListFragment)
        transaction.commit()
    }

    override fun initDestroy() {
    }
}