package com.example.vinyls_equipo_16.ui.adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.vinyls_equipo_16.R
import com.example.vinyls_equipo_16.databinding.TrackItemBinding
import com.example.vinyls_equipo_16.models.Track

class TracksAdapter : RecyclerView.Adapter<TracksAdapter.TracksViewHolder>(){

    var tracks :List<Track> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val withDataBinding: TrackItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            TracksViewHolder.LAYOUT,
            parent,
            false)
        return TracksViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.track = tracks[position]
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }


    class TracksViewHolder(val viewDataBinding: TrackItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.track_item
        }
    }


}