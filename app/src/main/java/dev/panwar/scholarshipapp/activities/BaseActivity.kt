package dev.panwar.scholarshipapp.activities

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import dev.panwar.scholarshipapp.R

open class BaseActivity : AppCompatActivity() {
    private var doubleBackToExitPressedOnce=false
    private lateinit var mProgressDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }

    fun showProgressDialog(text:String){
        mProgressDialog= Dialog(this)
        mProgressDialog.setContentView(R.layout.dialog_progress)
//        findViewById<TextView>(R.id.tv_progress_text).text= text
        mProgressDialog.show()
    }
    // for hiding progress Dialogue
    fun hideProgressDialogue(){
        mProgressDialog.dismiss()
    }

    //    TO get the current user id
    fun getCurrentUserID():String{
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    //    if someone pressed back button twice....we close the Application
    fun doubleBackToExit(){
        if (doubleBackToExitPressedOnce){
            super.onBackPressed()
            return
        }
//    initially it is set as false....one pressing once it becomes true...on if we press again it enters the if condition
        doubleBackToExitPressedOnce=true
        Toast.makeText(this,resources.getString(R.string.please_click_back_again_to_exit), Toast.LENGTH_SHORT).show()

//    if User Does not press back button again before 2 secs we reset doubleBackToExitPressedOnce=false
        Handler().postDelayed({
            doubleBackToExitPressedOnce=false
        },2000)
    }

    //    for showing snackBar in Case of any error
    fun showErrorSnackBar(message:String){
        val snackBar= Snackbar.make(findViewById(android.R.id.content),message, Snackbar.LENGTH_LONG)
//    for changing anything to the view of SnackBar
        val snackBarView=snackBar.view
        snackBarView.setBackgroundColor(ContextCompat.getColor(this,R.color.snackbar_error_color))
        snackBar.show()
    }

    fun storagePermission(context: Context){
        if (Build.VERSION.SDK_INT>=33){
            Dexter.withContext(context).withPermissions(
                Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_AUDIO
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {

                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    showRationaleDialogueForPermissions(context)
                }


            }).onSameThread().check()
        }else{

            Dexter.withContext(context).withPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
//                Toast.makeText(context,"Read External Storage Permission Granted",Toast.LENGTH_SHORT).show()
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    showRationaleDialogueForPermissions(context)
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    showRationaleDialogueForPermissions(context)
                }

            }).onSameThread().check()

        }

    }

    fun showRationaleDialogueForPermissions(context: Context) {
        AlertDialog.Builder(context).setMessage("It looks Like you have turned off Permission required for this app's functioning. It can be enabled under Application settings"
        ).setPositiveButton("Go To Settings"){_,_->
// making user go to settings to manually give the permission
//            automatically going to the APP Settings
            try {
                val intent= Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri= Uri.fromParts("package",packageName,null)
                intent.data=uri
                startActivity(intent)
            }
            catch (e: ActivityNotFoundException){
                e.printStackTrace()
            }

        }.setNegativeButton("Cancel"){dialog,_ ->
            dialog.dismiss()
        }.show()
    }

}