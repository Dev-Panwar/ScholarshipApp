package dev.panwar.scholarshipapp.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dev.panwar.abtax.response.ApplyScholarshipResponse
import dev.panwar.scholarshipapp.R
import dev.panwar.scholarshipapp.activities.utils.Constants
import dev.panwar.scholarshipapp.api.RetrofitInstance
import dev.panwar.scholarshipapp.databinding.ActivityApplyScholarshipBinding
import dev.panwar.scholarshipapp.firebase.FireStoreClass
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class ApplyScholarship : BaseActivity() {

    private var binding:ActivityApplyScholarshipBinding?=null

    private var mAadharPart: MultipartBody.Part?=null
    private var mIncomePart: MultipartBody.Part?=null
    private var mMarksheetPart: MultipartBody.Part?=null

    private val permissionsToCheck = arrayOf(
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.READ_MEDIA_AUDIO
        // Add more permissions as needed
    )



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityApplyScholarshipBinding.inflate(layoutInflater)
        setContentView(binding?.root)



        binding?.tvTitle?.setOnClickListener {
            onBackPressed()
        }

        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val application_ID=sharedPreferences.getString("APPLICATION_ID", "")

        if (application_ID != null && application_ID!="") {
            Log.d("application_ID",application_ID)
            binding?.llLayoutApplication?.visibility=View.GONE
            binding?.llAlreadyApplied?.visibility=View.VISIBLE
            binding?.referenceNum?.text=application_ID

        }

        binding?.btnSelectAadhar?.setOnClickListener {
            if (Build.VERSION.SDK_INT>=33){
                if (checkPermissions()){
                    Constants.showFileChooser(this, Constants.SELECT_AADHAR_REQUEST_CODE)
                }else{
                    storagePermission(this)
                }
            }else{
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    Constants.showFileChooser(this,Constants.SELECT_AADHAR_REQUEST_CODE)
                }
//            asking for storage permission...This time not using dexter library...using the default method
                else{
                    storagePermission(this)
                }
            }
        }

        binding?.btnSelectIncomeCertificate?.setOnClickListener {
            if (Build.VERSION.SDK_INT>=33){
                if (checkPermissions()){
                    Constants.showFileChooser(this, Constants.SELECT_INCOME_REQUEST_CODE)
                }else{
                    storagePermission(this)
                }
            }else{
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    Constants.showFileChooser(this,Constants.SELECT_INCOME_REQUEST_CODE)
                }
//            asking for storage permission...This time not using dexter library...using the default method
                else{
                    storagePermission(this)
                }
            }
        }

        binding?.btnSelectMarksheet?.setOnClickListener {
            if (Build.VERSION.SDK_INT>=33){
                if (checkPermissions()){
                    Constants.showFileChooser(this, Constants.SELECT_MARKSHEET_REQUEST_CODE)
                }else{
                    storagePermission(this)
                }
            }else{
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    Constants.showFileChooser(this,Constants.SELECT_MARKSHEET_REQUEST_CODE)
                }
//            asking for storage permission...This time not using dexter library...using the default method
                else{
                    storagePermission(this)
                }
            }
        }

        binding?.deleteSelectedAadharFile?.setOnClickListener{
            binding?.tvSelectedAadharFile?.text=resources.getString(R.string.no_file_chosen)
            binding?.deleteSelectedAadharFile?.visibility=View.GONE
            mAadharPart=null
        }

        binding?.deleteSelectedIncomeCertificate?.setOnClickListener{
            binding?.selectedIncomeCertificate?.text=resources.getString(R.string.no_file_chosen)
            binding?.deleteSelectedIncomeCertificate?.visibility=View.GONE
            mIncomePart=null
        }

        binding?.deleteSelectedMarksheet?.setOnClickListener{
            binding?.selectedMarksheet?.text=resources.getString(R.string.no_file_chosen)
            binding?.deleteSelectedMarksheet?.visibility=View.GONE
            mMarksheetPart=null
        }

        binding?.btnApply?.setOnClickListener {
            if (binding?.etName?.text?.isEmpty() == false && binding?.etName?.text?.isEmpty() == false && binding?.etName?.text?.isEmpty() == false){
                if (mIncomePart!=null && mAadharPart!=null && mMarksheetPart!=null){
                    makeApiCall()
                }else{
                    Toast.makeText(this,"Please select all documents",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this,"All fields are required!",Toast.LENGTH_SHORT).show()
            }
        }



    }

    private fun makeApiCall(){
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("USER_ID", null)

        val username = binding?.etName?.text.toString().trim{it<=' '}
        val rollNum = binding?.etRollNum?.text.toString().trim{it<=' '}
        val branch = binding?.etBranch?.text.toString().trim{it<=' '}

        val usernameRB = RequestBody.create("text/plain".toMediaTypeOrNull(),username)
        val rollNumRB = RequestBody.create("text/plain".toMediaTypeOrNull(),rollNum)
        val branchRB = RequestBody.create("text/plain".toMediaTypeOrNull(),branch)
        val studentRB = RequestBody.create("text/plain".toMediaTypeOrNull(),userId!!)

//        if (userId!=null){
//            Log.d("userID: Apply Scholarship",userId!!)
//            return
//        }

        showProgressDialog("Applying...")

        val call: Call<ApplyScholarshipResponse> = RetrofitInstance.api.applyScholarship(branchRB,usernameRB,rollNumRB,mAadharPart!!,mMarksheetPart!!,mIncomePart!!,studentRB)
        call.enqueue(object : Callback<ApplyScholarshipResponse> {
            override fun onResponse(call: Call<ApplyScholarshipResponse>, response: Response<ApplyScholarshipResponse>) {
                if (response.isSuccessful) {
                    hideProgressDialogue()


                    Log.e("Application Response",response.body().toString())
                    val userHashMap = HashMap<String,Any>()
                    userHashMap[Constants.APPLICATION_ID] = response.body()!!.application.id

                    FireStoreClass().updateUserProfileData(this@ApplyScholarship,userHashMap)

                    val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

                    with(sharedPref.edit()) {
                        putString("APPLICATION_ID", response.body()!!.application.id)
                        apply()
                    }

                    Toast.makeText(this@ApplyScholarship,"Application Submitted Successfully",Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@ApplyScholarship,StudentActivity::class.java))

                } else {
                    hideProgressDialogue()
                    // Handle non-successful response (e.g., HTTP error)
                    val errorBody = response.errorBody()?.string()
                    val jsonObject = JSONObject(errorBody)
                    val errorMessage = jsonObject.getString("error")
                    // Show the error message in your app (e.g., using a Toast or a TextView)
                    // For example, using Toast:
                    Toast.makeText(this@ApplyScholarship, "You Can Apply Once Once", Toast.LENGTH_SHORT).show()
                    Log.e("Application Response",errorMessage.toString())

                }
            }

            override fun onFailure(call: Call<ApplyScholarshipResponse>, t: Throwable) {
                hideProgressDialogue()
                // Handle failure (e.g., network issues)
                Toast.makeText(this@ApplyScholarship, "Request failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.SELECT_AADHAR_REQUEST_CODE && data!!.data != null) {
                data.data?.let { uri ->

                    binding?.deleteSelectedAadharFile?.visibility = View.VISIBLE

                    val File = createTempFile(uri) //creating temporary file
                    binding?.tvSelectedAadharFile?.text = getFileName(uri) //setting text of selected file
                    copyUriToFile(uri, File) //copying file content of selected file to temp file
                    val requestFile = RequestBody.create("*/*".toMediaTypeOrNull(), File)
                    val fileBody = MultipartBody.Part.createFormData("aadharCard", File.name, requestFile)

                    mAadharPart = fileBody
                }
            }

            if (requestCode == Constants.SELECT_INCOME_REQUEST_CODE && data!!.data != null) {
                data.data?.let { uri ->

                    binding?.deleteSelectedIncomeCertificate?.visibility = View.VISIBLE

                    val File = createTempFile(uri) //creating temporary file
                    binding?.selectedIncomeCertificate?.text = getFileName(uri) //setting text of selected file
                    copyUriToFile(uri, File) //copying file content of selected file to temp file
                    val requestFile = RequestBody.create("*/*".toMediaTypeOrNull(), File)
                    val fileBody = MultipartBody.Part.createFormData("incomeCertificate", File.name, requestFile)

                    mIncomePart = fileBody
                }
            }

            if (requestCode == Constants.SELECT_MARKSHEET_REQUEST_CODE && data!!.data != null) {
                data.data?.let { uri ->

                    binding?.deleteSelectedMarksheet?.visibility = View.VISIBLE

                    val File = createTempFile(uri) //creating temporary file
                    binding?.selectedMarksheet?.text = getFileName(uri) //setting text of selected file
                    copyUriToFile(uri, File) //copying file content of selected file to temp file
                    val requestFile = RequestBody.create("*/*".toMediaTypeOrNull(), File)
                    val fileBody = MultipartBody.Part.createFormData("marksheet", File.name, requestFile)

                    mMarksheetPart = fileBody
                }
            }



        }
    }

    private fun createTempFile(uri: Uri): File {
        val storageDir = cacheDir
        return File.createTempFile("document_", ".${Constants.getFileExtension(this,uri)}", storageDir)
    }

    private fun copyUriToFile(uri: Uri, destFile: File) {
        contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(destFile).use { output ->
                val buffer = ByteArray(4 * 1024)
                var bytesRead: Int
                while (input.read(buffer).also { bytesRead = it } >= 0) {
                    output.write(buffer, 0, bytesRead)
                }
            }
        }
    }

    @SuppressLint("Range")
    private fun getFileName(uri: Uri): String? {
        var result: String? = null
        val scheme = uri.scheme

        if (scheme == "content") {
            contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            }
        } else if (scheme == "file") {
            result = File(uri.path!!).name
        }

        return result
    }

    private fun checkPermissions(): Boolean {
        for (permission in permissionsToCheck) {
            if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}