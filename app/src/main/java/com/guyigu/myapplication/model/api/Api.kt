package com.guyigu.myapplication.model.api

import com.guyigu.myapplication.bean.*
import retrofit2.http.*

/**
 * Created by tang on 2020/10/18
 */
interface Api {
    @GET("app/user/login")
    @JvmSuppressWildcards
    suspend fun login(@QueryMap map: Map<String, Any>): LoginBean

    @GET("app/user/getToken")
    suspend fun getToken(@Header("Authorization") Authorization: String?): TokenBean

    /**
     * 好友列表
     */
    @POST("app/user/myFriendList")
    suspend fun getMyFriendList(): MyFriendBean

    /**
     *搜索好友
     */
    @GET("app/user/searchPhone")
    suspend fun searchPhone(@Query("userName") userName: String): LoginBean

    /**
     * 添加好友,发送申请
     */
    @FormUrlEncoded
    @POST("app/user/addFriend")
    suspend fun addFriend(@Field("remark") remark: String, @Field("userId") userId: Int): AddFriendBean

    /**
     * 好友申请审核
     * -1 已拒绝 1已经通过
     */
    @FormUrlEncoded
    @POST("app/user/checkFriendApply")
    suspend fun getCheckFriendApply(@Field("type") type: Int, @Field("userApplyRecordId") userApplyRecordId: Int):AddFriendBean

    /**
     * 获取好友申请列表
     * -1 已拒绝 0 待通过 1已经通过
     */
    @FormUrlEncoded
    @POST("app/user/friendApplyList")
    suspend fun getFriendApplyList(@Field("type") type: Int): ApplyFriendBean

    /**
     * 删除好友
     */
    @FormUrlEncoded
    @POST("/app/user/delFriend")
    suspend fun delFriend(@Field("userId") userId: Int):AddFriendBean

    /**
     * 测试系统消息
     */
    @GET("app/user/testMsg")
    suspend fun testMsg():AddFriendBean

}