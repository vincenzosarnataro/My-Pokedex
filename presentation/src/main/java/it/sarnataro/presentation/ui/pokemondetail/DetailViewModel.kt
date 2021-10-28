package it.sarnataro.presentation.ui.pokemondetail

import androidx.lifecycle.viewModelScope
import io.uniflow.android.AndroidDataFlow
import io.uniflow.core.coroutines.onFlow
import it.sarnataro.domain.usecase.GetPokemonDetail
import it.sarnataro.domain.usecase.GetPokemonList
import it.sarnataro.presentation.ui.homepage.mapping.toUiPokemon
import it.sarnataro.presentation.ui.homepage.uimodel.UiHomeModel
import it.sarnataro.presentation.ui.pokemondetail.uimodel.UiDetailModel
import kotlinx.coroutines.launch

class DetailViewModel (private val getPokemonDetail: GetPokemonDetail): AndroidDataFlow() {


    fun getPokemonInfo(id:Int){
        viewModelScope.launch {
            val flow = getPokemonDetail(id)

            onFlow(
                flow = { flow },
                doAction = { value ->

                    if (value != null)
                    setState (UiDetailModel(uiPokemon = value.toUiPokemon()))

                }
            )
        }
    }
}