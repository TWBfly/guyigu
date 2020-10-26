package com.guyigu.myapplication.ui.viewmodel

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.LogUtils
import com.guyigu.myapplication.bean.ApplyFriendData
import com.guyigu.myapplication.bean.Data
import com.guyigu.myapplication.bean.MyGroupList
import com.guyigu.myapplication.model.api.Api
import com.guyigu.myapplication.model.api.ApiService
import com.guyigu.myapplication.util.OK
import com.hjq.toast.ToastUtils
import kotlinx.coroutines.launch

/**
 * Created by tang on 2020/10/19
 */
class GroupViewModel : ViewModel() {

    private var api: Api = ApiService.instance.api
    val stringLiveData = MutableLiveData<String>()
    val groupListLiveData = MutableLiveData<MutableList<MyGroupList>>()

    /**
     * 创建群
     */
    fun addGroup(groupName: String): MutableLiveData<String> {
        viewModelScope.launch {
            api.addGroup(groupName).let {
                if (OK == it.msg) {
                    stringLiveData.postValue(OK)
                } else {
                    LogUtils.e("error=" + it.msg)
                    ToastUtils.show(it.msg)
                }
            }
        }
        return stringLiveData
    }

    /**
     * 我的创建的群组列表
     */
    fun getMyGroupList(): MutableLiveData<MutableList<MyGroupList>> {
        viewModelScope.launch {
            api.getMyGroupList().let {
                if (OK == it.msg) {
                    groupListLiveData.postValue(it.data)
                } else {
                    ToastUtils.show(it.msg)
                }
            }
        }
        return groupListLiveData
    }

    /**
     * 加入群组
     */
    fun joinGroup(groupId: Int, joinUserId: Int): MutableLiveData<String> {
        viewModelScope.launch {
            api.joinGroup(groupId, joinUserId).let {
                if (OK == it.msg) {
                    stringLiveData.postValue("加入成功")
                } else {
                    ToastUtils.show(it.msg)
                }
            }
        }
        return stringLiveData
    }


}