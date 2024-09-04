package dev.panwar.abtax.response

data class ApplyScholarshipResponse(
    val application: Application,
    val message: String
)

data class Application(
    val aadharCard: String,
    val amountSanction: Any,
    val approvedByFinanceHead: Boolean,
    val approvedByHod: Boolean,
    val approvedByPrincipal: Boolean,
    val branch: String,
    val createdAt: String,
    val hodFeedback: Any,
    val id: String,
    val incomeCertificate: String,
    val marksheet: String,
    val name: String,
    val principalFeedback: Any,
    val rollNo: String,
    val status: String,
    val studentId: String
)