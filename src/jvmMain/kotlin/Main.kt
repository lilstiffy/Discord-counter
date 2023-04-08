import androidx.compose.foundation.Image
import androidx.compose.material.MaterialTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.sikuli.script.App
import org.sikuli.script.Key
import org.sikuli.script.Screen
import java.util.concurrent.TimeUnit


@Composable
fun CounterView(viewModel: CounterViewModel) {
    var isError by remember { mutableStateOf(false) }

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
                        isError = false
                    } catch (e: Exception) {
                        isError = true
                    }
                },
                isError = isError,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            if (isError) {
                Text("Please enter only digits")
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

    private val screenObservable = Single.fromCallable { Screen() }.subscribeOn(Schedulers.io())
    private var countingDisposable: Disposable? = null
    private var screen: Screen? = null

    init {
        screenObservable.subscribe { s -> screen = s }
    }

    fun countButtonClicked() {
        val currentlyRunning = running.value
        running.value = !running.value

        if (currentlyRunning) {
            countingDisposable?.dispose()
        } else {
            App.focus("Discord")
            countingDisposable = Observable.interval(1500L, TimeUnit.MILLISECONDS)
                    .observeOn(Schedulers.io())
                    .subscribe { count() }
        }
    }

    private fun count() {
        screen?.paste("${number.value}")
        screen?.type(Key.ENTER)
        number.value += 1
    }
}

fun main() = application {
    Window(
        title = "Counter",
        icon = painterResource("lilstiffy.png"),
        onCloseRequest = ::exitApplication,
        state = WindowState(
            width = 250.dp,
            height = 300.dp,
            position = WindowPosition(Alignment.Center)
        ),
        resizable = false
    ) {
        CounterView(CounterViewModel())
    }
}
