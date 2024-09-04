package dev.panwar.scholarshipapp.response

data class UserNotificationResponseItem(
    val createdAt: String,
    val id: String,
    val isAdmin: Boolean,
    val message: String,
    val title: String,
    val userId: String
)