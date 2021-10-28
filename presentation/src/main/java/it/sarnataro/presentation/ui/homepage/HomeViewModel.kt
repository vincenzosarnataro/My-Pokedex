package it.sarnataro.presentation.ui.homepage

import androidx.lifecycle.viewModelScope
import androidx.paging.*
import io.uniflow.android.AndroidDataFlow
import io.uniflow.core.coroutines.onFlow
import io.uniflow.core.flow.data.UIState
import it.sarnataro.domain.usecase.GetPokemonList
import it.sarnataro.presentation.ui.homepage.paging.PokemonPagingSource
import it.sarnataro.presentation.ui.homepage.uimodel.UiHomeModel
import kotlinx.coroutines.launch

class HomeViewModel(private val getPokemonList: GetPokemonList) : AndroidDataFlow() {


    init {
        viewModelScope.launch {
            setState(UIState.Loading)

        }

    }

    fun checkState(state: LoadState) {
        viewModelScope.launch {
            when (state) {
                is LoadState.Loading -> setState(UIState.Loading)
                is LoadState.Error -> setState(UIState.Failed())


                else -> {
                }
            }
        }
    }

    @ExperimentalPagingApi
    fun loadPokemonList() {
        val flow = Pager(
            PagingConfig(pageSize = 5)
        ) {
            PokemonPagingSource(getPokemonList)
        }.flow
            .cachedIn(viewModelScope)
        // Launch a job
        viewModelScope.launch {
            onFlow(
                flow = { flow },
                doAction = { value ->

                    setState(UiHomeModel(uiPokemonList = value))

                }
            )
        }
    }
}