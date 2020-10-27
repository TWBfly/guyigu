package com.guyigu.myapplication.model.db.dao

import androidx.room.*
import com.guyigu.myapplication.model.db.entity.FriendEntity


/**
 * Created by tang on 2020/10/20
 */
@Dao
interface FriendDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFriend(vararg userEntities: FriendEntity?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateFriend(vararg userEntities: FriendEntity?): Int

    @Delete
    fun deleteFriend(vararg userEntities: FriendEntity?)

    @Query("SELECT * FROM friend")
    fun queryAllUsers(): List<FriendEntity>?

    @Query("SELECT * FROM friend WHERE id =:userId")
    fun queryFriendById(userId: Int): FriendEntity?
}