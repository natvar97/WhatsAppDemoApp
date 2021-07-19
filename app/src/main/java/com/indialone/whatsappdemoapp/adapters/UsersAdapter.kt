package com.indialone.whatsappdemoapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.indialone.whatsappdemoapp.ChatDetailActivity
import com.indialone.whatsappdemoapp.R
import com.indialone.whatsappdemoapp.databinding.SampleShowUserBinding
import com.indialone.whatsappdemoapp.models.User

class UsersAdapter(
    private val context: Context,
    private val users: ArrayList<User>
) : RecyclerView.Adapter<UsersAdapter.UsersViewHolder>() {
    class UsersViewHolder(itemView: SampleShowUserBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val image = itemView.ivProfile
        val username = itemView.tvUsername
        val lastMessage = itemView.tvLastMessage

        fun bind(user: User) {
            username.text = user.userName
            lastMessage.text = user.lastMessage
            Glide.with(itemView.context)
                .load(user.profileImage)
                .placeholder(R.drawable.place_holder)
                .error(R.drawable.place_holder)
                .centerCrop()
                .into(image)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view = SampleShowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UsersViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bind(users[position])
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context , ChatDetailActivity::class.java)
            intent.putExtra("userId", users[position].userId)
            intent.putExtra("profilePic", users[position].profileImage)
            intent.putExtra("userName", users[position].userName)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }
}