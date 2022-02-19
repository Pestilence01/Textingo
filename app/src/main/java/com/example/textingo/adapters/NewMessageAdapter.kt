package com.example.textingo.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.textingo.constants.Constants
import com.example.textingo.R
import com.example.textingo.messages.ChatLogActivity
import com.example.textingo.models.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.user_row_new_message.view.*


open class NewMessageAdapter(
    private val context: Context,
    private var list: ArrayList<User>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.user_row_new_message,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if(holder is MyViewHolder){
            val bitmap = model.profileImageUrl
            val username = model.username
            holder.itemView.username_textview_new_message.text = username
            Picasso.get().load(bitmap).into(holder.itemView.imageview_new_message)

            holder.itemView.setOnClickListener {
                val intent = Intent(context, ChatLogActivity::class.java)
                intent.putExtra(Constants.NEW_MESSAGE_USER_KEY, model)
                context.startActivity(intent)
                (context as Activity).finish()
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}