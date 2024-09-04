package dev.panwar.scholarshipapp.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import dev.panwar.scholarshipapp.MainActivity
import dev.panwar.scholarshipapp.activities.ApplyScholarship
import dev.panwar.scholarshipapp.activities.LoginActivity
import dev.panwar.scholarshipapp.activities.SignUpActivity
import dev.panwar.scholarshipapp.activities.StudentActivity
import dev.panwar.scholarshipapp.activities.utils.Constants
import dev.panwar.scholarshipapp.model.User

class FireStoreClass {

    // initially fireStore
    private val mFireStore= FirebaseFirestore.getInstance()

    fun registerUser(activity: SignUpActivity, userInfo: User){
//        Adding a new Collection of Data to FireStore DB...with name Users...Then we Giving current user ID (id created of User when we SignUp via Firebase authentication) as the Document id to Store in Particular Collection...Then adding all info of Users to the Particular User i.e.(all the parameters of data Class User).
//        .Then after Success of task UserRegisteredSuccess fun execute

        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID()).set(userInfo, SetOptions.merge()).addOnSuccessListener {
//                 function of SignUp activity
                activity.userRegisteredSuccess()
            }.addOnFailureListener {

//                 activity.javaClass.name gives the name of Activity
                    e-> Log.e(activity.javaClass.name,"Error Writing Document")

            }
    }

    //    getting current user id from FireBase Auth
    fun getCurrentUserID():String{
//    getting the current User
        val currentUser= FirebaseAuth.getInstance().currentUser
//    initially current user id
        var currentUserId=""
        if (currentUser!=null){
//            if Current User not null then getting it's id
            currentUserId=currentUser.uid
        }
        return currentUserId
    }

//    /    this function is used to load User Data from Fire Store DB and send to Different Activity Function to perform their tasks
    //    by default readBoardsList:Boolean will contain false if true is passed then i will use true
    fun loadUserData(activity: Activity){
//        same as above function just set change to get...refer to code explanation by above comments
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID()).get().addOnSuccessListener { document->
//                we stored the currently Logged in User details by converting Current User ID(document) to object of type User data class
                val loggedInUser = document.toObject(User::class.java)!!

                when(activity){
                    is LoginActivity ->{
//                       fir this fun will be called by signInActivity
//                       calling function of signInactivity and passing current user..where it will start Main Activity where this Function is called Again and Main Activity context is passed then isMainActivity code is executed
                        activity.signInSuccess(loggedInUser)
                    }
                    is MainActivity -> {
                        activity.updateUI(loggedInUser)
                    }

                    is StudentActivity->{
                        activity.setUserData(loggedInUser)
                    }

                }
            }.addOnFailureListener {
//                 activity.javaClass.name gives the name of Activity
                    e->

                when(activity){
                    is LoginActivity ->{
//                        calling function of signInactivity and passing current user
                        activity.hideProgressDialogue()
                    }
                    is MainActivity -> {
//                        activity.hideProgressDialogue()
                    }
                }
                Log.e(activity.javaClass.name,"Error Writing Document")
            }
    }

    // updating the info in Fire store database of current user Using Hashmap
    fun updateUserProfileData(activity: Activity, userHashmap:HashMap<String,Any>){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID()).update(userHashmap).addOnSuccessListener {
                Log.i(activity.javaClass.simpleName,"Profile data updated successfully")
                Toast.makeText(activity,"Profile details updated successfully", Toast.LENGTH_SHORT).show()
                when(activity){
                    is ApplyScholarship ->{
                        activity.hideProgressDialogue()
                    }
                }

            }.addOnFailureListener {
                when(activity){
                    is ApplyScholarship ->{
                        Log.d("failed to make updates","failed")
                        activity.hideProgressDialogue()
                    }

                }

                Log.i(activity.javaClass.simpleName,"Error while creating a board")
                Toast.makeText(activity,"Error when updating the profile", Toast.LENGTH_SHORT).show()
            }
    }

}