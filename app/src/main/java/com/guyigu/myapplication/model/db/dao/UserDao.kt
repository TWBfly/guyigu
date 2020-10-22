package com.guyigu.myapplication.model.db.dao

import androidx.room.*
import com.guyigu.myapplication.model.db.entity.UserEntity


/**
 * Created by tang on 2020/10/20
 */
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(vararg userEntities: UserEntity?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateUsers(vararg userEntities: UserEntity?): Int

    @Delete
    fun deleteUsers(vararg userEntities: UserEntity?)

    @Query("SELECT * FROM users")
    fun queryAllUsers(): List<UserEntity>?

    @Query("SELECT * FROM users WHERE id =:userId")
    fun queryUserById(userId:Int): UserEntity?

//    @Query("SELECT * FROM users WHERE firstName LIKE :search " + "OR lastName LIKE :search")
//    fun findUserWithName(search: String?): List<User?>?
}