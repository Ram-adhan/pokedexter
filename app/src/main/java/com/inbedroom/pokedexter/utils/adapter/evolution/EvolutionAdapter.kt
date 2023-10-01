package com.inbedroom.pokedexter.utils.adapter.evolution

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.inbedroom.pokedexter.databinding.ItemEvolutionBinding
import com.inbedroom.pokedexter.features.model.EvolutionData
import com.inbedroom.pokedexter.features.model.EvolutionType

class EvolutionAdapter(private var list: List<EvolutionData>): RecyclerView.Adapter<EvolutionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemEvolutionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.count()

    inner class ViewHolder(private val binding: ItemEvolutionBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: EvolutionData) {
            Glide.with(itemView.context)
                .load(item.sprite)
                .into(binding.ivSprite)

            if (item.evolutionType == null) {
                binding.arrowLvUp.isVisible = false
                binding.tvLvUp.isVisible = false
            } else {
                if (item.evolutionType is EvolutionType.LevelUp) {
                    val withLevel = !item.evolutionType.specialTrigger
                    val text =if (withLevel) {
                        "at lv ${item.evolutionType.at}"
                    } else {
                        "Lv Up with special trigger"
                    }
                    binding.tvLvUp.text = text
                }
            }
        }
    }
}