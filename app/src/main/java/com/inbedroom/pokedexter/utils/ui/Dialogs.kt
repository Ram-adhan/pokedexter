package com.inbedroom.pokedexter.utils.ui

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import androidx.core.view.isVisible
import com.inbedroom.pokedexter.R
import com.inbedroom.pokedexter.databinding.CustomDialogAskBinding
import com.inbedroom.pokedexter.databinding.CustomDialogOptionBinding

interface DialogEditNameInterface {
    fun onApply(newName: String, pokemonId: Int = 0)
    fun onCancel() {}
}

interface DialogOptionInterface {
    fun onPositive()
    fun onNegative()
}
fun Context.addDialogSuccessCatch(pokemonName: String, listener: DialogEditNameInterface? = null): Dialog {
    val dialog = Dialog(this, R.style.DialogSlideAnimFullWidth)
    val binding = CustomDialogAskBinding.bind(LayoutInflater.from(this).inflate(R.layout.custom_dialog_ask, null))

    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setContentView(binding.root)
    dialog.setCancelable(false)

    binding.tvDescription.text = getString(R.string.you_caught_a_pokemon, pokemonName)

    binding.btnDialogNegative.setOnClickListener {
        listener?.onCancel()
        dialog.dismiss()
    }

    binding.btnDialogPositive.setOnClickListener {
        listener?.onApply(binding.etDialogContent.text.toString())
        dialog.dismiss()
    }

    return dialog
}

fun Context.dialogEditOption(listener: DialogOptionInterface? = null): Dialog {
    val dialog = Dialog(this, R.style.DialogSlideAnimFullWidth)
    val binding = CustomDialogOptionBinding.bind(LayoutInflater.from(this).inflate(R.layout.custom_dialog_option, null))

    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setContentView(binding.root)
    dialog.setCancelable(false)

    binding.btnDialogNegative.setOnClickListener {
        listener?.onNegative()
        dialog.dismiss()
    }

    binding.btnDialogPositive.setOnClickListener {
        listener?.onPositive()
        dialog.dismiss()
    }

    return dialog
}