package com.example.runandremember.recyclerview

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.runandremember.Sport
import com.example.runandremember.databinding.ItemTrainingAdapterBinding

class ItemTrainingAdapter(
    private val items: ArrayList<Sport>,
    private val onClick: (Sport) -> Unit,
    private val onDelete: (Sport) -> Unit
) : RecyclerView.Adapter<ItemTrainingAdapter.TrainingViewHolder>() {

    inner class TrainingViewHolder(private val binding: ItemTrainingAdapterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(sport: Sport) {
            binding.nickImageDepor3.setImageURI(Uri.parse(sport.image))
            binding.nickSport3.text = sport.name
            binding.nickHours3.text = sport.time
            binding.nickDesc3.text = sport.description
            binding.nickDay3.text = sport.day
            binding.root.setOnClickListener { onClick(sport) }
            binding.btnDelEntre.setOnClickListener { onDelete(sport) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingViewHolder {
        val binding = ItemTrainingAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrainingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrainingViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
