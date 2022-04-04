package com.sokah.retoapps.ui.publish

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sokah.retoapps.RetoAppsAplication
import com.sokah.retoapps.RetoAppsAplication.Companion.prefs
import com.sokah.retoapps.SharedPreferences
import com.sokah.retoapps.model.Post
import com.sokah.retoapps.model.User

class PublishViewModel : ViewModel() {


    fun uploadPost(post: Post){

        prefs.savePost(post)
    }

     fun getUser():User {

        return prefs.getLoggedUser()!!
    }

}