package custom

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogState

@OptIn(ExperimentalUnitApi::class)
@Composable
fun ErrorDialog(errorMessage: String, onClose: () -> Unit) {
    Dialog(
        onCloseRequest = onClose,
        state = DialogState(width = 200.dp, height = 200.dp),
        resizable = false,
        title = "Error"
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.Center) {
                Text(
                    text = "Error",
                    fontSize = TextUnit(22f, TextUnitType.Sp),
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = errorMessage,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = onClose,
                    modifier = Modifier.align(Alignment.CenterHorizontally))
                {
                    Text(text = "OK")
                }
            }
        }
    }
}