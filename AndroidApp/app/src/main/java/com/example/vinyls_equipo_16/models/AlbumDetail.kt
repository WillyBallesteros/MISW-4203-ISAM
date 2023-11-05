package com.example.vinyls_equipo_16.models

data class AlbumDetail (
    val albumId:Int,
    val name:String,
    val cover:String,
    val releaseDate:String,
    val description:String,
    val genre:String,
    val recordLabel:String,
    val tracks:List<Track>
)