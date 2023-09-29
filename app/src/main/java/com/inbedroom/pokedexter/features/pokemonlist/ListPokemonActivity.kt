package com.inbedroom.pokedexter.features.pokemonlist

import android.animation.ValueAnimator
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.inbedroom.pokedexter.databinding.ActivityListPokemonBinding
import com.inbedroom.pokedexter.utils.LoadingHandler
import com.inbedroom.pokedexter.utils.LoadingHandlerImpl
import com.inbedroom.pokedexter.utils.adapter.pokemonlist.PokemonListAdapter
import kotlinx.coroutines.launch

class ListPokemonActivity : AppCompatActivity(), LoadingHandler by LoadingHandlerImpl() {

    private lateinit var binding: ActivityListPokemonBinding
    private lateinit var adapter: PokemonListAdapter
    private val layoutManager: GridLayoutManager by lazy { GridLayoutManager(this, 3) }
    private val viewModel: PokemonListViewModel by viewModels { PokemonListViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListPokemonBinding.inflate(layoutInflater)
        setContentView(binding.root)


        adapter = PokemonListAdapter(layoutManager)
        binding.rvList.apply {
            adapter = this@ListPokemonActivity.adapter
            layoutManager = this@ListPokemonActivity.layoutManager
        }

        initializeLoadingDialog(this)

        lifecycleScope.launch {
            viewModel.uiState
                .flowWithLifecycle(lifecycle)
                .collect { state -> stateHandler(state) }
        }

        viewModel.getPokemonList()

        binding.etSearch.doAfterTextChanged {
            viewModel.getPokemonList(it.toString())
        }

        binding.toggleChange.setOnClickListener {
            if (layoutManager.spanCount == 3) {
                val animator = ValueAnimator.ofFloat(0.3f, 0.5f)
                animator.duration = 700
                startToggleAnimation(animator)
                layoutManager.spanCount = 1
                adapter.notifyItemRangeChanged(0, adapter.currentList.size)
            } else {
                val animator = ValueAnimator.ofFloat(0.5f, 0f)
                animator.duration = 1200
                startToggleAnimation(animator)
                layoutManager.spanCount = 3
                binding.rvList.requestLayout()
                adapter.notifyItemRangeChanged(0, adapter.currentList.size)
            }
        }
    }

    private fun startToggleAnimation(animator: ValueAnimator) {
        animator.addUpdateListener {
            binding.toggleChange.progress = it.animatedValue as Float
        }
        animator.start()
    }

    private fun stateHandler(state: PokemonListUiState) {
        setProgressVisibility(state is PokemonListUiState.Loading)
        when (state) {
            is PokemonListUiState.SuccessGetPokemonList -> {
                adapter.submitList(state.data)
            }
            is PokemonListUiState.ErrorGetPokemonList -> {
                Toast.makeText(this, "error code -> ${state.code}", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }
}