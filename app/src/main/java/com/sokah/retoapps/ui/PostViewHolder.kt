package com.sokah.retoapps.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.sokah.retoapps.databinding.PostCardBinding
import com.sokah.retoapps.model.Post
import java.time.Month
import java.time.format.TextStyle
import java.util.*


class PostViewHolder(view : View) :RecyclerView.ViewHolder(view) {

    val binding = PostCardBinding.bind(view)

    @SuppressLint("NewApi")
    fun bind(post : Post){
        val monthh = Month.of(post.date.get(Calendar.MONTH)+1)
        val day = post.date.get(Calendar.DAY_OF_MONTH).toString()
        val month = monthh.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        val year = post.date.get(Calendar.YEAR).toString()
        val bitmap = BitmapFactory.decodeFile(post.img)
        binding.tvPost.text=post.postMessage
        binding.imgPostCard.setImageBitmap(bitmap)
        binding.tvCityPost.text=post.city
        binding.tvPostDate.text = day +" " + " "+month+" " +year


    }
}
