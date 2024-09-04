package dev.panwar.scholarshipapp.request

data class FinanceHeadApprovalRequest(
    val amount: Int,
    val approve: Boolean
)