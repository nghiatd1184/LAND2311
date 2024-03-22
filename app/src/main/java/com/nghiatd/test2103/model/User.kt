package com.nghiatd.test2103.model

data class User(
    val name:String,
    val avatar: Int,
    val messages: List<Message>
    ) {
}