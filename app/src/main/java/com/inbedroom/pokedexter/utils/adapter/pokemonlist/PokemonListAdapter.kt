package com.inbedroom.pokedexter.utils.adapter.pokemonlist

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.inbedroom.pokedexter.data.pokemonservice.PokemonRepository
import com.inbedroom.pokedexter.databinding.ItemPokemonBinding
import com.inbedroom.pokedexter.databinding.ItemPokemonGridBinding

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
        return if (layoutManager?.spanCount == 1) LINEAR_ITEM else GRID_ITEM
    }

    abstract class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class GridItemHolder(private val binding: ItemPokemonGridBinding) :
        ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
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
            binding.tvPokedexEntry.text = "#" + item.id.toString().padStart(PokemonRepository.maxPad, '0')
            binding.tvNickName.isVisible = item.nickname.isNotEmpty()
            binding.tvNickName.text = item.nickname

            binding.root.setOnClickListener { onItemClickListener?.invoke(item) }
        }
    }

    inner class LinearItemHolder(private val binding: ItemPokemonBinding) :
        ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: PokemonModel) {
            binding.root.setOnClickListener(null)

            Glide.with(binding.ivSprite).load(item.sprite).into(binding.ivSprite)

            val name = if (item.nickname.isNotEmpty()) {
                binding.edit.isVisible = true
                "${item.name}/${item.nickname}"
            } else {
                binding.edit.isVisible = false
                item.name
            }
            binding.tvName.text = name
            binding.tvPokedexEntry.text = "#" + item.id.toString().padStart(PokemonRepository.maxPad, '0')

            binding.root.setOnClickListener { onItemClickListener?.invoke(item) }
        }
    }
}