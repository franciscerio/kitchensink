package com.fcerio.kitchensink.features.effect

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch

class EffectActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val snackBarHostState = remember {
                SnackbarHostState()
            }
            val scope = rememberCoroutineScope()
            val counter = produceState(initialValue = 0) {
                delay(3000L)
                value = 4
            }

            if (counter.value % 5 == 0 && counter.value > 0) {
                LaunchedEffect(key1 = snackBarHostState) {
                    snackBarHostState.showSnackbar("Hello")
                }
            }
            Scaffold(snackbarHost = { SnackbarHost(hostState = snackBarHostState) }) {
                Button(modifier = Modifier.padding(it), onClick = { }) {
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

@Composable
fun RememberCoroutineScopeDemo() {
    val scope = rememberCoroutineScope()
    Button(onClick = {
        scope.launch {
            delay(1000L)
            Log.d("Test", "Delayed")
        }
    }) {
        Text(text = "Click Me!")
    }
}

@Composable
fun RememberUpdatedStateDemo(
    onTimeOut: () -> Unit
) {
    val updatedOnTimeout by rememberUpdatedState(newValue = onTimeOut)

    LaunchedEffect(key1 = true) {
        delay(3000L)
        updatedOnTimeout()
    }
}

@Composable
fun DisposableEffectDemo() {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                println("On Pause Called!")
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@Composable
fun SideEffectDemo(nonComposeCounter: Int) {
    SideEffect {
        println("Called after every successful recomposition")
    }
}

@Composable
fun produceStateDemo(countUpTo: Int): State<Int> {
    return produceState(initialValue = 0) {
        while (value < countUpTo) {
            delay(1000L)
            value++
        }
    }
}

@Composable
fun coroutineFlowDemo(countUpTo: Int): State<Int> {
    return flow<Int> {
        var value = 0
        while (value < countUpTo) {
            delay(1000L)
            value++
            emit(value)
        }
    }.collectAsState(initial = 0)
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun DerivedStateDemo() {
    var counter by remember {
        mutableStateOf(0)
    }
    val counterText by derivedStateOf {
        "The counter is $counter"
    }
    Button(onClick = {
        counter++
    }) {
        Text(text = counterText)
    }
}

@Composable
fun SnapshotFlowDemo() {
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    LaunchedEffect(key1 = snackBarHostState) {
        snapshotFlow { snackBarHostState }
            .mapNotNull { it.currentSnackbarData?.visuals?.message }
            .distinctUntilChanged()
            .collect { message ->
                println("A snacbar with message $message was shown!")
            }
    }
}