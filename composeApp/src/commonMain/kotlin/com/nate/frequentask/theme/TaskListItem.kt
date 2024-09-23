package com.nate.frequentask.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nate.frequentask.data.Theme
import com.nate.frequentask.displayDate
import frequentaskmulti.composeapp.generated.resources.Res
import frequentaskmulti.composeapp.generated.resources.delete
import frequentaskmulti.composeapp.generated.resources.next_due_on
import org.jetbrains.compose.resources.stringResource

@Composable
fun TaskListItem(
    task: Theme.Task, onTaskClick: (String) -> Unit, onTaskDelete: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxWidth().clickable { onTaskClick(task.id) }
        .background(MaterialTheme.colorScheme.background)
        .border(1.dp, MaterialTheme.colorScheme.surfaceVariant).padding(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            // Task Name
            Text(
                text = task.name,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )

            // Next Due On
            Text(
                text = formatNextDueOn(task.nextDueOn),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )

            // Kebab Menu
            IconButton(onClick = { isExpanded = !isExpanded }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
            }
        }
        if (isExpanded) {
            OutlinedButton(onClick = {
                onTaskDelete(task.id)
                isExpanded = false
            }, modifier = Modifier.align(Alignment.End)) {
                Text(text = stringResource(Res.string.delete))
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}

@Composable
fun formatNextDueOn(timestamp: Long): String {
    return stringResource(Res.string.next_due_on, timestamp.displayDate())
}
