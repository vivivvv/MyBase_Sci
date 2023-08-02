package com.app.mybase

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.mybase.room.UserEntity

class UserAdapter : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private var usersList = ArrayList<UserEntity>()

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<UserEntity>) {
        usersList.clear()
        usersList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.user_rv_item,
            parent, false
        )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = usersList[position].name
        holder.email.text = usersList[position].email
        holder.number.text = usersList[position].phoneNumber
        holder.gender.text = usersList[position].gender
    }

    override fun getItemCount(): Int {
        return usersList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name = itemView.findViewById<TextView>(R.id.name)
        var email = itemView.findViewById<TextView>(R.id.email)
        var number = itemView.findViewById<TextView>(R.id.number)
        var gender = itemView.findViewById<TextView>(R.id.gender)
    }

}