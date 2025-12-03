package com.example.runandremember.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.runandremember.Planning
import com.example.runandremember.databinding.ItemPlanAdapterBinding

class ItemPlanAdapter(
    private val items: ArrayList<Planning>,
    private val onClick: (Planning) -> Unit,
    private val onDelete: (Planning) -> Unit
) : RecyclerView.Adapter<ItemPlanAdapter.PlanViewHolder>() {

    inner class PlanViewHolder(private val binding: ItemPlanAdapterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(planning: Planning) {
            binding.nickPlan2.text = planning.descplan
            binding.btnEdit3Entre.setOnClickListener { onClick(planning) }
            binding.btnDel3Entre.setOnClickListener { onDelete(planning) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val binding = ItemPlanAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
