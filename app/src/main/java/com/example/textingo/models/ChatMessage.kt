package com.example.textingo.models

class ChatMessage(val text: String, val fromId: String, val toId: String, val timestamp: Long, val senderUrl: String, val receiverUrl: String) {
    constructor() : this( "", "", "", -1, "", "")
}