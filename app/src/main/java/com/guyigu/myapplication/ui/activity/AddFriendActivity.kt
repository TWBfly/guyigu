package com.guyigu.myapplication.ui.activity

import androidx.activity.viewModels
import com.blankj.utilcode.util.LogUtils
import com.guyigu.myapplication.R
import com.guyigu.myapplication.base.BaseActivity
import com.guyigu.myapplication.ui.viewmodel.FriendViewModel
import com.guyigu.myapplication.util.add_friend_id
import com.guyigu.myapplication.util.add_name
import kotlinx.android.synthetic.main.activity_addfriend.*
import kotlinx.android.synthetic.main.activity_base.*

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
        title_name.text = "添加新朋友"
        intent.apply {
            friendId = getIntExtra(add_friend_id, 0)
            name_ = getStringExtra(add_name).toString()
        }

        name_tv.text = name_
        remark.setText(name_)

        val friendDao = db.friendDao()
        val queryFriendById = friendDao.queryFriendById(friendId)
        LogUtils.i("queryFriendById="+queryFriendById)
//        if (null == queryFriendById) {
//            add_btn.text = "添加通信录"
//            add_btn.setOnClickListener {
//                addFriend()
//            }
//        } else {
//            add_btn.text = "发送消息"
//        }
        add_btn.text = "添加通信录"
        add_btn.setOnClickListener {
            addFriend()
        }
    }

    private fun addFriend() {
        model.addFriend(remark.text.toString(), friendId).observe(mContext, {
            model.testMsg().observe(mContext, {
                showToast(it)
                finish()
            })
        })
    }

    override fun initDestroy() {
    }
}