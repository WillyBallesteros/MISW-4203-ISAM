package com.example.vinyls_equipo_16.models


data class Track (
    val id:Int,
    val name: String,
    val duration:String
)

data class Album (
    val albumId:Int,
    val name:String,
    val cover:String,
    val releaseDate:String,
    val description:String,
    val genre:String,
    val recordLabel:String
)

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
