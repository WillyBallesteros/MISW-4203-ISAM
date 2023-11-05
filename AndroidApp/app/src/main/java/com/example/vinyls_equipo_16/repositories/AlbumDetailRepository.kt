package com.example.vinyls_equipo_16.repositories

import android.app.Application
import com.example.vinyls_equipo_16.models.AlbumDetail
import com.example.vinyls_equipo_16.network.NetworkServiceAdapter

class AlbumDetailRepository (val application: Application){
    suspend fun refreshData(albumId: Int ): AlbumDetail {
        return NetworkServiceAdapter.getInstance(application).getAlbum(albumId)

    }
}