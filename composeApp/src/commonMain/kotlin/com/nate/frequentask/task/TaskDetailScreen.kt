package com.nate.frequentask.task

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nate.frequentask.Navigation
import com.nate.frequentask.components.FrequentTaskDatePickerDialog
import com.nate.frequentask.components.InfiniteSpinner
import com.nate.frequentask.data.Theme
import com.nate.frequentask.data.ThemeRepository
import com.nate.frequentask.data.complete
import com.nate.frequentask.data.updateFrequency
import com.nate.frequentask.data.updateLastCompleted
import com.nate.frequentask.displayDate
import com.nate.frequentask.navigationIcon
import frequentaskmulti.composeapp.generated.resources.Res
import frequentaskmulti.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    theme: Theme, task: Theme.Task, themeRepository: ThemeRepository, navController: NavController
) {
    var isEditingTitle by remember { mutableStateOf(false) }
    var isEditingDescription by remember { mutableStateOf(false) }
    var taskName by remember { mutableStateOf(task.name) }
    var taskDescription by remember { mutableStateOf(task.description) }
    var showNextDueDatePicker by remember { mutableStateOf(false) }
    var showLastCompletedDatePicker by remember { mutableStateOf(false) }
    var showMarkDoneDialog by remember { mutableStateOf(false) }
    var maxFrequency by remember { mutableIntStateOf(task.frequency.toInt() + 5) } // Max frequency is 5 days
    var items by remember {
        mutableStateOf((1..maxFrequency).toList())
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            if (isEditingTitle) {
                BasicTextField(
                    value = taskName,
                    onValueChange = { taskName = it },
                    textStyle = TextStyle.Default.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                )
            } else {
                Text(text = taskName)
            }
        },
            navigationIcon = navController.navigationIcon(),
            actions = {
                if (!isEditingTitle) {
                    IconButton(onClick = { isEditingTitle = true }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                    }
                } else {
                    IconButton(onClick = {
                        isEditingTitle = false
                        themeRepository.updateTaskName(theme.id, task, taskName)
                    }) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null)
                    }
                }
            })
    }, content = { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (showNextDueDatePicker) {
                FrequentTaskDatePickerDialog(onDateSelected = {
                    it?.also {
                        themeRepository.updateTask(
                            theme.id, task.copy(nextDueOn = it)
                        )
                    }
                }, onDismiss = { showNextDueDatePicker = false })
            }
            if (showLastCompletedDatePicker) {
                FrequentTaskDatePickerDialog(onDateSelected = {
                    it?.also {
                        themeRepository.updateTask(theme.id, task.updateLastCompleted(it))
                    }
                },
                    onDismiss = { showLastCompletedDatePicker = false },
                    allowPastDates = true
                )
            }
            if (showMarkDoneDialog) {
                ShowMarkDoneDialog(onDismiss = { showMarkDoneDialog = false }, onMarkDone = {
                    themeRepository.updateTask(theme.id, task.complete(it))
                    showMarkDoneDialog = false
                })
            }
            // Task details
            ElevatedButton(
                onClick = { showMarkDoneDialog = true },
                modifier = Modifier.scale(1.5f).padding(16.dp)
            ) {
                Text(stringResource(Res.string.complete_task))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(Res.string.task_description),
                style = MaterialTheme.typography.labelSmall
            )
            if (isEditingDescription) {
                val focusRequester = remember { FocusRequester() }
                TextField(
                    value = taskDescription,
                    onValueChange = { taskDescription = it },
                    textStyle = TextStyle.Default.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background).padding(16.dp)
                        .focusRequester(focusRequester),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        isEditingDescription = false
                    })
                )
                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }
            } else {
                Text(
                    text = taskDescription, modifier = Modifier.padding(8.dp).clickable {
                            isEditingDescription = true
                        }, color = MaterialTheme.colorScheme.onBackground
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(6.dp).weight(1f).clickable {
                        showLastCompletedDatePicker = true
                    }) {
                    Text(
                        text = stringResource(Res.string.last_completed),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = task.lastCompletedOn?.displayDate() ?: "-",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),

                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = task.note,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(4.dp).fillMaxWidth(),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Column(modifier = Modifier.padding(6.dp).weight(1f).clickable {
                        showNextDueDatePicker = true
                    }) {
                    Text(
                        text = stringResource(Res.string.next_due_on),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = task.nextDueOn.displayDate(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(Res.string.frequency),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(4.dp)
            )
            // Spinner for selecting frequency
            InfiniteSpinner(selectedItem = task.frequency.toInt(), onValueChange = {
                themeRepository.updateTask(
                    theme.id, task.updateFrequency(it)
                )
            }, more = {
                maxFrequency += 10
                items = (1..maxFrequency).toList()
            }, items = items
            )
        }
    })
}