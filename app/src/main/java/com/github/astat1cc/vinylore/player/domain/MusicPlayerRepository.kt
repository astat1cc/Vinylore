package com.github.astat1cc.vinylore.player.domain

interface MusicPlayerRepository {

    suspend fun getLastPlayingAlbumPath(): String
}