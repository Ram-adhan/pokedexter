package com.inbedroom.pokedexter.utils.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.core.view.marginRight
import com.google.android.material.card.MaterialCardView
import com.inbedroom.pokedexter.databinding.ItemPokemonTypeBinding

class PokemonTypeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): MaterialCardView(context, attrs, defStyleAttr) {
    private val binding = ItemPokemonTypeBinding.inflate(LayoutInflater.from(context), this, true)
    fun setText(text: String) {
        binding.tvType.text = text
    }

    fun setBackgroundColor(@ColorInt bg: Int, @ColorInt stroke: Int? = null) {
        binding.root.setCardBackgroundColor(bg)
        stroke?.let {
            binding.root.strokeColor = it
        }
    }

    fun setMarginRight(margin: Int) {
        (binding.root.layoutParams as? LayoutParams)?.apply {
            rightMargin = margin
        }
    }
}