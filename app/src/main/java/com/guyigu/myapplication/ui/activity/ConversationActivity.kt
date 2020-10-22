package com.guyigu.myapplication.ui.activity

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.guyigu.myapplication.R
import com.guyigu.myapplication.base.BaseActivity
import io.rong.imkit.fragment.ConversationFragment

/**
 * Created by tang on 2020/10/19
 */
class ConversationActivity : BaseActivity() {
    override fun contentLayoutId(): Int = R.layout.activity_conversation

    override fun initView() {
        // 添加会话界面
        val conversationFragment = ConversationFragment()
        val manager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = manager.beginTransaction()
        transaction.replace(R.id.container, conversationFragment)
        transaction.commit()
    }

    override fun initDestroy() {
    }
}