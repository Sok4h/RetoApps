package com.sokah.retoapps.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sokah.retoapps.R
import com.sokah.retoapps.model.Post

class PostAdapter() : RecyclerView.Adapter<PostViewHolder>() {

    var postList = mutableListOf<Post>()


    fun addPost(post: Post) {

        postList.add(post)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)

        return PostViewHolder(layoutInflater.inflate(R.layout.post_card,parent,false))
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {

        holder.bind(postList[position])
    }

    fun clear(){

        postList.clear()
    }
    override fun getItemCount(): Int {

         return postList.size
    }
}