package dev.panwar.scholarshipapp.activities.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {
    //    Collection name for users in Firebase
    const val USERS:String= "Users"
    //    Attributes of User data Class
    const val IMAGE:String="image"
    const val NAME:String="name"
    const val MOBILE:String="mobile"
    const val APPLICATION_ID:String="applicationNumber"
    const val BASE_URL:String="https://hosho-digital-be.onrender.com"



    //   for Start Activity for Result
    const val READ_STORAGE_PERMISSION_CODE=1
    const val PICK_IMAGE_REQUEST_CODE=2

    const val SELECT_AADHAR_REQUEST_CODE=1
    const val SELECT_INCOME_REQUEST_CODE=10
    const val SELECT_MARKSHEET_REQUEST_CODE=15


    fun showFileChooser(activity: Activity,requestCode:Int) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*" // All files
        intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf(
            "image/*",  // Include image files
            "application/pdf"  // Include PDF files
        )
        )

        activity.startActivityForResult(intent,requestCode)
    }

    fun getFileExtension(activity: Activity, uri: Uri?):String?{
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }

}