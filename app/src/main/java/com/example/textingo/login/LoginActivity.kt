package com.example.textingo.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.textingo.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_button_login.setOnClickListener {
            val email = email_edittext_login.text.toString()
            val password = password_edittext_login.text.toString()

        }

        back_to_register_textview.setOnClickListener{
            finish()
        }
    }
}