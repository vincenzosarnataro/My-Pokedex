package it.sarnataro.presentation.ui.pokemondetail

import androidx.lifecycle.viewModelScope
import io.uniflow.android.AndroidDataFlow
import io.uniflow.core.coroutines.onFlow
import io.uniflow.core.flow.data.UIState
import it.sarnataro.domain.usecase.GetPokemonDetail
import it.sarnataro.presentation.ui.homepage.mapping.toUiPokemon
import it.sarnataro.presentation.ui.pokemondetail.uimodel.UiDetailModel
import kotlinx.coroutines.launch

class DetailViewModel(private val getPokemonDetail: GetPokemonDetail) : AndroidDataFlow() {


    init {
        viewModelScope.launch {
            setState(UIState.Loading)
        }
    }

    fun getPokemonInfo(id: Int) {
        viewModelScope.launch {
            setState(UIState.Loading)

            val flow = getPokemonDetail(id)

            onFlow(
                flow = { flow },
                doAction = { value ->

                    if (value != null)
                        setState(UiDetailModel(uiPokemon = value.toUiPokemon()))
                    else
                        setState(UIState.Failed())

                },
                onError = { _, _ -> setState(UIState.Failed()) }
            )
        }
    }
}