package com.farzonestudios.toptracer.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farzonestudios.toptracer.ui.main.domain.LoginUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase,
) : ViewModel() {
    sealed class Event {
        data class SetLoginEnabled(val isEnabled: Boolean) : Event()
        object NavigateToDetails : Event()
        data class ShowSnackbar(val message: String) : Event()
    }

    private val _state = MutableSharedFlow<Event>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val state: SharedFlow<Event> = _state

    fun onInputFieldsChanged(username: String?, password: String?) {
        val buttonEnabled = !(username.isNullOrBlank() || password.isNullOrBlank())
        _state.tryEmit(Event.SetLoginEnabled(buttonEnabled))
    }

    fun doLogin(username: String, password: String) {
        viewModelScope.launch {
            // Thread safe, since repository handles thread switching (Coroutine best practices)
            val result = loginUserUseCase.loginUser(username, password)
            result.fold({
                _state.emit(Event.NavigateToDetails)
            }, {
                _state.emit(Event.ShowSnackbar(it.message ?: "Something went wrong!"))
            })
        }
    }

    fun onForgotPasswordClick() {
        viewModelScope.launch {
            _state.emit(
                Event.ShowSnackbar(
                    "Too bad! Might try the world\'s most common password ;)"
                )
            )
        }
    }
}