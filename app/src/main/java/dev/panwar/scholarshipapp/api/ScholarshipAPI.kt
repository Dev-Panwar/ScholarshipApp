package dev.panwar.scholarshipapp.api

import dev.panwar.abtax.response.ApplyScholarshipResponse
import dev.panwar.scholarshipapp.request.FinanceHeadApprovalRequest
import dev.panwar.scholarshipapp.request.HODApprovalRequest
import dev.panwar.scholarshipapp.request.PrincipalApprovalRequest
import dev.panwar.scholarshipapp.response.FundsResponse
import dev.panwar.scholarshipapp.response.HodApprovalResponse
import dev.panwar.scholarshipapp.response.TrackApplicationResponse
import dev.panwar.scholarshipapp.response.UserNotificationResponseItem
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ScholarshipAPI {

    @Multipart
    @POST("/api/v1/student/upload-documents")
    fun applyScholarship(
        @Part("branch") itrSelectFile: RequestBody,
        @Part("name") selectWord: RequestBody,
        @Part("rollNo") panId: RequestBody,
        @Part aadharCard: MultipartBody.Part,
        @Part marksheet: MultipartBody.Part,
        @Part incomeCertificate: MultipartBody.Part,
        @Part("studentId") password: RequestBody
    ): Call<ApplyScholarshipResponse>


    @GET("/api/v1/admin/get-applications")
    suspend fun getApplicationStatus():Response<List<TrackApplicationResponse>>

    @PATCH("/api/v1/scholarship-application/{applicationId}/approve/hod")
    suspend fun uploadHODApproval(
        @Path("applicationId") applicationId: String,
        @Body approvalRequest: HODApprovalRequest
    ):Response<HodApprovalResponse>

    @PATCH("/api/v1/scholarship-application/{applicationId}/approve/principal")
    suspend fun uploadPrincipalApproval(
        @Path("applicationId") applicationId: String,
        @Body approvalRequest: PrincipalApprovalRequest
    ):Response<HodApprovalResponse>

    @PATCH("/api/v1/scholarship-application/{applicationId}/approve/finance-head")
    suspend fun uploadFinanceHeadApproval(
        @Path("applicationId") applicationId: String,
        @Body approvalRequest: FinanceHeadApprovalRequest
    ):Response<HodApprovalResponse>

    @GET("/api/v1/getAmountDetails")
    suspend fun getFunds():Response<FundsResponse>

    @GET("/api/v1/scholarships/accepted")
    suspend fun getAcceptedApplication():Response<List<TrackApplicationResponse>>

    @GET("/api/v1/scholarships/pending")
    suspend fun getPendingApplication():Response<List<TrackApplicationResponse>>

    @GET("/api/v1/scholarships/rejected")
    suspend fun getRejectedApplication():Response<List<TrackApplicationResponse>>

    @GET("/api/v1/notifications/user")
    suspend fun getUserNotifications():Response<List<UserNotificationResponseItem>>





}