package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(val itemsList: List<String>, val longClickListener: LongClickListener):
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    interface LongClickListener {
        fun onItemLongClicked(adapterPosition: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(android.R.layout.simple_list_item_1,parent,false)
        return ViewHolder(contactView)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val textView: TextView
        init {
            textView = itemView.findViewById(android.R.id.text1)

            itemView.setOnClickListener() {
                longClickListener.onItemLongClicked(adapterPosition)
                true
            }
        }

    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    override fun onBindViewHolder(holder: ItemAdapter.ViewHolder, position: Int) {
        val item: String = itemsList.get(position)
        holder.textView.text = item

    }
}

