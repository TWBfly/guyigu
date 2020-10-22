/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.guyigu.myapplication.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.guyigu.myapplication.model.db.dao.FriendDao
import com.guyigu.myapplication.model.db.dao.UserDao
import com.guyigu.myapplication.model.db.entity.UserEntity
import com.guyigu.myapplication.util.DATABASE_NAME
import com.tencent.wcdb.database.SQLiteCipherSpec
import com.tencent.wcdb.room.db.WCDBOpenHelperFactory

/**
 * The Room database for this app
 */
@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    companion object {

        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            val cipherSpec: SQLiteCipherSpec = SQLiteCipherSpec() // 指定加密方式，使用默认加密可以省略
                .setPageSize(4096)
                .setKDFIteration(64000)

            val factory = WCDBOpenHelperFactory()
//                .passphrase("passphrase".toByteArray()) // 指定加密DB密钥，非加密DB去掉此行
//                .cipherSpec(cipherSpec) // 指定加密方式，使用默认加密可以省略
                .writeAheadLoggingEnabled(true) // 打开WAL以及读写并发，可以省略让Room决定是否要打开
                .asyncCheckpointEnabled(true)

            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
//                .addMigrations()
//                .openHelperFactory(factory)
                .allowMainThreadQueries()
                .addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                        }
                    }
                )
                .build()
        }
    }

    /**
     * 用户信息
     */
    abstract fun userDao(): UserDao
    /**
     * 好友
     */
    abstract fun friendDao(): FriendDao
}
