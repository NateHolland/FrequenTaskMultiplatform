package com.nate.frequentask.components

import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import com.nate.frequentask.now
import frequentaskmulti.composeapp.generated.resources.Res
import frequentaskmulti.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrequentTaskDatePickerDialog(
    onDateSelected: (Long?) -> Unit, onDismiss: () -> Unit, allowPastDates: Boolean = false
) {
    val datePickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return allowPastDates || utcTimeMillis >= now()
        }
    })

    val selectedDate = datePickerState.selectedDateMillis

    DatePickerDialog(onDismissRequest = { onDismiss() }, confirmButton = {
        Button(onClick = {
            onDateSelected(selectedDate)
            onDismiss()
        }

        ) {
            Text(text = stringResource(Res.string.confirm))
        }
    }, dismissButton = {
        Button(onClick = {
            onDismiss()
        }) {
            Text(text = stringResource(Res.string.cancel))
        }
    }) {
        DatePicker(
            state = datePickerState
        )
    }
}
