package com.nate.frequentask.today

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nate.frequentask.data.Theme
import com.nate.frequentask.data.isLate

@Composable
fun TodayListItem(
    task: Theme.Task, onTaskClick: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth().clickable { onTaskClick(task.id) }.padding(1.dp)
        .background(MaterialTheme.colorScheme.background)
        .let { if (task.isLate()) it.border(1.dp, MaterialTheme.colorScheme.error) else it }) {
        Column(
            modifier = Modifier.padding(4.dp).background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.Start
        ) {
            // Task Name
            Text(
                text = task.name,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }
    }
}