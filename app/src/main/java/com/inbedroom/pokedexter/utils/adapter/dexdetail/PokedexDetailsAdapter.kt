package com.inbedroom.pokedexter.utils.adapter.dexdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.marginStart
import androidx.recyclerview.widget.RecyclerView
import com.inbedroom.pokedexter.R
import com.inbedroom.pokedexter.databinding.ItemPokemonDetailBinding
import com.inbedroom.pokedexter.features.pokemondetail.DetailValue
import com.inbedroom.pokedexter.features.pokemondetail.Details
import com.inbedroom.pokedexter.utils.ui.PokemonTypeView

class PokedexDetailsAdapter(private var currentList: MutableList<Details>): RecyclerView.Adapter<PokedexDetailsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemPokemonDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun getItemCount(): Int = currentList.count()

    inner class ViewHolder(private val binding: ItemPokemonDetailBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Details) {
            binding.title.text = item.name
            when (item.detail) {
                is DetailValue.StringValue -> {
                    binding.textValue.text = (item.detail.value)
                }
                is DetailValue.PokemonTypeValue -> {
                    binding.typeValue.removeAllViews()
                    item.detail.value.forEach { pokemonType ->
                        val tv = PokemonTypeView(itemView.context)
                        tv.setText(pokemonType.name)
                        tv.setBackgroundColor(pokemonType.bgColor, pokemonType.strokeColor)
                        tv.setMarginRight(itemView.context.resources.getDimensionPixelSize(R.dimen.type_margin))

                        binding.typeValue.addView(tv)
                    }
                }
            }
        }
    }
}