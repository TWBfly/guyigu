package com.guyigu.myapplication.ui.activity

import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.guyigu.myapplication.R
import com.guyigu.myapplication.base.BaseActivity
import com.guyigu.myapplication.model.db.entity.FriendEntity
import com.guyigu.myapplication.ui.adapter.FriendListAdapter
import com.guyigu.myapplication.ui.viewmodel.FriendViewModel
import com.guyigu.myapplication.util.*
import io.rong.imkit.RongIM
import io.rong.imlib.model.Conversation
import kotlinx.android.synthetic.main.activity_address_book.*
import kotlinx.android.synthetic.main.activity_base.*

/**
 * Created by tang on 2020/10/20
 * 通讯录
 */
class AddressBookActivity : BaseActivity() {
    private lateinit var mContext: AddressBookActivity
    private lateinit var mAdapter: FriendListAdapter
    val model: FriendViewModel by viewModels()

    override fun contentLayoutId(): Int = R.layout.activity_address_book

    override fun initView() {
        mContext = this
        title_name.text = "通讯录"
        address_book_img.visibility = View.GONE
        mAdapter = FriendListAdapter()
        recycle_view.layoutManager = LinearLayoutManager(mContext)
        recycle_view.adapter = mAdapter

        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    model.searchFriend(query).observe(mContext, {
                        //搜索完 跳转
                        val intent = Intent(mContext, AddFriendActivity::class.java)
                        intent.putExtra(add_friend_id, it.id)
                        intent.putExtra(add_name, it.name)

                        startActivity(intent)
                    })
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        getFriendList()

        mAdapter.setOnItemClickListener { adapter, view, position ->
            val conversationType = Conversation.ConversationType.PRIVATE;
            val targetId = mAdapter.data[position].id.toString()
            val title = mAdapter.data[position].name
            val friendDao = db.friendDao()
            val queryFriendById = friendDao.queryFriendById(mAdapter.data[position].id)
            val friendEntity = FriendEntity()
            mAdapter.data[position].apply {
                friendEntity.id = id
                friendEntity.name = name
                friendEntity.img = img
                friendEntity.phone = phone
            }
            if (queryFriendById == null) {
                friendDao.insertFriend(friendEntity)
            } else {
                friendDao.updateFriend(friendEntity)
            }

            RongIM.getInstance().startConversation(mContext, conversationType, targetId, title, bundleOf(item_click_friend_id to friendEntity.id, item_click_friend_name to friendEntity.name, item_click_friend_img to friendEntity.img ))
        }

    }

    private fun getFriendList() {
        model.getFriendList().observe(mContext, {
            mAdapter.setNewInstance(it)
        })
    }

    override fun initDestroy() {
    }
}