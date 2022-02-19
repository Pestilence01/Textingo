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

    val mDbRef = FirebaseDatabase.getInstance("https://textingo-default-rtdb.europe-west1.firebasedatabase.app/").getReference()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        val user: User = intent.getParcelableExtra<User>(Constants.NEW_MESSAGE_USER_KEY)!!

        val receiverUid = user.uid.toString()
        val senderUid = FirebaseAuth.getInstance().uid.toString()

        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid


        supportActionBar?.title = user.username.toString()

        val messageList: ArrayList<ChatMessage> = ArrayList()

        val chatAdapter = ChatLogAdapter(this, messageList)

        recyclerview_chat_log.adapter = chatAdapter

        mDbRef.child("chats").child(senderRoom!!).child("messages")
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
            performSendMessage()
        }
    }


    private fun performSendMessage() {
        val text = edittext_chat_log.text.toString()

        val fromId = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(Constants.NEW_MESSAGE_USER_KEY)
        val toId = user!!.uid

        if (fromId == null) return

        val chatMessage = ChatMessage(text, fromId, toId, System.currentTimeMillis() / 1000)

        mDbRef.child("chats").child(senderRoom!!).child("messages").push().setValue(chatMessage).addOnSuccessListener {
            mDbRef.child("chats").child(receiverRoom!!).child("messages").push().setValue(chatMessage)
        }

        edittext_chat_log.text.clear()

    }
}