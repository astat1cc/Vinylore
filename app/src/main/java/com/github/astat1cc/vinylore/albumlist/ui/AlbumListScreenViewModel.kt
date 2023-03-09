package com.github.astat1cc.vinylore.albumlist.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.astat1cc.vinylore.SLIDE_IN_DURATION
import com.github.astat1cc.vinylore.albumlist.domain.AlbumListScreenInteractor
import com.github.astat1cc.vinylore.core.AppErrorHandler
import com.github.astat1cc.vinylore.core.DispatchersProvider
import com.github.astat1cc.vinylore.core.models.domain.AppListingAlbum
import com.github.astat1cc.vinylore.core.models.domain.FetchResult
import com.github.astat1cc.vinylore.core.models.ui.ListingAlbumUi
import com.github.astat1cc.vinylore.core.models.ui.UiState
import com.github.astat1cc.vinylore.player.ui.service.MediaPlayerServiceConnection
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AlbumListScreenViewModel(
    private val interactor: AlbumListScreenInteractor,
    private val dispatchers: DispatchersProvider,
    private val errorHandler: AppErrorHandler,
    private val serviceConnection: MediaPlayerServiceConnection
) : ViewModel() {

//    val uiState: StateFlow<UiState<List<AlbumUi>?>> = interactor.fetchAlbums()
//        .map { fetchResult -> fetchResult.toUiState() }
//        .stateIn(viewModelScope, SharingStarted.Lazily, UiState.Loading())

    private val _uiState = MutableStateFlow<UiState<List<ListingAlbumUi>?>>(UiState.Loading())
    val uiState: StateFlow<UiState<List<ListingAlbumUi>?>> = _uiState.asStateFlow()

    private val _clickedAlbumUri = MutableStateFlow<Uri?>(null)
    val clickedAlbumUri: StateFlow<Uri?> = _clickedAlbumUri.asStateFlow()

    init {
        enableAlbumsScan()
        viewModelScope.launch {
            // default delay makes transition animation smoother, because UiState.Loading wouldn't
            // be changed to UiState.Success with probably huge list of items
            val defaultDelay = launch { delay(SLIDE_IN_DURATION.toLong() + 20L) }
            interactor.fetchAlbums().collect { fetchResult ->
                defaultDelay.join()
                _uiState.value = fetchResult.toUiState()
            }
        }
    }

//    init {
//        fetchAlbums()
//    }

//    fun fetchAlbums() {
//        viewModelScope.launch(dispatchers.io()) {
//            _uiState.value = interactor.fetchAlbums().toUiState()
//        }
//    }

    fun handleChosenDirUri(uri: Uri) = viewModelScope.launch(dispatchers.io()) {
        interactor.saveChosenDirectoryPath(uri.toString())
    }

    fun onAlbumClick(albumUri: Uri) = viewModelScope.launch(dispatchers.io()) {
        interactor.saveChosenPlayingAlbum(albumUri)
        _clickedAlbumUri.value = albumUri
        delay(600L) // delay of album slideOut animation
    }

    fun disableAlbumsScan() {
        interactor.disableAlbumsScan()
    }

    fun enableAlbumsScan() {
        _uiState.value = UiState.Loading()
        interactor.enableAlbumsScan()
    }

    private fun FetchResult<List<AppListingAlbum>?>.toUiState(): UiState<List<ListingAlbumUi>?> =
        when (this) {
            is FetchResult.Success -> {
                UiState.Success(
                    data?.map { albumDomain ->
                        ListingAlbumUi.fromDomain(albumDomain)
                    }
                )
            }
            is FetchResult.Fail -> {
                UiState.Fail(
                    message = errorHandler.getErrorMessage(error)
                )
            }
        }
}