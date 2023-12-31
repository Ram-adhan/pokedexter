package com.inbedroom.pokedexter.features.pokemondetail

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.inbedroom.pokedexter.R
import com.inbedroom.pokedexter.databinding.ActivityPokemonDetailBinding
import com.inbedroom.pokedexter.utils.LoadingHandler
import com.inbedroom.pokedexter.utils.LoadingHandlerImpl
import com.inbedroom.pokedexter.utils.adapter.dexdetail.PokedexDetailsAdapter
import com.inbedroom.pokedexter.utils.adapter.evolution.EvolutionAdapter
import com.inbedroom.pokedexter.utils.ui.DialogEditNameInterface
import com.inbedroom.pokedexter.utils.ui.addDialogSuccessCatch
import kotlinx.coroutines.launch

class PokemonDetailActivity : AppCompatActivity(), LoadingHandler by LoadingHandlerImpl() {

    companion object {
        private const val ID_DATA = "idData"
        fun newIntent(context: Context, id: Int) = Intent(
            context,
            PokemonDetailActivity::class.java
        ).apply {
            putExtra(ID_DATA, id)
        }
    }

    private lateinit var binding: ActivityPokemonDetailBinding
    private val viewModel: PokemonDetailViewModel by viewModels { PokemonDetailViewModel.Factory }
    private val id: Int by lazy { intent.getIntExtra(ID_DATA, -1) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initializeLoadingDialog(this)

        observeState()

        binding.btnCatch.setOnClickListener {
            viewModel.catchOrReleasePokemon()
        }

        if (id > 0) {
            viewModel.getDetails(id)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }
    private fun observeState() {
        lifecycleScope.launch {
            viewModel.uiState
                .flowWithLifecycle(lifecycle)
                .collect { state ->
                    setProgressVisibility(state is PokemonDetailUiState.Loading)
                    when (state) {
                        is PokemonDetailUiState.SuccessGetDetail -> {
                            Glide.with(this@PokemonDetailActivity)
                                .asBitmap()
                                .load(state.data.artwork)
                                .addListener(object : RequestListener<Bitmap> {
                                    override fun onLoadFailed(
                                        e: GlideException?,
                                        model: Any?,
                                        target: Target<Bitmap>?,
                                        isFirstResource: Boolean
                                    ): Boolean {
                                        return false
                                    }

                                    override fun onResourceReady(
                                        resource: Bitmap?,
                                        model: Any?,
                                        target: Target<Bitmap>?,
                                        dataSource: DataSource?,
                                        isFirstResource: Boolean
                                    ): Boolean {
                                        binding.imagePlaceholder.isVisible = false
                                        return false
                                    }
                                })
                                .into(binding.ivArtwork)

                            binding.tvPokemonName.text =
                                state.data.name.replaceFirstChar { it.uppercase() }

                            supportActionBar?.title = binding.tvPokemonName.text
                            binding.rvDexData.apply {
                                adapter = PokedexDetailsAdapter(state.data.details.toMutableList())
                                layoutManager = LinearLayoutManager(this@PokemonDetailActivity)
                            }
                        }
                        is PokemonDetailUiState.SuccessGetEvolutionChain -> {
                            binding.tvNoEvolution.isVisible = false
                            Log.d("PokemonDetail", "evolutionChain: ${state.data}")
                            binding.rvEvolution.apply {
                                adapter = EvolutionAdapter(state.data)
                                layoutManager = LinearLayoutManager(
                                    this@PokemonDetailActivity,
                                    LinearLayoutManager.HORIZONTAL,
                                    false
                                )
                            }
                        }
                        is PokemonDetailUiState.PokemonAlreadyCaught -> {
                            supportActionBar?.title = state.pokemonName
                            binding.ivPokeball.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this@PokemonDetailActivity,
                                    R.drawable.colored_pokeball
                                )
                            )
                        }
                        is PokemonDetailUiState.SuccessCatchPokemon -> {
                            binding.sparkAnimation.playAnimation()
                            binding.ivPokeball.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this@PokemonDetailActivity,
                                    R.drawable.colored_pokeball
                                )
                            )
                            addDialogSuccessCatch(pokemonName = state.pokemonName, object : DialogEditNameInterface {
                                override fun onApply(newName: String, pokemonId: Int) {
                                    viewModel.editPokemonName(newName)
                                }
                            }).show()
                        }
                        is PokemonDetailUiState.SuccessRenamePokemon -> {
                            supportActionBar?.title = state.pokemonName
                            Toast.makeText(
                                this@PokemonDetailActivity,
                                "Pokemon renamed",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                        is PokemonDetailUiState.PokemonReleased -> {
                            supportActionBar?.title = binding.tvPokemonName.text
                            binding.ivPokeball.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this@PokemonDetailActivity,
                                    R.drawable.bw_pokeball
                                )
                            )
                        }
                        is PokemonDetailUiState.NoEvolutionChain -> {
                            binding.tvNoEvolution.isVisible = true
                        }
                        is PokemonDetailUiState.Error -> {
                            Toast
                                .makeText(
                                    this@PokemonDetailActivity,
                                    state.message,
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }
                        else -> {

                        }
                    }

                }
        }
    }
}