package com.farzonestudios.toptracer.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farzonestudios.toptracer.data.User
import com.farzonestudios.toptracer.ui.details.data.Gif
import com.farzonestudios.toptracer.ui.details.domain.ClearUserUseCase
import com.farzonestudios.toptracer.ui.details.domain.GetGifUseCase
import com.farzonestudios.toptracer.ui.details.domain.ListenToUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val clearUserUseCase: ClearUserUseCase,
    private val getGifUseCase: GetGifUseCase,
    listenToUserUseCase: ListenToUserUseCase
) : ViewModel() {
    sealed class Event {
        data class SetState(
            val userName: String,
            val gifState: GifState
        ) : Event()

        object NavigateToMain : Event()
    }

    sealed class GifState {
        object Loading : GifState()
        data class Complete(val gif: Gif) : GifState()
        data class Error(val throwable: Throwable) : GifState()
    }

    private val _state = MutableSharedFlow<Event>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val state: SharedFlow<Event> = _state

    init {
        listenToUserUseCase()
            .onEach { user ->
                if (user == null) {
                    _state.emit(Event.NavigateToMain)
                } else {
                    setUsernameAndGif(user)
                }
            }
            .launchIn(viewModelScope)
    }

    private suspend fun setUsernameAndGif(user: User) {
        // Start loading
        _state.emit(
            Event.SetState(
                user.username,
                GifState.Loading
            )
        )

        getGifUseCase().fold(
            onSuccess = {
                _state.emit(
                    Event.SetState(
                        user.username,
                        GifState.Complete(it)
                    )
                )
            }, onFailure = {
                _state.emit(
                    Event.SetState(
                        user.username,
                        GifState.Error(it)
                    )
                )
            })
    }

    fun logout() {
        viewModelScope.launch {
            clearUserUseCase()
        }
    }
}