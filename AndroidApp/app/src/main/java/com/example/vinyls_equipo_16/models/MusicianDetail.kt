package com.example.vinyls_equipo_16.models

data class MusicianDetail(
    val musicianId:Int,
    val name:String,
    val image:String,
    val description: String,
    val birthDate:String,
    val performerPrizes:List<Prize>,
    val albums:List<Album>
)
