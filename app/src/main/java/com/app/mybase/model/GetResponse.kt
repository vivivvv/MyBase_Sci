package com.app.mybase.model

data class GetResponse(
    val _id: String,
    var eMail: String,
    val gender: String,
    val phoneNumber: String,
    val userName: String
)
