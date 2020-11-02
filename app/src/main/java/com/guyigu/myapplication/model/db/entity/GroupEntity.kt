package com.guyigu.myapplication.model.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by tang on 2020/11/2
 */
@Entity(tableName = "group")
class GroupEntity {
    @PrimaryKey
    var groupId: Int = 0
    var groupName: String = ""
}