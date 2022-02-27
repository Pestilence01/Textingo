package com.example.textingo.messages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.textingo.R
import com.example.textingo.adapters.LatestMessageAdapter
import com.example.textingo.constants.Constants
import com.example.textingo.login.RegisterActivity
import com.example.textingo.models.ChatMessage
import com.example.textingo.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_latest_messages.*

class LatestMessagesActivity : AppCompatActivity() {

    private lateinit var currentUser: User
    private lateinit var latestMessagesList: ArrayList<ChatMessage>
    private lateinit var latestMessagesMap: HashMap<String, ChatMessage>
    private lateinit var adapter: LatestMessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)

        if(intent.hasExtra(Constants.CURRENT_USER_KEY)){
            currentUser = intent.getParcelableExtra<User>(Constants.CURRENT_USER_KEY)!!
        }

        latestMessagesList = ArrayList<ChatMessage>()
        latestMessagesMap = HashMap<String, ChatMessage>()

        adapter = LatestMessageAdapter(this, latestMessagesList)
        recyclerview_latest_messages.adapter = adapter

        listenForLatestMessages()



    }

    override fun onResume() {
        FirebaseDatabase.getInstance("https://textingo-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users").child(FirebaseAuth.getInstance().uid.toString()).addListenerForSingleValueEvent(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser = snapshot.getValue(User::class.java)!!
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
            })
        super.onResume()
    }


    private fun listenForLatestMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance("https://textingo-default-rtdb.europe-west1.firebasedatabase.app/").getReference("/latest-messages/$fromId")
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                latestMessagesMap[p0.key!!] = chatMessage
                refreshRecyclerViewMessages()
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                latestMessagesMap[p0.key!!] = chatMessage
                refreshRecyclerViewMessages()
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }
            override fun onChildRemoved(p0: DataSnapshot) {

            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun refreshRecyclerViewMessages() {
        latestMessagesList.clear()
        latestMessagesMap.values.forEach {
            latestMessagesList.add(it)
        }
        adapter.notifyDataSetChanged()
        Log.i("size: ", latestMessagesList.get(0).text)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_new_message -> {
                val intent = Intent(this, NewMessageActivity::class.java)
                intent.putExtra(Constants.CURRENT_USER_KEY_ADAPTER, currentUser)
                startActivity(intent)
            }
            R.id.menu_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, RegisterActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}