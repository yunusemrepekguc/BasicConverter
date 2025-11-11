package com.yempe.financeapps.core.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

open class BaseViewModel<State, NavigationEvent, UIEvent>(val initialState: State) : ViewModel() {

    private val _state = MutableStateFlow(value = initialState)
    protected val state: StateFlow<State> = _state.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>(replay = 1)
    val navigationEvent: SharedFlow<NavigationEvent> = _navigationEvent.asSharedFlow()

    private val _uiEvent = MutableSharedFlow<UIEvent>()
    val uiEvent: SharedFlow<UIEvent> = _uiEvent.asSharedFlow()


    protected fun updateState(reducer: (prevState: State) -> State) {
        _state.update(reducer)
    }

    protected fun updateAndGetState(mutator: (prevState: State) -> State): State {
        return _state.updateAndGet { prevState ->
            mutator(prevState)
        }
    }

    protected fun navigate(command: NavigationEvent) {
        viewModelScope.launch {
            _navigationEvent.emit(value = command)
        }
    }

    protected fun postEvent(event: UIEvent) {
        viewModelScope.launch {
            _uiEvent.emit(value = event)
        }
    }

    protected fun launchAll(
        context: CoroutineContext = Dispatchers.IO,
        suspendableFunction: suspend () -> Unit
    ) {
        viewModelScope.launch(
            context = context
        ) {
            try {
                suspendableFunction()
            } catch (t: Exception) {
                Timber.Forest.e(t)
            }
        }
    }
}