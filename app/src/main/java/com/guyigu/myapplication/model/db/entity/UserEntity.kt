package com.guyigu.myapplication.model.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Created by tang on 2020/10/20
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey var id: Int = 0,
    var phone: String = "",
    var pass: String = "",
    var name: String = "",
    var img: String = "",
    var token: String = "",
)

