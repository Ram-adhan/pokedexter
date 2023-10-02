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
import com.inbedroom.pokedexter.utils.LoadingHandler
import com.inbedroom.pokedexter.utils.LoadingHandlerImpl
import com.inbedroom.pokedexter.utils.adapter.pokemonlist.PokemonListAdapter
import com.inbedroom.pokedexter.utils.adapter.pokemonlist.PokemonModel
import com.inbedroom.pokedexter.utils.ui.DialogOptionInterface
import com.inbedroom.pokedexter.utils.ui.dialogEditOption
import kotlinx.coroutines.launch

class PokemonStorageActivity: AppCompatActivity(), LoadingHandler by LoadingHandlerImpl() {
    companion object {
        private const val SPAN_COUNT = 2
    }
    private lateinit var binding: ActivityListPokemonBinding
    private lateinit var adapter: PokemonListAdapter
    private val layoutManager: GridLayoutManager by lazy { GridLayoutManager(this, SPAN_COUNT) }
    private val viewModel: PokemonStorageViewModel by viewModels { PokemonStorageViewModel.Factory }

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

        binding.etSearch.doAfterTextChanged {
            viewModel.getCaughtPokemon(it.toString())
        }

        adapter.onItemClickListener = this::onPokemonItemClick
        adapter.onEditClickListener = this::onEditPokemon

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

    override fun onResume() {
        super.onResume()
        viewModel.getCaughtPokemon("")
    }

    private fun onEditPokemon(pokemonModel: PokemonModel) {
        dialogEditOption(object : DialogOptionInterface {
            override fun onPositive() {
                viewModel.renamePokemon(pokemonModel)
            }

            override fun onNegative() {
                viewModel.releasePokemon(pokemonModel)
            }
        }).show()
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
            is PokemonListUiState.SuccessReleasePokemon -> {
                Toast.makeText(this, "${state.name} is Released", Toast.LENGTH_SHORT).show()
                viewModel.getCaughtPokemon(binding.etSearch.text.toString())
            }
            is PokemonListUiState.Error -> {
                Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
            }
            is PokemonListUiState.SuccessRenamePokemon -> {
                Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                viewModel.getCaughtPokemon(binding.etSearch.text.toString())
            }
            else -> {}
        }
    }
}