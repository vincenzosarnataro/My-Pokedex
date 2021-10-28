package it.sarnataro.presentation.ui.pokemondetail

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.chip.Chip
import com.google.android.material.transition.platform.MaterialArcMotion
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import io.uniflow.android.livedata.onStates
import io.uniflow.core.flow.data.UIState
import it.sarnataro.presentation.R
import it.sarnataro.presentation.databinding.ActivityPokemonDetailBinding
import it.sarnataro.presentation.databinding.CustomItemCarouselBinding
import it.sarnataro.presentation.ui.BaseActivity
import it.sarnataro.presentation.ui.homepage.uimodel.UiPokemon
import it.sarnataro.presentation.ui.pokemondetail.adapter.StatAdapter
import it.sarnataro.presentation.ui.pokemondetail.uimodel.UiDetailModel
import org.imaginativeworld.whynotimagecarousel.listener.CarouselListener
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import org.koin.android.ext.android.inject

class PokemonDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityPokemonDetailBinding
    private val viewModel: DetailViewModel by inject()
    override fun onCreate(savedInstanceState: Bundle?) {


        findViewById<View>(android.R.id.content).transitionName = "shared_element_container"

        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())

        window.sharedElementEnterTransition = MaterialContainerTransform().apply {
            addTarget(android.R.id.content)
            pathMotion = MaterialArcMotion()
        }
        window.sharedElementReturnTransition = MaterialContainerTransform().apply {
            addTarget(android.R.id.content)
            duration = 250L
        }
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.carousel.registerLifecycle(lifecycle)

        binding.carousel.carouselListener = object : CarouselListener {
            override fun onCreateViewHolder(
                layoutInflater: LayoutInflater,
                parent: ViewGroup
            ): ViewBinding {
                // Here, our XML layout file name is custom_item_layout.xml. So our view binding generated class name is CustomItemLayoutBinding.
                return CustomItemCarouselBinding.inflate(layoutInflater, parent, false)
            }

            override fun onBindViewHolder(binding: ViewBinding, item: CarouselItem, position: Int) {
                // Cast the binding to the returned view binding class of the onCreateViewHolder() method.
                val currentBinding = binding as CustomItemCarouselBinding

                Glide.with(binding.root.context).asBitmap()
                    .load(item.imageUrl)
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
                            if (position != 0) return false
                            val p: Palette = Palette.from(resource).generate()
                            p.dominantSwatch?.rgb?.let { color ->
                                this@PokemonDetailActivity.binding.header.setBackgroundColor(color)

                            }
                            return false
                        }

                    })
                    .into(currentBinding.pokemonImage)


            }
        }

        onStates(viewModel) { state ->
            when (state) {
                is UiDetailModel -> {
                    binding.apply {
                        val list = mutableListOf<CarouselItem>()
                        list.add(
                            CarouselItem(
                                imageUrl = state.uiPokemon.urlImage
                            )
                        )
                        list.addAll(state.uiPokemon.images.map { CarouselItem(it) })

                        carousel.setData(list)



                        pokemonName.text = state.uiPokemon.name
                        pokemonId.text = state.uiPokemon.getFormatId()
                        setTypes(state.uiPokemon.types)
                        binding.listStats.adapter = StatAdapter(state.uiPokemon.stats)

                    }

                }
                is UIState.Loading -> {
                }
                is UIState.Failed -> {
                }
            }
        }
        setUpAdapter()
        intent.getIntExtra("id", -1).let {
            viewModel.getPokemonInfo(it)
        }

    }

    private fun setUpAdapter() {

        binding.listStats.apply {
            layoutManager = LinearLayoutManager(this@PokemonDetailActivity)
            setHasFixedSize(true)
        }
        setAnimation()
    }

    private fun setAnimation() {
        val animation: LayoutAnimationController =
            AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_fall_down)
        binding.listStats.layoutAnimation = animation
    }

    private fun setTypes(types: List<String>) {
        binding.typeGroup.removeAllViews()
        types.forEach { type ->
            val chip = Chip(this)
            chip.text = type

            chip.setChipBackgroundColorResource(UiPokemon.getTypeColor(type))
            chip.setTextColor(ContextCompat.getColor(this, R.color.white))
            chip.isClickable = false
            binding.typeGroup.addView(chip)

        }
    }
}