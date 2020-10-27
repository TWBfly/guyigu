package com.guyigu.myapplication.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.LogUtils
import com.guyigu.myapplication.MyApp
import com.guyigu.myapplication.model.api.ApiService
import com.guyigu.myapplication.model.db.AppDatabase
import com.guyigu.myapplication.model.db.entity.FriendEntity
import com.guyigu.myapplication.model.db.entity.UserEntity
import com.guyigu.myapplication.util.OK
import com.guyigu.myapplication.util.loginToken
import com.hjq.toast.ToastUtils
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * Created by tang on 2020/10/19
 */
class LoginViewModel : ViewModel() {
    val liveData = MutableLiveData<String>()
    fun login(name_: String, password: String): MutableLiveData<String> {
        viewModelScope.launch {
            ApiService.instance.api.login(mapOf("userName" to name_, "passWord" to password)).let {
                if (OK == it.msg) {
                    val userDao = AppDatabase.getInstance(MyApp.instance).userDao()
                    val friendDao = AppDatabase.getInstance(MyApp.instance).friendDao()
                    val userEntity = UserEntity()
                    val friendEntity = FriendEntity()
                    it.data?.apply {
                        userEntity.let { it_ ->
                            it_.id = id
                            it_.img = img
                            it_.name = name
                            it_.pass = pass
                            it_.phone = phone
                            it_.token = token
                        }
                        friendEntity.let {friend->
                            friend.id = id
                            friend.img = img
                            friend.name = name
                            friend.pass = pass
                            friend.phone = phone
                        }
                    }
                    userDao.insertUsers(userEntity)
                    friendDao.insertFriend(friendEntity)

                    MMKV.defaultMMKV().encode(loginToken, it.data?.token)
                    ApiService.instance.api.getToken(it.data?.token).let { it1 ->
                        if (it1.data.isBlank()) {
                            liveData.postValue(it1.msg)
                        } else {
                            liveData.postValue(it1.data)
                        }
                    }
                } else {
                    ToastUtils.show(it.msg)
                }
            }
        }
        return liveData
    }


}