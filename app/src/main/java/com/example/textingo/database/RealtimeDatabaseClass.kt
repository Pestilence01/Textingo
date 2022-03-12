package com.example.textingo.database

import com.google.firebase.auth.FirebaseAuth

class RealtimeDatabaseClass {

    fun getCurrentUserID(): String {
        var currentUser = FirebaseAuth.getInstance().currentUser
        var currentID = ""
        if(currentUser != null){
            currentID = currentUser.uid
        }

        return currentID
    }

}