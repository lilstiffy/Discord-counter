import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import view.CounterView
import view.CounterViewModel

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