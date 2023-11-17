package com.example.vinyls_equipo_16.ui.adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil.inflate
import androidx.recyclerview.widget.RecyclerView
import com.example.vinyls_equipo_16.R
import com.example.vinyls_equipo_16.databinding.CollectorAlbumItemBinding
import com.example.vinyls_equipo_16.models.CollectorAlbum

class CollectorAlbumsAdapter : RecyclerView.Adapter<CollectorAlbumsAdapter.CollectorAlbumsViewHolder>(){

    var collectorAlbums :List<CollectorAlbum> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectorAlbumsViewHolder {
        val withDataBinding: CollectorAlbumItemBinding = inflate(
            LayoutInflater.from(parent.context),
            CollectorAlbumsViewHolder.LAYOUT,
            parent,
            false)
        return CollectorAlbumsViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: CollectorAlbumsViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.collectorAlbum = collectorAlbums[position]
        }
    }

    override fun getItemCount(): Int {
        return collectorAlbums.size
    }

    class CollectorAlbumsViewHolder(val viewDataBinding: CollectorAlbumItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.collector_album_item
        }
    }
}