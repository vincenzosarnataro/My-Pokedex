package it.sarnataro.presentation.ui.homepage.adapter

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import it.sarnataro.presentation.R
import it.sarnataro.presentation.databinding.PokemonItemLayoutBinding
import it.sarnataro.presentation.ui.homepage.uimodel.UiPokemon
import it.sarnataro.presentation.ui.pokemondetail.PokemonDetailActivity


class PokemonAdapter(val onClick: (Int) -> Unit) :
    PagingDataAdapter<UiPokemon, PokemonViewHolder>(
        PokemonDiffCallback()
    ) {
    companion object {
        const val POKEMON_ITEM = 1
        const val FOOTER_ITEM = 2
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val data = getItem(position)

        holder.bind(data,onClick)

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

    fun bind(pokemon: UiPokemon?,onClick: (Int) -> Unit) {

        binding.apply {
            pokemonCard.setOnClickListener {
                val intent = Intent(root.context, PokemonDetailActivity::class.java)
                intent.putExtra("id", pokemon?.id)
                val options = ActivityOptions.makeSceneTransitionAnimation(
                    root.context as Activity,
                    pokemonCard,
                    "shared_element_container" // The transition name to be matched in Activity B.
                )
                root.context.startActivity(intent, options.toBundle())
            }
            pokemonCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    root.context,
                    R.color.background800
                )
            )

            Glide.with(binding.root.context).asBitmap()
                .load(pokemon?.urlImage)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(object : RequestListener<Bitmap> {
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
                        resource ?: return false
                        val p: Palette = Palette.from(resource).generate()
                        p.dominantSwatch?.rgb?.let { color ->
                            pokemonCard.setCardBackgroundColor(color)

                        }
                        return false
                    }

                })
                .into(pokemonImage)

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