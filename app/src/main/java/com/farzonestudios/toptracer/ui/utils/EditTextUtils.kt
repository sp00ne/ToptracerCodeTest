package com.farzonestudios.toptracer.ui.utils

import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun EditText.listenToTextChange(): Flow<String> = callbackFlow {
    send(text.toString())
    val textWatcher = doAfterTextChanged {
        trySend(it?.toString().orEmpty())
    }

    awaitClose { removeTextChangedListener(textWatcher) }
}