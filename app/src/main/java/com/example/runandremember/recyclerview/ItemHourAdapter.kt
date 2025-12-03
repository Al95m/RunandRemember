package com.example.runandremember.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.runandremember.Hour
import com.example.runandremember.databinding.ItemHourAdapterBinding

class ItemHourAdapter(
    private val items: ArrayList<Hour>,
    private val onClick: (Hour) -> Unit,
    private val onDelete: (Hour) -> Unit
) : RecyclerView.Adapter<ItemHourAdapter.HourViewHolder>() {

    inner class HourViewHolder(private val binding: ItemHourAdapterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(hour: Hour) {
            binding.nickHour3.text = hour.hourtime
            binding.root.setOnClickListener { onClick(hour) }
            binding.btnDel2Entre.setOnClickListener { onDelete(hour) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourViewHolder {
        val binding = ItemHourAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HourViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HourViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
