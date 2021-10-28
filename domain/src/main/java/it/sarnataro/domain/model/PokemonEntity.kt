package it.sarnataro.domain.model

data class PokemonEntity(val id:Int, val name:String,val stats:List<Stat>,val images: List<String>,val types:List<String>)


data class Stat(val name:String,val value:Int)