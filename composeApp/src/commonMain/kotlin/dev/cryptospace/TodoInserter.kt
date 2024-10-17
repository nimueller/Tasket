package dev.cryptospace

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent

@Composable
fun TodoInserter(
    onItemAdded: (String) -> Unit = {},
) {
    Row {
        var text by remember { mutableStateOf("") }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth() then Modifier.onKeyEvent { event ->
                return@onKeyEvent if (event.key == Key.Enter) {
                    onItemAdded(text)
                    text = ""
                    true
                } else {
                    false
                }
            },
            value = text,
            onValueChange = { text = it },
            singleLine = true,
            label = { Text("Neues TODO") })
    }
}