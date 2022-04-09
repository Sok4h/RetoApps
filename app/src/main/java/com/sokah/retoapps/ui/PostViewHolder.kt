package com.sokah.retoapps.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.text.format.DateFormat
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.sokah.retoapps.RetoAppsAplication
import com.sokah.retoapps.databinding.PostCardBinding
import com.sokah.retoapps.model.Post
import java.time.Month
import java.time.format.TextStyle
import java.util.*


class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding = PostCardBinding.bind(view)


    fun bind(post: Post) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var monthNumber = Month.of(post.date.get(Calendar.MONTH) + 1)
            val day = post.date.get(Calendar.DAY_OF_MONTH).toString()
            val month = monthNumber.getDisplayName(TextStyle.SHORT, Locale.getDefault())
            val year = post.date.get(Calendar.YEAR).toString()


            binding.tvPost.text = post.postMessage
            binding.tvCityPost.text = post.city
            binding.tvPostDate.text = day + " " + month + " " + year
        } else {
            val day = DateFormat.format("dd", post.date.time) as String // 20
            val monthString = DateFormat.format("MMM", post.date.time) as String // Jun
            val year = DateFormat.format("yyyy", post.date.time) as String // 2013

            binding.tvCityPost.text = post.city
            binding.tvPostDate.text = day + " " + monthString + " " + year
        }

        val bitmap = BitmapFactory.decodeFile(post.img)
        var user = RetoAppsAplication.prefs.getUserById(post.userId)
        binding.tvPost.text = post.postMessage
        binding.tvPostName.text = user.name
        binding.imgProfilePost.setImageBitmap(BitmapFactory.decodeFile(user.picture))
        binding.imgPostCard.setImageBitmap(bitmap)


    }
}
