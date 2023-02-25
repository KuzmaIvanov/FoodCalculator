package com.example.foodcalculator.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

/*Можно передавать еще целые composable (content диалогового окна) и изпользовать внутри Column, для переиспользования */
@Composable
fun PlantsFiltersDialog(
    onDismissRequest: () -> Unit = {},
    onApplyClick: () -> Unit = {}
    /*content: @Composable () -> Unit*/
) {
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Surface(
            modifier = Modifier
                .wrapContentHeight()
                .wrapContentWidth(),
            shape = MaterialTheme.shapes.large
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "SOME TEXT")
                Spacer(modifier = Modifier.height(24.dp))
                TextButton(
                    onClick = onApplyClick
                ) {
                    Text(text = "APPLY")
                }
            }
        }
    }
}