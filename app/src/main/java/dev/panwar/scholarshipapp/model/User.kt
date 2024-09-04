package dev.panwar.scholarshipapp.model

data class User(
    val id:String="",
    val name:String="",
    val email:String="",
    val role:String="STUDENT",
    val phoneNumber:String="",
    val applicationNumber:String=""
)
