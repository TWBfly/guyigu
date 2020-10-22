package com.guyigu.myapplication.model.db.dao

import androidx.room.*
import com.guyigu.myapplication.model.db.entity.UserEntity


/**
 * Created by tang on 2020/10/20
 */
@Dao
interface FriendDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFriend(vararg userEntities: UserEntity?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateFriend(vararg userEntities: UserEntity?): Int

    @Delete
    fun deleteFriend(vararg userEntities: UserEntity?)

    @Query("SELECT * FROM users")
    fun queryAllUsers(): List<UserEntity>?

    @Query("SELECT * FROM users WHERE id =:userId")
    fun queryFriendById(userId: Int): UserEntity?
}