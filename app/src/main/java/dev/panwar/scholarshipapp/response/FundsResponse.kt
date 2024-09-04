package dev.panwar.scholarshipapp.response

data class FundsResponse(
    val difference: Int,
    val remainingAmount: Int,
    val totalAmount: Int,
    val totalAmountSanctioned: Int
)