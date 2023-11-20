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
import com.example.vinyls_equipo_16.databinding.MusicianItemBinding
import com.example.vinyls_equipo_16.models.Musician
import com.example.vinyls_equipo_16.ui.MusicianFragmentDirections

class MusiciansAdapter : RecyclerView.Adapter<MusiciansAdapter.MusicianViewHolder>(){

    @SuppressLint("NotifyDataSetChanged")
    var musicians :List<Musician> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicianViewHolder {
        val withDataBinding: MusicianItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            MusicianViewHolder.LAYOUT,
            parent,
            false)
        return MusicianViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: MusicianViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.musician = musicians[position]
        }
        holder.bind(musicians[position])
        holder.viewDataBinding.root.setOnClickListener {
           val action = MusicianFragmentDirections.actionMusicianFragmentToMusicianDetailFragment(musicians[position].musicianId)
           holder.viewDataBinding.root.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return musicians.size
    }


    class MusicianViewHolder(val viewDataBinding: MusicianItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.musician_item
        }
        fun bind(musician: Musician) {
            Glide.with(itemView)
                .load(musician.image.toUri().buildUpon().scheme("https").build())
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.loading_animation)

                        .error(R.drawable.ic_broken_image))
                .into(viewDataBinding.musicianImage)
        }

    }


}