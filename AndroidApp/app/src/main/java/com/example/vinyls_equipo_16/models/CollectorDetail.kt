package com.example.vinyls_equipo_16.models

data class CollectorDetail(
    val collectorId:Int,
    val name:String,
    val telephone:String,
    val email:String,
    val comments:List<Comment>,
    val favoritePerformers:List<FavoritePerformer>,
    val collectorAlbums:List<CollectorAlbum>
)
