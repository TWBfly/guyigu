package com.guyigu.myapplication.bean

/**
 * Created by tang on 2020/10/19
 */
data class LoginBean(
    val code: Int = 0,
    val msg: String = "",
    val data: Data?
)

data class Data(
    val id: Int = 0,
    val phone: String = "",
    val pass: String = "",
    val name: String = "",
    val img: String = "",
    val token: String = "",
)


data class TokenBean(
    val code: Int = 0,
    val msg: String = "",
    val data: String
)

data class AddFriendBean(
    val code: Int = 0,
    val msg: String = ""
)

data class ApplyFriendBean(
    val code: Int = 0,
    val msg: String = "",
    val data: MutableList<ApplyFriendData>
)

data class ApplyFriendData(
    val id: Int = 0,
    val phone: String = "",
    val name: String = "",
    val img: String = "",
    val remark: String = "",
)

data class MyFriendBean(
    val code: Int = 0,
    val msg: String = "",
    val data: MutableList<Data>?
)
