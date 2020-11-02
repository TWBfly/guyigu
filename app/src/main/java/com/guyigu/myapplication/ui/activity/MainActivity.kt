package com.guyigu.myapplication.ui.activity

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.FragmentUtils
import com.blankj.utilcode.util.LogUtils
import com.guyigu.myapplication.R
import com.guyigu.myapplication.base.BaseActivity
import com.guyigu.myapplication.model.db.dao.FriendDao
import com.guyigu.myapplication.model.db.entity.FriendEntity
import com.guyigu.myapplication.ui.fragment.GuyiGuFragment
import com.guyigu.myapplication.ui.fragment.MessageFragment
import com.guyigu.myapplication.ui.fragment.MyFragment
import com.guyigu.myapplication.ui.viewmodel.FriendViewModel
import com.guyigu.myapplication.ui.vm.MyExtensionModule
import com.guyigu.myapplication.util.loginToken
import com.guyigu.myapplication.util.token_
import io.rong.imkit.DefaultExtensionModule
import io.rong.imkit.IExtensionModule
import io.rong.imkit.RongExtensionManager
import io.rong.imkit.RongIM
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.Group
import io.rong.imlib.model.UserInfo
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch


class MainActivity : BaseActivity() {
    private lateinit var mContext: MainActivity
    private var myFragment: MyFragment? = null
    private var guyiGuFragment: GuyiGuFragment? = null
    private var messageFragment: MessageFragment? = null
    val model: FriendViewModel by viewModels()

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
                    more_img.visibility = View.GONE
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
                    more_img.visibility = View.GONE
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

        //选择好友
        more_img.setOnClickListener {
            startActivity(Intent(mContext, SelectorFriendActivity::class.java))
        }

        LogUtils.e("Authorization=" + kv?.decodeString(loginToken))
    }

    private fun setTitle() {
        title_name.text = "聊天"
        address_book_img.visibility = View.VISIBLE
        more_img.visibility = View.VISIBLE
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
                    { num: Int ->
                        LogUtils.i("消息未读数量==$num")
                    }, Conversation.ConversationType.PRIVATE,
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

        val friendDao = db.friendDao()
        model.getFriendList().observe(mContext, {
            lifecycleScope.launch {
                for (data in it) {
                    val queryFriendById = friendDao.queryFriendById(data.id)
                    if (null == queryFriendById) {
                        val friendEntity = FriendEntity()
                        friendEntity.id = data.id
                        friendEntity.name = data.name
                        friendEntity.phone = data.phone
                        friendEntity.img = data.img
                        friendDao.insertFriend(friendEntity)
                    }
                }
                setRYUserInfo(friendDao)
            }
        })

//        setRYGroupInfo()

        registerExtensionPlugin()

    }

    /**
     * 设置群信息
     */
    private fun setRYGroupInfo() {
        RongIM.setGroupInfoProvider({ groupId ->
            //刷新群缓存
            //RongIM.getInstance().refreshGroupUserInfoCache(groupInfo)
            Group(groupId, "groupId 对应的名称", Uri.parse("groupId 对应的头像地址"))
        }, true)
    }

    /**
     * 设置用户信息
     */
    private fun setRYUserInfo(friendDao: FriendDao) {
        RongIM.setUserInfoProvider({ userId ->
            /**
             * 获取设置用户信息. 通过返回的 userId 来封装生产用户信息.
             * @param userId 用户 ID
             */

            val queryUserById = friendDao.queryFriendById(userId = userId.toInt())
            LogUtils.e("设置用户信息 userId==$userId==queryUserById==$queryUserById")
            if (queryUserById != null) {
                UserInfo(userId, queryUserById.name, Uri.parse(queryUserById.img))
                val userInfo = UserInfo(userId, queryUserById.name, Uri.parse(queryUserById.img))
                RongIM.getInstance().refreshUserInfoCache(userInfo)
            }
            null
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