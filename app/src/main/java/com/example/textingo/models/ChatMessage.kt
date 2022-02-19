package com.example.textingo.models

class ChatMessage(val text: String, val fromId: String, val toId: String, val timestamp: Long) {
    constructor() : this( "", "", "", -1)
}