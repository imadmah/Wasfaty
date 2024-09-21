package com.example.wasfaty.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate

@Composable
fun CalendarPicker(
    onDaySelected: (Int) -> Unit
) {
    // Material Dialog for picking the date
    val dateDialog = rememberMaterialDialogState()

    Column(

    ) {
        dateDialog.show()
            MaterialDialog(
                dialogState = dateDialog,
                buttons = {
                    positiveButton("OK")
                    negativeButton("Cancel")
                }
            ) {
                // Use the date picker and handle the selected date
                datepicker { date: LocalDate ->
                    onDaySelected(date.dayOfMonth) // Pass only the selected day
                }
            }
    }
}

