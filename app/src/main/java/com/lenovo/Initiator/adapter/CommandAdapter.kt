package com.lenovo.Initiator.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.lenovo.Initiator.R

class CommandAdapter(context: Context, data: List<String>) : RecyclerView.Adapter<CommandAdapter.ItemHolder>() {
    private var context: Context
    private var data: List<String>

    init {
        this.context = context
        this.data = data
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ItemHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.command_item, null);

        return ItemHolder(view);
    }

    override fun getItemCount(): Int {
        if (data.size > 0) return data.size else return 0
    }

    override fun onBindViewHolder(p0: ItemHolder, p1: Int) {
        p0.textView.text = data.get(p1)
    }


    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView

        init {
            textView = itemView.findViewById(R.id.command)
        }
    }
}
