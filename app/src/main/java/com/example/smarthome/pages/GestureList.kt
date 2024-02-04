package com.example.smarthome.pages

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GestureList(selectedRoute: MutableState<String>) {
    // TODO: Implement gesture list
    Text(text = "Gesture List")
    Button(onClick = {
        selectedRoute.value = "gesture_detail/h_0"
    }, modifier = Modifier.padding(16.dp)) {
        Text(text = "Go to Detail")
    }
}

@Composable
fun GestureItem() {
    // TODO: Implement gesture item
}
