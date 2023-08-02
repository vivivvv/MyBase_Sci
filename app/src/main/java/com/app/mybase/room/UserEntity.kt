package com.app.mybase.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey var userId: String,
    var name: String? = null,
    var phoneNumber: String? = null,
    var email: String? = null,
    var gender: String? = null
)