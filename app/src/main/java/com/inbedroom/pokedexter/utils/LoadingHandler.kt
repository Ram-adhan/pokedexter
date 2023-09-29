package com.inbedroom.pokedexter.utils

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.inbedroom.pokedexter.R

interface LoadingHandler {
    fun initializeLoadingDialog(context: AppCompatActivity)
    fun showProgress()
    fun dismissProgress()
    fun setProgressVisibility(isVisible: Boolean)

    fun stackProgress(isAdd: Boolean = true) {

    }
}

class LoadingHandlerImpl : LoadingHandler, LifecycleEventObserver {
    private var loadingDialog: Dialog? = null
    private var progressStack: Int = 0
        set(value) {
            field = value
            if (value > 0) showProgress() else dismissProgress()
        }

    override fun initializeLoadingDialog(context: AppCompatActivity) {
        loadingDialog = Dialog(context)
        loadingDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        loadingDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        loadingDialog?.setContentView(R.layout.loading_layout)
        loadingDialog?.setCancelable(false)

        context.lifecycle.addObserver(this)
    }

    override fun showProgress() {
        loadingDialog?.show()
    }

    override fun dismissProgress() {
        loadingDialog?.dismiss()
    }

    override fun setProgressVisibility(isVisible: Boolean) {
        if (isVisible) {
            showProgress()
        } else {
            dismissProgress()
        }
    }

    override fun stackProgress(isAdd: Boolean) {
        if (isAdd) progressStack++ else {
            progressStack--
            if (progressStack <= 0) {
                progressStack = 0
            }
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_DESTROY -> loadingDialog?.dismiss()
            else -> {}
        }
    }
}