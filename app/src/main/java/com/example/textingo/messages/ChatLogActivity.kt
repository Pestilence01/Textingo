package com.example.textingo.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.textingo.constants.Constants
import com.example.textingo.R
import com.example.textingo.adapters.ChatLogAdapter
import com.example.textingo.models.ChatMessage
import com.example.textingo.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : AppCompatActivity() {

    var receiverRoom: String? = null
    var senderRoom: String? = null

    private val mDbRefUsers = FirebaseDatabase.getInstance("https://textingo-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users")
    private val mDbRefChats = FirebaseDatabase.getInstance("https://textingo-default-rtdb.europe-west1.firebasedatabase.app/").getReference("chats")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        val receiver: User = intent.getParcelableExtra<User>(Constants.NEW_MESSAGE_USER_KEY)!!




        val receiverUid = receiver.uid.toString()
        val senderUid = FirebaseAuth.getInstance().uid.toString()

        var senderUrl: String = ""



        mDbRefUsers.child(senderUid).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val currentUser = snapshot.getValue(User::class.java)
                senderUrl = currentUser!!.profileImageUrl
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })





        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid


        supportActionBar?.title = receiver.username.toString()

        val messageList: ArrayList<ChatMessage> = ArrayList()

        val chatAdapter = ChatLogAdapter(this, messageList)

        recyclerview_chat_log.adapter = chatAdapter

        mDbRefChats.child(senderRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()

                    for(postSnapshot in snapshot.children){
                        val message = postSnapshot.getValue(ChatMessage::class.java)
                        messageList.add(message!!)
                    }
                    chatAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        send_button_chat_log.setOnClickListener {
            performSendMessage(receiver.profileImageUrl, senderUrl)
        }
    }


    private fun performSendMessage(url1: String, url2: String) {
        val text = edittext_chat_log.text.toString()

        val fromId = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(Constants.NEW_MESSAGE_USER_KEY)
        val toId = user!!.uid

        if (fromId == null) return

        val chatMessage = ChatMessage(text, fromId, toId, System.currentTimeMillis() / 1000, url2, url1)

        mDbRefChats.child(senderRoom!!).child("messages").push().setValue(chatMessage).addOnSuccessListener {
            mDbRefChats.child(receiverRoom!!).child("messages").push().setValue(chatMessage)
        }

        edittext_chat_log.text.clear()

        val latestMessageRef = FirebaseDatabase.getInstance("https://textingo-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/latest-messages/$fromId/$toId")
        latestMessageRef.setValue(chatMessage)

        val latestMessageToRef = FirebaseDatabase.getInstance("https://textingo-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/latest-messages/$toId/$fromId")
        latestMessageToRef.setValue(chatMessage)

    }
}