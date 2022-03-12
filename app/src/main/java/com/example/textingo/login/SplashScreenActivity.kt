package com.example.textingo.login

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.example.textingo.R
import com.example.textingo.database.RealtimeDatabaseClass
import com.example.textingo.messages.LatestMessagesActivity
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val typeFace: Typeface = Typeface.createFromAsset(assets, "SquashwillRegular.ttf")
        tv_app_name.setTypeface(typeFace)

        var currentUid = RealtimeDatabaseClass().getCurrentUserID()

        if(currentUid == ""){
            Handler().postDelayed({
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }, 3000)
        }

        else {
            Handler().postDelayed({
                startActivity(Intent(this, LatestMessagesActivity::class.java))
                finish()
            }, 3000)
        }
    }
}