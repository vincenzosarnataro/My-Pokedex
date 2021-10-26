package it.sarnataro.dbdatasource.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DbPokemon (@PrimaryKey val id:Int, val name:String)