package com.example.demoapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.demoapp.databinding.AdapterLayoutBinding

class MainAdapter(context: Context) : RecyclerView.Adapter<ViewHolder>() {
    var list = mutableListOf<String>()
    var listener: ItemClickListener

    init {
        listener = context as ItemClickListener
    }

    fun setDataList(listItem: List<String>) {
        this.list = listItem.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterLayoutBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.binding.name.text = "charcters_$position"
        holder.binding.url.text = "url:$item"

        holder.itemView.setOnClickListener(View.OnClickListener {
            listener.onListItemClick(item)
        })
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ItemClickListener {
        fun onListItemClick(
            data: String
        )
    }

}


class ViewHolder(val binding: AdapterLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

}