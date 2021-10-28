package it.sarnataro.presentation.ui.homepage.uimodel

import androidx.paging.PagingData
import io.uniflow.core.flow.data.UIState

data class UiHomeModel(val uiPokemonList:PagingData<UiPokemon>): UIState()
{
}