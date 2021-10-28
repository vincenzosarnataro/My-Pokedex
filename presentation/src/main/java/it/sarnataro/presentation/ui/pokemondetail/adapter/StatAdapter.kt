package it.sarnataro.presentation.ui.pokemondetail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.sarnataro.presentation.R
import it.sarnataro.presentation.databinding.StatsItemLayoutBinding
import it.sarnataro.presentation.ui.homepage.uimodel.UiPokemon

class StatAdapter(var list: List<UiPokemon.UiStat>) : RecyclerView.Adapter<StatHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatHolder {
        return StatHolder(
            StatsItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: StatHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class StatHolder(private val binding: StatsItemLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UiPokemon.UiStat) {
        binding.pokemonStats.text =
            binding.root.resources.getString(R.string.stat_string, item.name, item.value)
    }

}