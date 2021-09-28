package com.example.a2in1app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_row.view.*

class GuessedNumberAdapter (val context: Context, val log_text: ArrayList<String>):
    RecyclerView.Adapter<GuessedNumberAdapter.ItemViewHolder>() {
    class ItemViewHolder(ItemView: View): RecyclerView.ViewHolder(ItemView)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val text = log_text[position]

        holder.itemView.apply {
            user_guss_tv.text = text
        }
    }
    override fun getItemCount(): Int = log_text.size
}