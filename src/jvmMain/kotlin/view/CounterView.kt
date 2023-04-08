package view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import custom.ErrorDialog
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.sikuli.script.App
import org.sikuli.script.Key
import java.lang.NullPointerException
import java.util.concurrent.TimeUnit

@Composable
fun CounterView(viewModel: CounterViewModel) {
    var inputError by remember { mutableStateOf(false) }

    MaterialTheme {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource("lilstiffy.png"),
                contentDescription = "stiffy",
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = viewModel.number.value.toString(),
                label = { Text("Start from") },
                enabled = !viewModel.running.value,
                onValueChange = {
                    try {
                        if (it.isBlank()) viewModel.number.value = 0 else viewModel.number.value = it.toInt()
                        inputError = false
                    } catch (e: Exception) {
                        inputError = true
                    }
                },
                isError = inputError,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            if (inputError) {
                Text("Please enter only digits")
            }

            if (viewModel.showError.value != null) {
                ErrorDialog(viewModel.showError.value!!) {
                    viewModel.showError.value = null
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.countButtonClicked()
                }
            ) {
                Text(if (!viewModel.running.value) "Start counting" else "Stop counting")
            }
        }
    }
}

class CounterViewModel {
    val running = mutableStateOf(false)
    val number = mutableStateOf(0)
    val showError = mutableStateOf<String?>(null)

    //Discord
    private val discord = App("Discord")
    private var countingDisposable: Disposable? = null

    fun countButtonClicked() {
        val currentlyRunning = running.value
        running.value = !running.value

        if (currentlyRunning) {
            countingDisposable?.dispose()
        } else {
            countingDisposable = Observable.interval(1500L, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .doOnSubscribe { discord.focus() }
                .doOnDispose { running.value = false }
                .subscribe { count() }
        }
    }

    private fun count() {
        try {
            discord.window().paste("${number.value}")
            discord.window().type(Key.ENTER)
            number.value += 1
        } catch (e: Exception) {
            when (e) {
                is NullPointerException -> showError.value = "Discord not open"
                else -> showError.value = "Unknown error you're on your own dude"
            }
            countingDisposable?.dispose()
        }
    }
}