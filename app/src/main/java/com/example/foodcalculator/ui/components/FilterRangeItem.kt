package com.example.foodcalculator.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun FilterRangeItem(
    label: String,
    unit: String,
    text: MutableState<String>
) {
    Column {
        Text(text = "$label, $unit:")
        OutlinedTextField(
            value = text.value,
            onValueChange = {
                text.value = it
            },
            modifier = Modifier
                .fillMaxWidth()
        )
        Text(
            text = "Example: MIN-MAX, MAX or MIN+",
            fontSize = 12.sp,
            color = Color.LightGray
        )
    }
}