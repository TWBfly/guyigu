package com.guyigu.myapplication.ui.activity

import androidx.activity.viewModels
import com.blankj.utilcode.util.LogUtils
import com.guyigu.myapplication.R
import com.guyigu.myapplication.base.BaseActivity
import com.guyigu.myapplication.ui.viewmodel.FriendViewModel
import com.guyigu.myapplication.util.add_friend_id
import com.guyigu.myapplication.util.add_name
import kotlinx.android.synthetic.main.activity_addfriend.*

/**
 * Created by tang on 2020/10/20
 */
class AddFriendActivity : BaseActivity() {
    private lateinit var mContext: AddFriendActivity
    private var friendId: Int = 0
    private var name_: String = ""
    val model: FriendViewModel by viewModels()

    override fun contentLayoutId(): Int = R.layout.activity_addfriend

    override fun initView() {
        mContext = this

        intent.apply {
            friendId = getIntExtra(add_friend_id, 0)
            name_ = getStringExtra(add_name).toString()
        }

        name_tv.text = name_
        val friendDao = db.friendDao()
        val queryFriendById = friendDao.queryFriendById(friendId)
        if (null == queryFriendById) {
            add_btn.text = "添加通信录"
            add_btn.setOnClickListener {
                addFriend()
            }
        } else {
            add_btn.text = "发送消息"
        }
    }

    private fun addFriend() {
        model.addFriend("这是测试备注", friendId).observe(mContext, {
            LogUtils.e("ss" + it)
            showToast(it)
            finish()
        })
    }

    override fun initDestroy() {
    }
}