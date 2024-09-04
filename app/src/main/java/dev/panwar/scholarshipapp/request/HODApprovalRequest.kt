package dev.panwar.scholarshipapp.request

data class HODApprovalRequest(
    val approve: Boolean,
    val feedback: String
)