package com.guyigu.myapplication.ui.activity

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.guyigu.myapplication.R
import com.guyigu.myapplication.base.BaseActivity
import io.rong.imkit.fragment.SubConversationListFragment

/**
 * Created by tang on 2020/10/23
 */
class SubConversationListActivity : BaseActivity() {
    override fun contentLayoutId(): Int = R.layout.activity_subconversation_list

    override fun initView() {
        val subconversationListFragment = SubConversationListFragment()
        val manager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = manager.beginTransaction()
        transaction.replace(R.id.container, subconversationListFragment)
        transaction.commit()
    }

    override fun initDestroy() {
    }
}