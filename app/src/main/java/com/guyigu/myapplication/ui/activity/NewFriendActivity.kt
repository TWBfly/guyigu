package com.guyigu.myapplication.ui.activity

import android.content.Intent
import androidx.activity.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.guyigu.myapplication.R
import com.guyigu.myapplication.base.BaseActivity
import com.guyigu.myapplication.bean.ApplyFriendBean
import com.guyigu.myapplication.bean.ApplyFriendData
import com.guyigu.myapplication.model.db.entity.FriendEntity
import com.guyigu.myapplication.ui.adapter.ApplyAdapter
import com.guyigu.myapplication.ui.viewmodel.FriendViewModel
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_newfriend.*

/**
 * Created by tang on 2020/10/23
 */
class NewFriendActivity : BaseActivity() {
    private lateinit var mContext: NewFriendActivity
    val model: FriendViewModel by viewModels()

    override fun contentLayoutId(): Int = R.layout.activity_newfriend

    override fun initView() {
        mContext = this
        title_name.text = "新的朋友"
        new_friend_recycle.layoutManager = LinearLayoutManager(this)
        val mAdapter = ApplyAdapter()
        new_friend_recycle.adapter = mAdapter

        //0 待通过
        model.friendApplyList(0).observe(mContext, {
            mAdapter.setNewInstance(it)
        })

        mAdapter.setOnItemChildClickListener { _, view, position ->
            if (view.id == R.id.item_agree) {
                //审核好友
                model.getCheckFriendApply(1, mAdapter.data[position].id).observe(mContext, {
                    //需要设置备注 暂无
                    //好友id，发消息
                    val friendEntity = FriendEntity()
                    mAdapter.data[position].apply {
                        friendEntity.id = id
                        friendEntity.phone = phone
                        friendEntity.name = name
                        friendEntity.img = img
                    }

                    db.friendDao().insertFriend(friendEntity)
                    startActivity(Intent(mContext, MainActivity::class.java))
                })
            }
        }
    }

    override fun initDestroy() {
    }
}