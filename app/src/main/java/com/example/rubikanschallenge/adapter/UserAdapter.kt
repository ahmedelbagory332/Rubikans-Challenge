package com.example.rubikanschallenge.adapter

import android.annotation.SuppressLint
import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rubikanschallenge.R
import com.example.rubikanschallenge.model.Users
import javax.inject.Inject


class UserAdapter @Inject constructor(private val ctx:Application) :
    ListAdapter<Users, UserAdapter.UsersViewHolder>(UsersDiffCallback) {

    var onItemClick : ((Users) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder =
        UsersViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
        )

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val user = getItem(position)

        Glide.with(ctx)
            .load(user.avatar)
             .into(holder.userProfilePhoto)

        holder.textViewName.text = user.firstName +" "+user.lastName
        holder.textViewEmail.text = user.email

        holder.itemView.setOnClickListener {
            onItemClick!!.invoke(user)
        }

    }


    class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.tvName)
        val textViewEmail: TextView = itemView.findViewById(R.id.tvEmail)
        val editUser: ImageButton = itemView.findViewById(R.id.editUser)
        val userProfilePhoto: ImageView = itemView.findViewById(R.id.profile_image)

    }


}

object UsersDiffCallback : DiffUtil.ItemCallback<Users>() {
    override fun areItemsTheSame(oldItem: Users, newItem: Users): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Users, newItem: Users): Boolean {
        return oldItem.id == newItem.id
    }
}