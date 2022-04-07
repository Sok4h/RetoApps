package com.sokah.retoapps.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sokah.retoapps.R
import com.sokah.retoapps.model.Post

class PostAdapter() : RecyclerView.Adapter<PostViewHolder>() {

    var postList = mutableListOf<Post>()


    fun addPosts(posts: MutableList<Post>) {

        val diff_util = PostDiffUtil(postList,posts)
        val diff_result = DiffUtil.calculateDiff(diff_util)
        diff_result.dispatchUpdatesTo(this)

        postList=posts
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


    class PostDiffUtil(

        private val oldList: MutableList<Post>,
        private val newList: MutableList<Post>

    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {

            return oldList[oldItemPosition].id.contentEquals(newList[newItemPosition].id)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {

            return oldList[oldItemPosition].postMessage.contentEquals(newList[newItemPosition].postMessage)
        }

    }
}