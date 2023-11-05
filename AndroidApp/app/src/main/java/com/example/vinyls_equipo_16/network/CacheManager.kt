package com.example.vinyls_equipo_16.network

import android.content.Context
import com.example.vinyls_equipo_16.models.Comment

class CacheManager(context: Context) {
    companion object{
        var instance: CacheManager? = null
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: CacheManager(context).also {
                    instance = it
                }
            }
    }
    private var comments: HashMap<Int, List<Comment>> = hashMapOf<Int, List<Comment>>()
    //private var comments: ArrayMap<Int, List<Comment>> = arrayMapOf<Int, List<Comment>>()
    fun addComments(albumId: Int, comment: List<Comment>){
        if (comments.containsKey(albumId)){
            comments[albumId] = comment
        }
    }
    fun getComments(albumId: Int) : List<Comment>{
        return if (comments.containsKey(albumId)) comments[albumId]!! else listOf<Comment>()
    }
}