package com.example.vinyls_equipo_16.ui.adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil.inflate
import androidx.recyclerview.widget.RecyclerView
import com.example.vinyls_equipo_16.R
import com.example.vinyls_equipo_16.databinding.CommentItemBinding
import com.example.vinyls_equipo_16.models.Comment

class CommentsAdapter : RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>(){

    var comments :List<Comment> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val withDataBinding: CommentItemBinding = inflate(
            LayoutInflater.from(parent.context),
            CommentsViewHolder.LAYOUT,
            parent,
            false)
        return CommentsViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.comment = comments[position]
        }
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    class CommentsViewHolder(val viewDataBinding: CommentItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.comment_item
        }
    }
}