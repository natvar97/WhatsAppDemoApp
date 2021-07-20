package com.indialone.whatsappdemoapp.models

data class User(
    var profileImage: String = "",
    var userName: String = "",
    var email: String = "",
    var password: String = "",
    var userId: String = "",
    var lastMessage: String = "",
    var about: String = ""
)
