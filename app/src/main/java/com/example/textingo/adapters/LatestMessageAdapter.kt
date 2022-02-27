package com.example.textingo.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.textingo.R
import com.example.textingo.constants.Constants
import com.example.textingo.messages.ChatLogActivity
import com.example.textingo.models.ChatMessage
import com.example.textingo.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.latest_message_row.view.*

class LatestMessageAdapter(
    private val context: Context,
    private var list: ArrayList<ChatMessage>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return LatestMessageAdapter.MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.latest_message_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if(holder is LatestMessageAdapter.MyViewHolder){

            if(holder is MyViewHolder){
                holder.itemView.message_textview_latest_message.text = model.text
                if(FirebaseAuth.getInstance().uid == model.fromId){
                    Picasso.get().load(model.receiverUrl).into(holder.itemView.imageview_latest_message)


                    FirebaseDatabase.getInstance("https://textingo-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users").child(model.toId).addListenerForSingleValueEvent(object:
                        ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val currentUser = snapshot.getValue(User::class.java)
                            holder.itemView.username_textview_latest_message.text = currentUser!!.username
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })


                } else {
                    Picasso.get().load(model.senderUrl).into(holder.itemView.imageview_latest_message)

                    FirebaseDatabase.getInstance("https://textingo-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users").child(model.fromId).addListenerForSingleValueEvent(object:
                        ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val currentUser = snapshot.getValue(User::class.java)
                            holder.itemView.username_textview_latest_message.text = currentUser!!.username
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
                }
            }

            holder.itemView.setOnClickListener {
                FirebaseDatabase.getInstance("https://textingo-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users").child(model.fromId).addListenerForSingleValueEvent(object:
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val currentUser = snapshot.getValue(User::class.java)
                        val intent = Intent(context, ChatLogActivity::class.java)
                        intent.putExtra(Constants.NEW_MESSAGE_USER_KEY, currentUser)
                        context.startActivity(intent)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}