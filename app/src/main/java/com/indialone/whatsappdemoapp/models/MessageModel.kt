package com.indialone.whatsappdemoapp.models

data class MessageModel(
    var uid: String = "",
    var message: String = "",
    var timeStamp: Long = 0L,
    var messageId: String = ""
)
