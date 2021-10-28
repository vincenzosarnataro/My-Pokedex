package it.sarnataro.presentation.ui.pokemondetail.uimodel

import androidx.paging.PagingData
import io.uniflow.core.flow.data.UIState
import it.sarnataro.presentation.ui.homepage.uimodel.UiPokemon

data class UiDetailModel(val uiPokemon: UiPokemon): UIState()
{
}