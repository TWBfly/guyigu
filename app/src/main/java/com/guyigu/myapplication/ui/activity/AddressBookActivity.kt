package com.guyigu.myapplication.ui.activity

import android.content.Intent
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.LogUtils
import com.guyigu.myapplication.R
import com.guyigu.myapplication.base.BaseActivity
import com.guyigu.myapplication.bean.ApplyFriendData
import com.guyigu.myapplication.ui.adapter.ApplyAdapter
import com.guyigu.myapplication.ui.viewmodel.FriendViewModel
import com.guyigu.myapplication.util.add_friend_id
import com.guyigu.myapplication.util.add_name
import kotlinx.android.synthetic.main.activity_address_book.*

/**
 * Created by tang on 2020/10/20
 * 通讯录
 */
class AddressBookActivity : BaseActivity() {
    private lateinit var mContext: AddressBookActivity
    private lateinit var mAdapter: ApplyAdapter
    val model: FriendViewModel by viewModels()

    override fun contentLayoutId(): Int = R.layout.activity_address_book

    override fun initView() {
        mContext = this

        mAdapter = ApplyAdapter()
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
        getFriendApplyList()
        getFriendList()

        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            if (view.id == R.id.item_agree) {
                //审核好友
                model.getCheckFriendApply(0, mAdapter.data[position].applyUserId).observe(mContext, {
                    showToast(it)
                })
            }
        }
    }

    private fun getFriendList(){
        model.getFriendList().observe(mContext,{
            LogUtils.e("getFriendList=="+it.size)

        })
    }

    /**
     * 好友申请列表
     */
    private fun getFriendApplyList() {
        //-1 已拒绝 0 待通过 1已经通过
        model.friendApplyList(1).observe(mContext, {
            mAdapter.setNewInstance(it)
        })

    }

    override fun initDestroy() {
    }
}