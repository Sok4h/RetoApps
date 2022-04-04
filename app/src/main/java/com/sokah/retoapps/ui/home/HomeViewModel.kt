package com.sokah.retoapps.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sokah.retoapps.RetoAppsAplication.Companion.prefs
import com.sokah.retoapps.model.Post

class HomeViewModel : ViewModel() {

    private var postList_ = MutableLiveData<MutableList<Post>>()
     var postList  : LiveData<MutableList<Post>> = postList_



    fun getPostList(){

        var posts = prefs.getPost()
        postList_.postValue(posts)
    }
}