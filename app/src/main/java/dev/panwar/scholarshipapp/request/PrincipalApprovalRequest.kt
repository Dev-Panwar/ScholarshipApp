package dev.panwar.scholarshipapp.request

data class PrincipalApprovalRequest(
    val approve: Boolean,
    val principalFeedback: String
)