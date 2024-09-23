package com.nate.frequentask.task

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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import frequentaskmulti.composeapp.generated.resources.Res
import frequentaskmulti.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun ShowMarkDoneDialog(onDismiss: () -> Unit, onMarkDone: (String) -> Unit) {
    var note by remember { mutableStateOf(TextFieldValue("")) }

    Dialog(onDismissRequest = { onDismiss() }, content = {
        Box(
            modifier = Modifier.border(1.dp, MaterialTheme.colorScheme.onPrimaryContainer)
                .background(MaterialTheme.colorScheme.surfaceVariant).padding(4.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text(
                    text = stringResource(Res.string.add_note),
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                BasicTextField(
                    value = note,
                    onValueChange = { note = it },
                    modifier = Modifier.fillMaxWidth()
                        .border(1.dp, MaterialTheme.colorScheme.onPrimaryContainer)
                        .background(MaterialTheme.colorScheme.background).padding(4.dp),
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
                            onMarkDone(note.text)
                            onDismiss()
                        }, modifier = Modifier.width(140.dp)
                    ) {
                        Text(text = stringResource(Res.string.complete_task))
                    }
                }
            }
        }

    })
}
