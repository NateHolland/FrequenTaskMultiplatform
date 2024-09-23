package com.nate.frequentask.theme

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nate.frequentask.Navigation
import com.nate.frequentask.data.ThemeRepository
import com.nate.frequentask.displayGroupTitle
import com.nate.frequentask.displayGroupVal
import com.nate.frequentask.navigationIcon

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ThemeDetailScreen(
    navController: NavController,
    themeID: String,
    themeRepository: ThemeRepository
) {
    var isEditing by remember { mutableStateOf(false) }
    var isAddTaskDialogVisible by remember { mutableStateOf(false) }
    val themeList by remember { themeRepository.themes }
    val theme = themeList.find { it.id == themeID }!!
    var themeName by remember { mutableStateOf(theme.name) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (isEditing) {
                        BasicTextField(
                            value = themeName,
                            onValueChange = { themeName = it },
                            textStyle = TextStyle.Default.copy(
                                color = MaterialTheme.colorScheme.onBackground
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    } else {
                        Text(text = themeName)
                    }
                }, navigationIcon = navController.navigationIcon(), actions = {
                    if (!isEditing) {
                        IconButton(
                            onClick = { isEditing = true }
                        ) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                        }
                    } else {
                        IconButton(
                            onClick = {
                                isEditing = false
                                themeRepository.updateThemeName(themeID, themeName)
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null)
                        }
                    }
                    IconButton(
                        onClick = { isAddTaskDialogVisible = true }
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    }
                }
            )
        },
        content = { padding ->
            if (isAddTaskDialogVisible) {
                AddTaskDialog(
                    onAddTask = { newTask ->
                        themeRepository.addTask(themeID, newTask)
                        isAddTaskDialogVisible = false
                    },
                    onDismiss = { isAddTaskDialogVisible = false }
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = padding.calculateTopPadding()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    theme.tasks.sortedBy { it.nextDueOn }.groupBy { it.nextDueOn.displayGroupVal() }
                        .map { taskGroup ->
                            stickyHeader {
                                Text(
                                    text = taskGroup.key.displayGroupTitle(),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(if (taskGroup.key.contentEquals("past")) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.surfaceVariant)
                                        .padding(4.dp),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = if (taskGroup.key.contentEquals("past")) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                            items(taskGroup.value) { task ->
                                TaskListItem(
                                    task = task,
                                    onTaskClick = {
                                        navController.navigate(
                                            Navigation.task(
                                                theme.id,
                                                it
                                            )
                                        )
                                    },
                                    onTaskDelete = { themeRepository.deleteTask(themeID, it) }
                                )
                            }
                        }
                }
            }
        }
    )
}

