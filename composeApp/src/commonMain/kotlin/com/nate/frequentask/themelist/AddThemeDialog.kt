package com.nate.frequentask.themelist

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.nate.frequentask.data.Theme
import frequentaskmulti.composeapp.generated.resources.Res
import frequentaskmulti.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun AddThemeDialog(
    onAddTheme: (Theme) -> Unit, onDismiss: () -> Unit
) {
    var themeName by remember { mutableStateOf(TextFieldValue("")) }
    Dialog(onDismissRequest = { onDismiss() }, content = {
        Box(
            modifier = Modifier.border(1.dp, MaterialTheme.colorScheme.onPrimaryContainer)
                .background(MaterialTheme.colorScheme.surfaceVariant).padding(4.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text(
                    text = stringResource(Res.string.add_theme),
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(value = themeName,
                    onValueChange = { themeName = it },
                    placeholder = { Text(text = stringResource(Res.string.theme_name)) },
                    modifier = Modifier.fillMaxWidth().padding(4.dp),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { onDismiss() }, modifier = Modifier.width(140.dp)
                    ) {
                        Text(text = stringResource(Res.string.cancel))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = {
                            val newTheme = Theme(
                                name = themeName.text,
                                active = true,
                                tasks = emptyList() // Initialize with an empty task list
                            )
                            onAddTheme(newTheme)
                            onDismiss()
                        }, modifier = Modifier.width(140.dp)
                    ) {
                        Text(text = stringResource(Res.string.add_theme))
                    }
                }
            }
        }
    })
}