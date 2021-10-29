package it.sarnataro.presentation.ui.homepage

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import io.uniflow.android.livedata.onStates
import io.uniflow.core.flow.data.UIState
import it.sarnataro.presentation.R
import it.sarnataro.presentation.databinding.ActivityMainBinding
import it.sarnataro.presentation.ui.BaseActivity
import it.sarnataro.presentation.ui.homepage.adapter.PokemonAdapter
import it.sarnataro.presentation.ui.homepage.adapter.PokemonLoadingStateAdapter
import it.sarnataro.presentation.ui.homepage.uimodel.UiHomeModel
import it.sarnataro.presentation.ui.pokemondetail.PokemonDetailActivity
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


@ExperimentalPagingApi
class MainActivity : BaseActivity() {
    private val viewModel: HomeViewModel by inject()
    private val adapterList = PokemonAdapter(::onClickCard)
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        setUpAnimationTransition()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpErrorView()
        observeStates()
        setUpAdapter()
        viewModel.loadPokemonList()


    }

    private fun observeStates() {
        onStates(viewModel) { state ->
            when (state) {
                is UiHomeModel -> {
                    showHome()
                    lifecycleScope.launch {
                        adapterList.submitData(state.uiPokemonList)

                    }
                }
                is UIState.Loading -> showLoading()
                is UIState.Failed -> showError()
            }
        }
    }

    private fun setUpErrorView() {
        binding.error.tryButton.setOnClickListener { retry() }
    }

    private fun setUpAnimationTransition() {
        setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())

        window.sharedElementsUseOverlay = false
    }

    private fun onClickCard(id:Int){
        val intent = Intent(this, PokemonDetailActivity::class.java)
        intent.putExtra("id",id)
        val options = ActivityOptions.makeSceneTransitionAnimation(
            this,
            this.findViewById<ViewGroup>(android.R.id.content),
            "shared_element_container" // The transition name to be matched in Activity B.
        )
        startActivity(intent, options.toBundle())
    }

    private fun showError() {
        binding.loading.isVisible = false
        binding.pokemonListRecycler.isVisible = false
        binding.error.root.isVisible = true
    }

    private fun showLoading() {
        binding.loading.isVisible = true
        binding.pokemonListRecycler.isVisible = false
        binding.error.root.isVisible = false
    }

    private fun showHome() {
        binding.loading.isVisible = false
        binding.pokemonListRecycler.isVisible = true
        binding.error.root.isVisible = false
    }

    private fun getLayoutManager(): RecyclerView.LayoutManager {
        val gridLayoutManager = GridLayoutManager(this, 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = adapterList.getItemViewType(position)
                return if (viewType == PokemonAdapter.POKEMON_ITEM) 1
                else 2
            }
        }
        return gridLayoutManager
    }

    private fun setAnimation() {
        val animation: LayoutAnimationController =
            AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_fall_down)
        binding.pokemonListRecycler.layoutAnimation = animation
    }

    private fun setLoadStateListener() {
        adapterList.addLoadStateListener { state ->
            if (adapterList.snapshot().isEmpty())
                viewModel.checkState(state = state.source.refresh)
            else
                showHome()

        }
    }

    private fun setUpAdapter() {

        binding.pokemonListRecycler.apply {
            layoutManager = this@MainActivity.getLayoutManager()
            setHasFixedSize(true)
            adapter = adapterList.withLoadStateFooter(PokemonLoadingStateAdapter(::retry))
        }
        setAnimation()
        setLoadStateListener()
    }

    private fun retry() {
        adapterList.retry()
    }
}