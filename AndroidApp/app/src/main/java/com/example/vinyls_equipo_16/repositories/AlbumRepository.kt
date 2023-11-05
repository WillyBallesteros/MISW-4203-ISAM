package com.example.vinyls_equipo_16.repositories

import android.app.Application
import com.example.vinyls_equipo_16.models.Album
import com.example.vinyls_equipo_16.network.NetworkServiceAdapter

class AlbumRepository (val application: Application){
    suspend fun refreshData(): List<Album> {
        return NetworkServiceAdapter.getInstance(application).getAlbums()
    }
}