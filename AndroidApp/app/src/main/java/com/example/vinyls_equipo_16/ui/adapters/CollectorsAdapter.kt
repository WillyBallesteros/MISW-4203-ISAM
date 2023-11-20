package com.example.vinyls_equipo_16.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.vinyls_equipo_16.R
import com.example.vinyls_equipo_16.databinding.CollectorItemBinding
import com.example.vinyls_equipo_16.models.Collector
import com.example.vinyls_equipo_16.ui.CollectorFragmentDirections

class CollectorsAdapter : RecyclerView.Adapter<CollectorsAdapter.CollectorViewHolder>(){

    @SuppressLint("NotifyDataSetChanged")
    var collectors :List<Collector> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectorViewHolder {
        val withDataBinding: CollectorItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            CollectorViewHolder.LAYOUT,
            parent,
            false)
        return CollectorViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: CollectorViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.collector = collectors[position]
        }
        holder.viewDataBinding.root.setOnClickListener {
            val action = CollectorFragmentDirections.actionCollectorFragmentToCollectorDetailFragment(collectors[position].collectorId)
            holder.viewDataBinding.root.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return collectors.size
    }


    class CollectorViewHolder(val viewDataBinding: CollectorItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.collector_item
        }

    }


}