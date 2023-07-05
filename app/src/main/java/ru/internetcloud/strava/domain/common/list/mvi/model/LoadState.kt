package ru.internetcloud.strava.domain.common.list.mvi.model

sealed class LoadState {
    object RefreshLoading : LoadState()
    object RefreshSuccess : LoadState()
    data class RefreshError(val throwable: Throwable) : LoadState()

    object ListLoading : LoadState()
    object ListSuccess : LoadState()
    data class ListError(val throwable: Throwable) : LoadState()

    data class PageLoading(val loadType: LoadType) : LoadState()
    data class PageError(val throwable: Throwable, val loadType: LoadType) : LoadState()
    data class PageSuccess(val loadType: LoadType) : LoadState()
}
