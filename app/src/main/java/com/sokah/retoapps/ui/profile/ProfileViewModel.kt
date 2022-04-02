package com.sokah.retoapps.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sokah.retoapps.RetoAppsAplication
import com.sokah.retoapps.User

class ProfileViewModel : ViewModel() {

    private var user_ = MutableLiveData<User>()

    var user :LiveData<User> = user_
    init {

        getUser()
    }

    private fun getUser() {

        user_.postValue(RetoAppsAplication.prefs.getLoggedUser())
    }

    fun updateUser(loggedUser: User) {

        RetoAppsAplication.prefs.updateUser(loggedUser)
    }


}