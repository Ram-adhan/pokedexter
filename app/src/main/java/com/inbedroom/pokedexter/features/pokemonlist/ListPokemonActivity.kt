package com.inbedroom.pokedexter.features.pokemonlist

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.inbedroom.pokedexter.R
import com.inbedroom.pokedexter.databinding.ActivityListPokemonBinding
import com.inbedroom.pokedexter.features.pokemondetail.PokemonDetailActivity
import com.inbedroom.pokedexter.features.pokemonlist.pokemonstorage.PokemonStorageActivity
import com.inbedroom.pokedexter.utils.LoadingHandler
import com.inbedroom.pokedexter.utils.LoadingHandlerImpl
import com.inbedroom.pokedexter.utils.adapter.pokemonlist.PokemonListAdapter
import com.inbedroom.pokedexter.utils.adapter.pokemonlist.PokemonModel
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

        adapter.onItemClickListener = this::onPokemonItemClick

        addMenuProvider(menuProvider)

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

    private val menuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.main_menu, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
                R.id.viewPokemonStorage -> {
                    startActivity(
                        Intent(
                            this@ListPokemonActivity,
                            PokemonStorageActivity::class.java
                        )
                    )
                    true
                }
                else -> false
            }
        }
    }
    private fun startToggleAnimation(animator: ValueAnimator) {
        animator.addUpdateListener {
            binding.toggleChange.progress = it.animatedValue as Float
        }
        animator.start()
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