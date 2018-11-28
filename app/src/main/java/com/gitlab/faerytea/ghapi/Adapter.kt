package com.gitlab.faerytea.ghapi

import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.gitlab.faerytea.ghapi.api.User
import com.squareup.picasso.Picasso

import java.util.Collections


class Adapter : RecyclerView.Adapter<Adapter.Holder>() {
    private var users = emptyList<User>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): Holder {
        return Holder(
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.user, viewGroup, false)
        )
    }

    override fun onBindViewHolder(holder: Holder, i: Int) {
        val user = users[i]
        holder.bind(user.userpic, user.login)
    }

    override fun getItemCount(): Int = users.size

    fun setData(users: List<User>) {
        this.users = users
        notifyDataSetChanged()
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val pic: ImageView = itemView.findViewById(R.id.pic)
        private val name: TextView = itemView.findViewById(R.id.name)

        fun bind(link: String?, username: CharSequence) {
            Log.d("ADAPTER", "bind: link=$link")
            Picasso.get().load(link)
                .tag(MainActivity::class.java)
                .placeholder(R.color.colorAccent)
                .error(R.color.colorPrimaryDark)
                .into(pic)
            name.text = username
        }
    }
}
