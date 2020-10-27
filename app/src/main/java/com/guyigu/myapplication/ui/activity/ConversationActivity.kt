package com.guyigu.myapplication.ui.activity

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.blankj.utilcode.util.BusUtils
import com.blankj.utilcode.util.BusUtils.Bus
import com.blankj.utilcode.util.LogUtils
import com.guyigu.myapplication.R
import com.guyigu.myapplication.base.BaseActivity
import com.guyigu.myapplication.bean.ItemClickDataBean
import com.guyigu.myapplication.util.itemClickData
import com.guyigu.myapplication.util.item_click_friend_id
import com.guyigu.myapplication.util.item_click_friend_img
import com.guyigu.myapplication.util.item_click_friend_name
import io.rong.imkit.fragment.ConversationFragment
import kotlinx.android.synthetic.main.activity_base.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * Created by tang on 2020/10/19
 */
class ConversationActivity : BaseActivity() {
    private lateinit var mContext: ConversationActivity
    private var id: String? = ""
    private var name: String? = ""
    private var img: String? = ""

    override fun contentLayoutId(): Int = R.layout.activity_conversation

    override fun initView() {
        mContext = this
        // 添加会话界面
        val conversationFragment = ConversationFragment()
        val manager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = manager.beginTransaction()
        transaction.replace(R.id.container, conversationFragment)
        transaction.commit()

        intent.apply {
            id = getStringExtra(item_click_friend_id)
            name = getStringExtra(item_click_friend_name)
            img = getStringExtra(item_click_friend_img)
        }
        title_name.text = name
        if (!EventBus.getDefault().isRegistered(mContext))
            EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onMessageEvent(event: ItemClickDataBean) {
        title_name.text = event.userName
    }

    override fun initDestroy() {
        EventBus.getDefault().unregister(this);
    }
}