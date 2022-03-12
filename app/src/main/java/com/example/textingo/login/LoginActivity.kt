package com.example.textingo.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.textingo.R
import com.example.textingo.constants.Constants
import com.example.textingo.messages.LatestMessagesActivity
import com.example.textingo.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_button_login.setOnClickListener {
            val email = email_edittext_login.text.toString()
            val password = password_edittext_login.text.toString()

            logInRegisteredUser(email, password)

        }

        login_click_to_register.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun validateLoginDetails(email: String, password: String): Boolean {
        if(email.isBlank() || email.isEmpty()){
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            return false
        }
        if(password.isEmpty() || password.isBlank()){
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun logInRegisteredUser(email: String, password: String) {

        if (validateLoginDetails(email, password)) {

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    val intent = Intent(this, LatestMessagesActivity::class.java)

                    FirebaseDatabase.getInstance("https://textingo-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users").get().addOnSuccessListener {
                        for(snap in it.children){
                            val currentUser = snap.getValue(User::class.java)
                            if(currentUser!!.email == email){
                                intent.putExtra(Constants.CURRENT_USER_KEY, currentUser)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                break
                            }
                        }
                    }

                }
        }
    }
}