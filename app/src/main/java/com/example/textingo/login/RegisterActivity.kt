package com.example.textingo.login

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.example.textingo.constants.Constants
import com.example.textingo.R
import com.example.textingo.messages.LatestMessagesActivity
import com.example.textingo.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    var selectedPhotoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_button_register.setOnClickListener {
            registerUser()
        }

        already_have_account_text_view.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        select_photo_register_button.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, Constants.SELECT_PHOTO_REGISTER_REQUEST_CODE)
        }

        civ_profile_photo_register.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, Constants.CIV_SELECT_PHOTO_REGISTER_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == Constants.SELECT_PHOTO_REGISTER_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK && data != null){
                selectedPhotoUri = data.data
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
                civ_profile_photo_register.setImageBitmap(bitmap)
                select_photo_register_button.visibility = View.INVISIBLE
                civ_profile_photo_register.visibility = View.VISIBLE
            }
        }
        if(requestCode == Constants.CIV_SELECT_PHOTO_REGISTER_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK && data != null){
                selectedPhotoUri = data.data
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
                civ_profile_photo_register.setImageBitmap(bitmap)
                select_photo_register_button.visibility = View.INVISIBLE
                civ_profile_photo_register.visibility = View.VISIBLE
            }
        }
    }

    private fun registerUser(){
        val email: String = email_edittext_register.text.toString()
        val password: String = password_edittext_register.text.toString()
        val username: String = username_edittext_register.text.toString()

        if(validate(username, email, password)){

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener
                    uploadProfileImageToFirebaseStorage()
                }
                .addOnFailureListener{
                    Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()
                }

        }
    }

    private fun uploadProfileImageToFirebaseStorage() {
        if (selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {

                ref.downloadUrl.addOnSuccessListener {
                    saveUserToFirebaseDatabase(it.toString())
                }
            }
            .addOnFailureListener {
            }
    }

    private fun saveUserToFirebaseDatabase(profileImageURL: String){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance("https://textingo-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/users/$uid")


        val user = User(email_edittext_register.text.toString(), uid.toString(), username_edittext_register.text.toString(), profileImageURL)

        ref.setValue(user).addOnSuccessListener {
            Toast.makeText(this, "You have successfully registered!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LatestMessagesActivity::class.java)
            intent.putExtra(Constants.CURRENT_USER_KEY, user)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }.addOnFailureListener {
            Toast.makeText(this, "There was an error during registration.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validate(username: String, email: String, password: String): Boolean {
        if(username.isBlank() || username.isEmpty()){
            Toast.makeText(this, "Please enter your username", Toast.LENGTH_SHORT).show()
            return false
        }
        if(email.isBlank() || email.isEmpty()){
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            return false
        }
        if(password.isEmpty() || password.isBlank()){
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show()
            return false
        }
        if(selectedPhotoUri == null){
            Toast.makeText(this, "Please choose a profile photo", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }


}