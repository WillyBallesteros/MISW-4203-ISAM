package com.example.vinyls_equipo_16.ui.adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil.inflate
import androidx.recyclerview.widget.RecyclerView
import com.example.vinyls_equipo_16.R
import com.example.vinyls_equipo_16.databinding.FavoritePerformerItemBinding
import com.example.vinyls_equipo_16.models.FavoritePerformer

class FavoritePerformersAdapter : RecyclerView.Adapter<FavoritePerformersAdapter.FavoritePerformersViewHolder>(){

    var favoritePerformers :List<FavoritePerformer> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritePerformersViewHolder {
        val withDataBinding: FavoritePerformerItemBinding = inflate(
            LayoutInflater.from(parent.context),
            FavoritePerformersViewHolder.LAYOUT,
            parent,
            false)
        return FavoritePerformersViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: FavoritePerformersViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.favoritePerformer = favoritePerformers[position]
        }
    }

    override fun getItemCount(): Int {
        return favoritePerformers.size
    }

    class FavoritePerformersViewHolder(val viewDataBinding: FavoritePerformerItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.favorite_performer_item
        }
    }
}