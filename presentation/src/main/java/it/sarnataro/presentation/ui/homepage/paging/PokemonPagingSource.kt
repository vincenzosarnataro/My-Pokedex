package it.sarnataro.presentation.ui.homepage.paging

import android.util.Log
import androidx.paging.LoadState
import androidx.paging.PagingSource
import androidx.paging.PagingState
import it.sarnataro.domain.usecase.GetPokemonList
import it.sarnataro.presentation.ui.homepage.mapping.toUiPokemon
import it.sarnataro.presentation.ui.homepage.uimodel.UiPokemon
import kotlinx.coroutines.flow.first
import java.lang.Exception

class PokemonPagingSource(private val getPokemonList: GetPokemonList) :
    PagingSource<Int, UiPokemon>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UiPokemon> {
        try {
            val currentOffset = params.key ?: 0
            val pokemonList = getPokemonList(offset = currentOffset).first()
            val nextOffset = currentOffset + pokemonList.size
            Log.e("PokemonPagingSource", "Current offset: $currentOffset")


            return LoadResult.Page(
                data = pokemonList.map { it.toUiPokemon() },
                prevKey = null,
                nextKey = nextOffset
            )
        }catch (e:Exception){
            return  LoadResult.Error(
                throwable = e
            )
        }

    }

    override fun getRefreshKey(state: PagingState<Int, UiPokemon>): Int? {
        return null
    }


}