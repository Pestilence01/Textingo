package com.example.textingo.messages

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.textingo.R
import com.example.textingo.adapters.NewMessageAdapter
import com.example.textingo.constants.Constants
import com.example.textingo.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_latest_messages.*
import kotlinx.android.synthetic.main.activity_new_message.*


class NewMessageActivity : AppCompatActivity() {


    private lateinit var userList: ArrayList<User>
    private lateinit var currentUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        if(intent.hasExtra(Constants.CURRENT_USER_KEY_ADAPTER)){
            currentUser = intent.getParcelableExtra<User>(Constants.CURRENT_USER_KEY_ADAPTER)!!
        }

        setupActionBar()

        recyclerview_newmessage.setHasFixedSize(true)

        userList = arrayListOf<User>()

        fetchUsers()



    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_new_message)

        val actionBar = supportActionBar

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        toolbar_new_message.setNavigationOnClickListener { onBackPressed() }

    }

    private fun fetchUsers() {

        val ref = FirebaseDatabase.getInstance("https://textingo-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/users")
        ref.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    for(userSH in p0.children){
                        val user = userSH.getValue(User::class.java)
                        if(user!!.uid == currentUser!!.uid){
                            continue
                        }
                        userList.add(user!!)
                    }

                    recyclerview_newmessage.adapter = NewMessageAdapter(this@NewMessageActivity, userList, currentUser)

                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }


}