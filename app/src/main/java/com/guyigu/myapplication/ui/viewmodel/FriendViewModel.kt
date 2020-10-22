package com.guyigu.myapplication.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guyigu.myapplication.bean.ApplyFriendData
import com.guyigu.myapplication.bean.Data
import com.guyigu.myapplication.model.api.Api
import com.guyigu.myapplication.model.api.ApiService
import com.guyigu.myapplication.util.OK
import com.hjq.toast.ToastUtils
import kotlinx.coroutines.launch

/**
 * Created by tang on 2020/10/19
 */
class FriendViewModel : ViewModel() {
    private var api: Api
    val liveData = MutableLiveData<Data>()
    val addFriendLiveData = MutableLiveData<String>()
    val friendApplyList = MutableLiveData<MutableList<ApplyFriendData>>()
    val myFriendList = MutableLiveData<MutableList<Data>>()

    init {
        api = ApiService.instance.api
    }

    /**
     * 搜索好友
     */
    fun searchFriend(phone: String): MutableLiveData<Data> {
        viewModelScope.launch {
            api.searchPhone(phone).let {
                if (OK == it.msg) {
                    liveData.postValue(it.data)
                }
            }
        }
        return liveData
    }

    /**
     * 添加好友
     */
    fun addFriend(remark: String, userId: Int): MutableLiveData<String> {
        viewModelScope.launch {
            api.addFriend(remark, userId).let {
                if (OK == it.msg) {
                    addFriendLiveData.postValue("添加成功")
                } else {
                    ToastUtils.show(it.msg)
                }
            }
        }
        return addFriendLiveData
    }

    /**
     * 获取好友申请列表
     * -1 已拒绝 0 待通过 1已经通过
     */
    fun friendApplyList(type: Int): MutableLiveData<MutableList<ApplyFriendData>> {
        viewModelScope.launch {
            api.getFriendApplyList(type).let {
                friendApplyList.postValue(it.data)
            }
        }
        return friendApplyList
    }

    /**
     * 好友申请审核
     * -1 已拒绝 1已经通过
     */
    fun getCheckFriendApply(type: Int, userApplyRecordId: Int): MutableLiveData<String> {
        viewModelScope.launch {
            api.getCheckFriendApply(type, userApplyRecordId).let {
                if (OK == it.msg) {
                    addFriendLiveData.postValue("添加成功")
                } else {
                    ToastUtils.show(it.msg)
                }
            }
        }
        return addFriendLiveData
    }

    /**
     * 好友列表
     */
    fun getFriendList(): MutableLiveData<MutableList<Data>> {
        viewModelScope.launch {
            api.getMyFriendList().let {
                if (OK == it.msg) {
                    myFriendList.postValue(it.data)
                }
            }
        }
        return myFriendList
    }

    /**
     * 删除好友
     */
    fun delFriend(userId: Int): MutableLiveData<String> {
        viewModelScope.launch {
            api.delFriend(userId).let {
                if (OK == it.msg) {
                    addFriendLiveData.postValue("删除成功")
                }
            }
        }
        return addFriendLiveData
    }


}