package dev.panwar.scholarshipapp.response

data class TrackApplicationResponse(
    val aadharCard: String,
    val amountSanction: Int,
    var approvedByFinanceHead: Boolean,
    var approvedByHod: Boolean,
    var approvedByPrincipal: Boolean,
    val branch: String,
    val createdAt: String,
    val hodFeedback: String,
    val id: String,
    val incomeCertificate: String,
    val marksheet: String,
    val name: String,
    val principalFeedback: String,
    val rollNo: String,
    val status: String,
    val studentId: String
)