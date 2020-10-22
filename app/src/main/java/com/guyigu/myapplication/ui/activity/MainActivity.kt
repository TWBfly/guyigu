package com.guyigu.myapplication.ui.activity

import android.content.Intent
import android.net.Uri
import android.view.View
import com.blankj.utilcode.util.FragmentUtils
import com.blankj.utilcode.util.LogUtils
import com.guyigu.myapplication.R
import com.guyigu.myapplication.base.BaseActivity
import com.guyigu.myapplication.ui.fragment.GuyiGuFragment
import com.guyigu.myapplication.ui.fragment.MessageFragment
import com.guyigu.myapplication.ui.fragment.MyFragment
import com.guyigu.myapplication.ui.vm.MyExtensionModule
import com.guyigu.myapplication.util.loginToken
import com.guyigu.myapplication.util.token_
import io.rong.imkit.DefaultExtensionModule
import io.rong.imkit.IExtensionModule
import io.rong.imkit.RongExtensionManager
import io.rong.imkit.RongIM
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.UserInfo
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity(){
    private lateinit var mContext: MainActivity
    private var myFragment: MyFragment? = null
    private var guyiGuFragment: GuyiGuFragment? = null
    private var messageFragment: MessageFragment? = null

    override fun contentLayoutId(): Int = R.layout.activity_main

    override fun initView() {
        mContext = this
        setTitle()
        showFragment()
        nav_view.itemIconTintList = null
        nav_view.setOnNavigationItemSelectedListener { item ->
            hideFragments()
            when (item.itemId) {
                R.id.navigation_item1 -> {
                    setTitle()
                    showFragment()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_item2 -> {
                    title_name.text = "估一估"
                    address_book_img.visibility = View.GONE
                    if (guyiGuFragment == null) {
                        guyiGuFragment = GuyiGuFragment()
                        FragmentUtils.add(supportFragmentManager, guyiGuFragment!!, R.id.main_frameLayout)
                    } else {
                        FragmentUtils.show(guyiGuFragment!!)
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_item3 -> {
                    title_name.text = "我的"
                    address_book_img.visibility = View.GONE
                    if (myFragment == null) {
                        myFragment = MyFragment()
                        FragmentUtils.add(supportFragmentManager, myFragment!!, R.id.main_frameLayout)
                    } else {
                        FragmentUtils.show(myFragment!!)
                    }
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }

        initRY()

        //跳转通讯录
        address_book_img.setOnClickListener {
            startActivity(Intent(mContext, AddressBookActivity::class.java))
        }

        LogUtils.e("Authorization=" + kv?.decodeString(loginToken))
    }

    private fun setTitle() {
        title_name.text = "聊天"
        address_book_img.visibility = View.VISIBLE

        address_book_img.setOnClickListener {
            startActivity(Intent(mContext,AddressBookActivity::class.java))
        }
    }

    private fun initRY() {
        val token = kv?.decodeString(token_)
        RongIM.connect(token, object : RongIMClient.ConnectCallbackEx() {
            override fun onSuccess(value: String?) {
                LogUtils.i("success=$value")
                RongIM.getInstance().enableNewComingMessageIcon(true);//显示新消息提醒
                RongIM.getInstance().enableUnreadMessageIcon(true);//显示未读消息数目

//                RongIMClient.getInstance().getTotalUnreadCount(object : RongIMClient.ResultCallback<Int>() {
//                    override fun onSuccess(num: Int?) {
//                        LogUtils.i("消息未读数量==$num")
//                    }
//
//                    override fun onError(errorCode: RongIMClient.ErrorCode?) {
//                        LogUtils.i("消息未读数量error==${errorCode?.value} ${errorCode?.message}")
//                    }
//
//                })

                RongIM.getInstance().addUnReadMessageCountChangedObserver(
                    { num: Int -> LogUtils.i("消息未读数量==$num") }, Conversation.ConversationType.PRIVATE,
                    Conversation.ConversationType.GROUP, Conversation.ConversationType.SYSTEM,
                    Conversation.ConversationType.PUBLIC_SERVICE, Conversation.ConversationType.APP_PUBLIC_SERVICE
                )

            }

            override fun onError(errorCode: RongIMClient.ErrorCode?) {
                LogUtils.i("onError=$errorCode")
            }

            override fun onTokenIncorrect() {
                LogUtils.i("onTokenIncorrect==")
            }

            override fun OnDatabaseOpened(db: RongIMClient.DatabaseOpenStatus?) {
                LogUtils.i("OnDatabaseOpened==$db")
            }
        })

//        setRYUserInfo()

        registerExtensionPlugin()

    }

    /**
     * 设置用户信息
     */
    private fun setRYUserInfo() {
        val userDao = db.userDao()
        RongIM.setUserInfoProvider({ userId ->
            /**
             * 获取设置用户信息. 通过返回的 userId 来封装生产用户信息.
             * @param userId 用户 ID
             */
            LogUtils.e("userId==$userId")
            val queryUserById = userDao.queryUserById(userId = userId.toInt())
            LogUtils.e("queryUserById==${queryUserById?.name}")
            UserInfo(userId, queryUserById?.name, Uri.parse(queryUserById?.img))
        }, true)

    }

    /**
     * 设置会话 +号 模块
     */
    private fun registerExtensionPlugin() {
        val moduleList = RongExtensionManager.getInstance().extensionModules
        var defaultModule: IExtensionModule? = null
        if (moduleList != null) {
            for (module in moduleList) {
                if (module is DefaultExtensionModule) {
                    defaultModule = module
                    break
                }
            }
            if (defaultModule != null) {
                RongExtensionManager.getInstance().unregisterExtensionModule(defaultModule)
                RongExtensionManager.getInstance().registerExtensionModule(MyExtensionModule())
            }
        }
    }

    private fun showFragment() {
        if (messageFragment == null) {
            messageFragment = MessageFragment()
            FragmentUtils.add(supportFragmentManager, messageFragment!!, R.id.main_frameLayout)
        } else {
            FragmentUtils.show(messageFragment!!)
        }
    }

    fun hideFragments() {
        if (messageFragment != null) FragmentUtils.hide(messageFragment!!)
        if (guyiGuFragment != null) FragmentUtils.hide(guyiGuFragment!!)
        if (myFragment != null) FragmentUtils.hide(myFragment!!)
    }

    override fun initDestroy() {
    }
}