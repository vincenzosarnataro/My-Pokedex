package it.sarnataro.presentation.ui.pokemondetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
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
import it.sarnataro.presentation.ui.util.setImageWithColorBackground
import it.sarnataro.presentation.ui.util.setUrlImage
import org.imaginativeworld.whynotimagecarousel.listener.CarouselListener
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import org.koin.android.ext.android.inject

class PokemonDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityPokemonDetailBinding
    private val viewModel: DetailViewModel by inject()
    override fun onCreate(savedInstanceState: Bundle?) {


        setUpAnimationTransition()
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpErrorView()
        setUpCarousel()

        observeStates()
        setUpAdapter()
        getPokemonDetail()

    }

    private fun observeStates() {
        onStates(viewModel) { state ->
            when (state) {
                is UiDetailModel -> {
                    showDetail()
                    binding.setUpPokemonDetail(state)
                }
                is UIState.Loading -> showLoading()
                is UIState.Failed -> showError()
            }
        }
    }

    private fun ActivityPokemonDetailBinding.setUpPokemonDetail(
        state: UiDetailModel
    ) {
        val list = mutableListOf<CarouselItem>()
        setPokemonImages(list, state)

        carousel.setData(list)



        pokemonName.text = state.uiPokemon.name
        pokemonId.text = state.uiPokemon.getFormatId()
        setTypes(state.uiPokemon.types)
        listStats.adapter = StatAdapter(state.uiPokemon.stats)
    }

    private fun setPokemonImages(
        list: MutableList<CarouselItem>,
        state: UiDetailModel
    ) {
        list.add(
            CarouselItem(
                imageUrl = state.uiPokemon.urlImage
            )
        )
        list.addAll(state.uiPokemon.images.map { CarouselItem(it) })
    }

    private fun setUpCarousel() {
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

                if (position == 0)
                    currentBinding.pokemonImage.setImageWithColorBackground(
                        item.imageUrl,
                        this@PokemonDetailActivity.binding.header
                    )
                else
                    currentBinding.pokemonImage.setUrlImage(item.imageUrl)


            }
        }
    }

    private fun setUpErrorView() {
        binding.error.tryButton.setOnClickListener { getPokemonDetail() }
    }

    private fun setUpAnimationTransition() {
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
    }

    private fun getPokemonDetail() {
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


    private fun showError() {
        binding.loading.isVisible = false
        binding.error.root.isVisible = true
    }

    private fun showLoading() {
        binding.loading.isVisible = true
        binding.error.root.isVisible = false
    }

    private fun showDetail() {
        binding.loading.isVisible = false
        binding.error.root.isVisible = false
    }
}