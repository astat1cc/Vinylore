package com.github.astat1cc.vinylore.player.ui.service

import android.content.ComponentName
import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.github.astat1cc.vinylore.Consts
import com.github.astat1cc.vinylore.core.models.ui.AudioTrackUi
import kotlinx.coroutines.flow.*

class MediaPlayerServiceConnection(
    context: Context
) {

    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.IDLE)
    val playerState = _playerState.asStateFlow()

    private val _playbackState = MutableStateFlow<PlaybackStateCompat?>(null)
    val playbackState: StateFlow<PlaybackStateCompat?> = _playbackState.asStateFlow()

    private val _isConnected = MutableStateFlow<Boolean>(false)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    private val _currentPlayingAudio = MutableStateFlow<AudioTrackUi?>(null)
    val currentPlayingAudio = _currentPlayingAudio.asStateFlow()

    private lateinit var mediaController: MediaControllerCompat

    private val mediaBrowserConnectionCallback = MediaBrowserConnectionCallback(context)
    private val mediaBrowser = MediaBrowserCompat(
        context,
        ComponentName(context, MusicService::class.java),
        mediaBrowserConnectionCallback,
        null
    ).apply {
        connect()
    }

    private var trackList = listOf<AudioTrackUi>()

    val rootMediaId: String
        get() = mediaBrowser.root

    val transportControl: MediaControllerCompat.TransportControls
        get() = mediaController.transportControls

    fun slowPause() {
        mediaBrowser.sendCustomAction(Consts.PAUSE_MEDIA_PLAY_ACTION, null, null)
        _playerState.value = PlayerState.PAUSED
    }

    fun slowResume() {
        mediaBrowser.sendCustomAction(Consts.RESUME_MEDIA_PLAY_ACTION, null, null)
        _playerState.value = PlayerState.PLAYING
    }

    fun startPlaying(tracks: List<AudioTrackUi>) {
        trackList = tracks
        mediaBrowser.sendCustomAction(
            Consts.START_MEDIA_PLAY_ACTION,
            null,
            null
        ) // todo if i need this?
        val trackToPlay = trackList.first()
        transportControl.playFromMediaId(
            trackToPlay.uri.toString(),
            null
        )
        _playerState.value = PlayerState.LAUNCHING
    }

    fun fastForward(seconds: Int = 10) {
        playbackState.value?.currentPosition?.let {
            transportControl.seekTo(it + seconds * 1000)
        }
    }

    fun rewind(seconds: Int = 10) {
        playbackState.value?.currentPosition?.let {
            transportControl.seekTo(it - seconds * 1000)
        }
    }

    fun skipToNext() {
        transportControl.skipToNext()
    }

    fun subscribe(
        parentId: String,
        callback: MediaBrowserCompat.SubscriptionCallback
    ) {
        mediaBrowser.subscribe(parentId, callback)
    }

    fun unsubscribe(
        parentId: String,
        callback: MediaBrowserCompat.SubscriptionCallback
    ) {
        mediaBrowser.unsubscribe(parentId, callback)
    }

    fun refreshMediaBrowserChildren() {
        mediaBrowser.sendCustomAction(Consts.REFRESH_MEDIA_PLAY_ACTION, null, null)
    }

    private inner class MediaBrowserConnectionCallback(
        private val context: Context
    ) : MediaBrowserCompat.ConnectionCallback() {

        override fun onConnected() {
            _isConnected.value = true
            mediaController = MediaControllerCompat(context, mediaBrowser.sessionToken).apply {
                registerCallback(MediaControllerCallback())
            }
        }

        override fun onConnectionSuspended() {
            _isConnected.value = false
        }

        override fun onConnectionFailed() {
            _isConnected.value = false
        }
    }

    private inner class MediaControllerCallback : MediaControllerCompat.Callback() {

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            super.onPlaybackStateChanged(state)

            _playbackState.value = state
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            super.onMetadataChanged(metadata)

            _currentPlayingAudio.value = metadata?.let {
                trackList.find { track ->
                    track.uri == metadata.description.mediaUri
                }
            }
        }

        override fun onSessionDestroyed() {
            super.onSessionDestroyed()

            mediaBrowserConnectionCallback.onConnectionSuspended()
        }
    }
}