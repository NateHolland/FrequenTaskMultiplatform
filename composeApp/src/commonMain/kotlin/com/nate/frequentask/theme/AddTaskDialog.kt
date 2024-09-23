package com.nate.frequentask.theme

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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.nate.frequentask.components.InfiniteSpinner
import com.nate.frequentask.data.Theme
import com.nate.frequentask.now
import frequentaskmulti.composeapp.generated.resources.Res
import frequentaskmulti.composeapp.generated.resources.*
import kotlinx.datetime.Clock
import org.jetbrains.compose.resources.stringResource

@Composable
fun AddTaskDialog(
    onAddTask: (Theme.Task) -> Unit, onDismiss: () -> Unit
) {
    var taskName by remember { mutableStateOf(TextFieldValue("")) }
    var taskDescription by remember { mutableStateOf(TextFieldValue("")) }
    var taskFrequency by remember { mutableLongStateOf(1L) } // Default frequency is every day
    var maxFrequency by remember { mutableIntStateOf(30) } // Max frequency is every 5 days
    var items by remember {
        mutableStateOf((1..maxFrequency).toList())
    }

    Dialog(onDismissRequest = { onDismiss() }, content = {
        Box(
            modifier = Modifier.border(1.dp, MaterialTheme.colorScheme.onPrimaryContainer)
                .background(MaterialTheme.colorScheme.surfaceVariant).padding(4.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text(
                    text = stringResource(Res.string.add_task),
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(value = taskName,
                    onValueChange = { taskName = it },
                    placeholder = { Text(text = stringResource(Res.string.task_name)) },
                    modifier = Modifier.fillMaxWidth().padding(4.dp),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(value = taskDescription,
                    onValueChange = { taskDescription = it },
                    placeholder = { Text(text = stringResource(Res.string.task_description)) },
                    modifier = Modifier.fillMaxWidth().padding(4.dp),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(Res.string.frequency),
                    style = MaterialTheme.typography.labelMedium
                )
                // Spinner for selecting frequency
                InfiniteSpinner(
                    selectedItem = taskFrequency.toInt(),
                    onValueChange = { taskFrequency = it.toLong() },
                    more = {
                        maxFrequency += 10
                        items = (1..maxFrequency).toList()
                    },
                    items = items
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
                    Button(
                        onClick = {
                            val newTask = Theme.Task(
                                name = taskName.text,
                                description = taskDescription.text,
                                frequency = taskFrequency,
                                nextDueOn = now(),
                                note = ""
                            )
                            onAddTask(newTask)
                            onDismiss()
                        }, modifier = Modifier.width(140.dp)
                    ) {
                        Text(text = stringResource(Res.string.add_task))
                    }
                }
            }
        }
    })
}
