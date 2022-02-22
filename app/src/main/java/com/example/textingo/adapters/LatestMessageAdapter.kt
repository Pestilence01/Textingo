package com.example.textingo.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.textingo.R
import com.example.textingo.constants.Constants
import com.example.textingo.messages.ChatLogActivity
import com.example.textingo.models.ChatMessage
import com.example.textingo.models.User
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
            }

            holder.itemView.setOnClickListener {
                // TODO START ChatLogActivity
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}