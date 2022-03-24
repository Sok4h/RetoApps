package com.sokah.retoapps

import android.content.Context
import android.util.Log
import com.google.gson.Gson

class SharedPreferences(val context: Context) {

    val db = context.getSharedPreferences("DB", Context.MODE_PRIVATE)
    val gson = Gson()
    val loggedUser ="LOGGED_USER"

    fun saveLoggedUser(userCode:String){

         db.edit().putString(loggedUser, userCode).apply()


    }

    fun getLoggedUser(): User? {

        val userCode = db.getString(loggedUser,"")
        Log.e("xd", db.getString(loggedUser,"").toString() )

        if(userCode != null){
            val userString = db.getString(userCode,"")
             Log.e("a", userString!! )
            Log.e("b", userCode )
            return gson.fromJson(userString, User::class.java)
        }

        else return null
    }

    fun createUser(){

        if(db.getString("USER_1","").toString().isEmpty()||db.getString("USER_2","").toString().isEmpty()){

            val user = User("Pepito Perez","xd")
            val user2 = User("Sokah","xd")
            val userString = gson.toJson(user)
            val userString2 = gson.toJson(user2)
            db.edit().putString("USER_1", userString).apply()
            db.edit().putString("USER_2", userString2).apply()
        }
        else return

    }

    fun logOut(){

        db.edit().remove(loggedUser).apply()
    }
}