package com.musttool.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.musttool.R
import com.musttool.models.AppModel
import com.musttool.utils.Utils
import kotlin.random.nextInt

class AppAdapter(var list: ArrayList<AppModel>, var context: Context) : RecyclerView.Adapter<AppAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.litem_list, parent, false)
        return ViewHolder(v)
    }
    override fun getItemCount(): Int {
        return list.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemTitle.text = list[position].title
        Glide.with(context).load(list[position].img).into(holder.itemImage)

        holder.itemView.setOnClickListener {
            val intent = Utils.getIntent(context,position)
            intent?.putExtra("book", list[position].title)
            holder.itemView.context.startActivity(intent)
            var randomValue = kotlin.random.Random.nextInt(0..10)
            Utils.getRandomAnimation(randomValue,context)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView = itemView.findViewById(R.id.image)
        var itemTitle: TextView = itemView.findViewById(R.id.tv)
    }

}