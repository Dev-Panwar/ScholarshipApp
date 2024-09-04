package dev.panwar.scholarshipapp.intro

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import dev.panwar.scholarshipapp.MainActivity
import dev.panwar.scholarshipapp.databinding.ActivitySplashScreenBinding
import dev.panwar.scholarshipapp.firebase.FireStoreClass

class SplashScreen : AppCompatActivity() {

    private var binding:ActivitySplashScreenBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding?.root)

//        To set this Screen as full screen covering complete display of phone and hiding status bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

//        for changing the Font of displayed title..i.e. APP name
        val typeFace: Typeface = Typeface.createFromAsset(assets,"Carnevalee Freakshow.ttf")
        binding?.tvAppName?.typeface=typeFace

//        to Move to Intro Activity automatically after 1.5 secs
        Handler().postDelayed({
//            For auto Log In of Previously LoggedIn User....This is feature of Firebase Authorization that it store the last User sign in or sign up...So we use fun of fireStore class to get user id from last User we have stored in firebase...if it's not empty we move ahead
            var currentUserId= FireStoreClass().getCurrentUserID()
            if (currentUserId.isNotEmpty()){
                startActivity(Intent(this, MainActivity::class.java))
            }else{
//                So that User sign in or sign up
                startActivity(Intent(this,IntroActivity::class.java))
            }
            finish()//finishes this activity
        },2000)

    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}