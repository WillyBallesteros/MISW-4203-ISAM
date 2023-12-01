package com.example.vinyls_equipo_16.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.vinyls_equipo_16.R
import com.example.vinyls_equipo_16.databinding.AlbumItemBinding
import com.example.vinyls_equipo_16.databinding.MusicianAlbumItemBinding
import com.example.vinyls_equipo_16.models.Album
import com.example.vinyls_equipo_16.ui.AlbumFragmentDirections

class MusicianAlbumAdapter : RecyclerView.Adapter<MusicianAlbumAdapter.MusicianAlbumViewHolder>(){

    var albums :List<Album> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicianAlbumViewHolder {
        val withDataBinding: MusicianAlbumItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            MusicianAlbumViewHolder.LAYOUT,
            parent,
            false)
        return MusicianAlbumViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: MusicianAlbumViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.album = albums[position]
        }
    }

    override fun getItemCount(): Int {
        return albums.size
    }

    class MusicianAlbumViewHolder(val viewDataBinding: MusicianAlbumItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.musician_album_item
        }
    }
}