package com.fcerio.kitchensink.features.effect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EffectActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val snackBarHostState = remember {
                SnackbarHostState()
            }
            val scope = rememberCoroutineScope()
            var counter = produceState(initialValue = 0) {
                delay(3000L)
                value = 4
            }

            if (counter.value % 5 == 0 && counter.value > 0) {
                LaunchedEffect(key1 = snackBarHostState) {
                    snackBarHostState.showSnackbar("Hello")
                }
            }
            Scaffold(snackbarHost = { SnackbarHost(hostState = snackBarHostState) }) {
                Button(modifier = Modifier.padding(it), onClick = {   }) {
                    Text(text = "Click me: ${counter.value}")
                }
            }
        }
    }
}

@Composable
fun MyComposable(backPressedDispatcher: OnBackPressedDispatcher) {
    val callback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

            }

        }
    }

    DisposableEffect(key1 = backPressedDispatcher) {
        backPressedDispatcher.addCallback(callback)

        onDispose {
            callback.remove()
        }
    }

    Button(onClick = { }) {
        Text(text = "Click me")
    }
}