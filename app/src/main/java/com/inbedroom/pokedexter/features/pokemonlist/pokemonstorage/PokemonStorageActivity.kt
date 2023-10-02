package com.inbedroom.pokedexter.features.pokemonlist.pokemonstorage

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.inbedroom.pokedexter.databinding.ActivityListPokemonBinding
import com.inbedroom.pokedexter.features.pokemondetail.PokemonDetailActivity
import com.inbedroom.pokedexter.features.pokemonlist.PokemonListUiState
import com.inbedroom.pokedexter.features.pokemonlist.PokemonListViewModel
import com.inbedroom.pokedexter.utils.LoadingHandler
import com.inbedroom.pokedexter.utils.LoadingHandlerImpl
import com.inbedroom.pokedexter.utils.adapter.pokemonlist.PokemonListAdapter
import com.inbedroom.pokedexter.utils.adapter.pokemonlist.PokemonModel
import kotlinx.coroutines.launch

class PokemonStorageActivity: AppCompatActivity(), LoadingHandler by LoadingHandlerImpl() {
    companion object {
        private const val SPAN_COUNT = 2
    }
    private lateinit var binding: ActivityListPokemonBinding
    private lateinit var adapter: PokemonListAdapter
    private val layoutManager: GridLayoutManager by lazy { GridLayoutManager(this, SPAN_COUNT) }
    private val viewModel: PokemonListViewModel by viewModels { PokemonListViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListPokemonBinding.inflate(layoutInflater)
        setContentView(binding.root)


        adapter = PokemonListAdapter(layoutManager)
        binding.rvList.apply {
            adapter = this@PokemonStorageActivity.adapter
            layoutManager = this@PokemonStorageActivity.layoutManager
        }

        initializeLoadingDialog(this)

        lifecycleScope.launch {
            viewModel.uiState
                .flowWithLifecycle(lifecycle)
                .collect { state -> stateHandler(state) }
        }

        viewModel.getCaughtPokemon("")

        binding.etSearch.doAfterTextChanged {
            viewModel.getCaughtPokemon(it.toString())
        }

        adapter.onItemClickListener = this::onPokemonItemClick

        binding.toggleChange.isVisible = false
    }

    private fun onPokemonItemClick(pokemonModel: PokemonModel) {
        startActivity(
            PokemonDetailActivity.newIntent(
                context = this,
                id = pokemonModel.id
            )
        )
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