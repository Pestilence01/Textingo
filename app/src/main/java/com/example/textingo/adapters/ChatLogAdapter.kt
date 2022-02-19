package com.example.textingo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.textingo.R
import com.example.textingo.constants.Constants
import com.example.textingo.models.ChatMessage
import com.google.firebase.auth.FirebaseAuth


class ChatLogAdapter(
    private val context: Context,
    private var list: ArrayList<ChatMessage>,
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == Constants.ITEM_SENT){
            val view: View = LayoutInflater.from(context).inflate(R.layout.chat_to_row, parent, false)
            SentViewHolder(view)
        }else{
            val view: View = LayoutInflater.from(context).inflate(R.layout.chat_from_row, parent, false)
            ReceivedViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = list[position]

        return if(FirebaseAuth.getInstance().uid.equals(currentMessage.fromId)){
            Constants.ITEM_SENT
        } else {
            Constants.ITEM_RECEIVED
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder.javaClass == SentViewHolder::class.java){
            val viewHolder = holder as SentViewHolder
            holder.sentMessage.text = list[position].text.toString()
        }else{
            val viewHolder = holder as ReceivedViewHolder
            viewHolder.receivedMessage.text = list[position].text.toString()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class SentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val sentMessage: TextView = itemView.findViewById<TextView>(R.id.textViewToRow)
    }

    class ReceivedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val receivedMessage: TextView = itemView.findViewById<TextView>(R.id.textViewFromRow)
    }
}