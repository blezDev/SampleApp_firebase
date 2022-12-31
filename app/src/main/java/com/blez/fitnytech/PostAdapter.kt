package com.blez.fitnytech

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.blez.fitnytech.databinding.PostLayoutBinding
import com.blez.fitnytech.model.Post
import com.bumptech.glide.Glide

class PostAdapter(val context: Context,val posts : List<Post>):RecyclerView.Adapter<PostAdapter.ItemView>() {
    private lateinit var binding: PostLayoutBinding

    inner class ItemView(binding : PostLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemView {
      binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.post_layout,parent,false)
        return ItemView(binding)
    }

    override fun onBindViewHolder(holder: ItemView, position: Int) {
       binding.usernameText.text = "From ${posts[position].username}"
        Glide.with(context).load(posts[position].image).into(binding.postImage)
        binding.descriptionText.text = posts[position].description
    }

    override fun getItemCount(): Int {
       return posts.size
    }
}