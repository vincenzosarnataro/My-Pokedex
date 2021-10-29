package it.sarnataro.presentation.ui.homepage.adapter

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import it.sarnataro.presentation.R
import it.sarnataro.presentation.databinding.PokemonItemLayoutBinding
import it.sarnataro.presentation.ui.homepage.uimodel.UiPokemon
import it.sarnataro.presentation.ui.pokemondetail.PokemonDetailActivity
import it.sarnataro.presentation.ui.util.setImageWithColorBackground


class PokemonAdapter(private val onClick: (Int?, View) -> Unit) :
    PagingDataAdapter<UiPokemon, PokemonViewHolder>(
        PokemonDiffCallback()
    ) {
    companion object {
        const val POKEMON_ITEM = 1
        const val FOOTER_ITEM = 2
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val data = getItem(position)

        holder.bind(data, onClick)

    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount) {
            FOOTER_ITEM
        } else {
            POKEMON_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        return PokemonViewHolder(
            PokemonItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }
}

class PokemonViewHolder(private val binding: PokemonItemLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(pokemon: UiPokemon?, onClick: (Int?,View) -> Unit) {

        binding.apply {
            pokemonCard.setOnClickListener { onClick.invoke(pokemon?.id,pokemonCard)}

            pokemonCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    root.context,
                    R.color.background800
                )
            )

            pokemonImage.setImageWithColorBackground(pokemon?.urlImage, pokemonCard)


            pokemonName.text = pokemon?.name.orEmpty()

        }
    }
}

private class PokemonDiffCallback : DiffUtil.ItemCallback<UiPokemon>() {
    override fun areItemsTheSame(oldItem: UiPokemon, newItem: UiPokemon): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UiPokemon, newItem: UiPokemon): Boolean {
        return oldItem == newItem
    }
}