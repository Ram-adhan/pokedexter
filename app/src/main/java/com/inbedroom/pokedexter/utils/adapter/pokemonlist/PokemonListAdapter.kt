package com.inbedroom.pokedexter.utils.adapter.pokemonlist

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.color.utilities.CorePalette
import com.inbedroom.pokedexter.databinding.ItemPokemonBinding
import com.inbedroom.pokedexter.databinding.ItemPokemonGridBinding
import kotlin.math.log

class PokemonListAdapter(
    private val layoutManager: GridLayoutManager? = null,
    var onItemClickListener: ((item: PokemonModel) -> Unit)? = null
) : ListAdapter<PokemonModel, PokemonListAdapter.ViewHolder>(ModelDiffUtil()) {
    companion object {
        private const val GRID_ITEM = 1
        private const val LINEAR_ITEM = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            GRID_ITEM -> GridItemHolder(
                ItemPokemonGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> LinearItemHolder(
                ItemPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as? GridItemHolder)?.bind(currentList[position])
        (holder as? LinearItemHolder)?.bind(currentList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return if (layoutManager?.spanCount == 3) GRID_ITEM else LINEAR_ITEM
    }

    abstract class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class GridItemHolder(private val binding: ItemPokemonGridBinding) :
        ViewHolder(binding.root) {
        fun bind(item: PokemonModel) {
            binding.root.setOnClickListener(null)

            Glide.with(binding.ivSprite)
                .asBitmap()
                .load(item.sprite)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        Palette.from(resource).generate {
                            val lightTextColor = it?.lightVibrantSwatch?.titleTextColor
                            val lightVibrantRgb = it?.lightVibrantSwatch?.rgb

                            it?.dominantSwatch.let { color ->
                                val (textColor, bgColor) = if (color?.rgb != null && color.rgb < -15100000) {
                                    lightTextColor to lightVibrantRgb
                                } else {
                                    color?.titleTextColor to color?.rgb
                                }

                                textColor?.let { txtColor ->
                                    binding.tvName.setTextColor(txtColor)
                                    binding.tvPokedexEntry.setTextColor(txtColor)
                                }
                                bgColor?.let { backgroundColor ->
                                    binding.root.setCardBackgroundColor(backgroundColor)
                                }
                            }
                        }
                        binding.ivSprite.setImageBitmap(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }
                })

            binding.tvName.text = item.name
            binding.tvPokedexEntry.text = item.id.toString()

            binding.root.setOnClickListener { onItemClickListener?.invoke(item) }
        }
    }

    inner class LinearItemHolder(private val binding: ItemPokemonBinding) :
        ViewHolder(binding.root) {
        fun bind(item: PokemonModel) {
            binding.root.setOnClickListener(null)

            Glide.with(binding.ivSprite).load(item.sprite).into(binding.ivSprite)

            binding.tvName.text = item.name
            binding.tvPokedexEntry.text = item.id.toString()

            binding.root.setOnClickListener { onItemClickListener?.invoke(item) }
        }
    }
}