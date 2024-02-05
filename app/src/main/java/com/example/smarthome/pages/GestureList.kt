package com.example.smarthome.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class Gesture(val name: String, val fileName: String)

@Composable
fun GestureList(selectedRoute: MutableState<String>) {
  // TODO: Implement gesture list
  val gestures = listOf(
    Gesture("Turn on lights", "h_light_on"),
    Gesture("Turn off lights", "h_light_off"),
    Gesture("Turn on fan", "h_fan_on"),
    Gesture("Turn off fan", "h_fan_off"),
    Gesture("Increase fan speed", "h_increase_fan_speed"),
    Gesture("Decrease fan speed", "h_decrease_fan_speed"),
    Gesture("Set Thermostat to specified temperature", "h_set_thermo"),
  ) + (0..9).map { Gesture("Gesture $it", "h_$it") }

  DropdownMenuBox(items = gestures, onItemClicked = { selectedRoute.value = "gesture_detail/$it" })
}

@Composable
fun DropdownMenuBox(
  items: List<Gesture>,
  onItemClicked: (String) -> Unit
) {
  var expanded by remember { mutableStateOf(false) }
  Box(
    modifier = Modifier
      .fillMaxSize()
      .wrapContentSize(Alignment.Center)
  ) {
    Surface(
      shape = RoundedCornerShape(4.dp),
      border = BorderStroke(1.dp, Color.Gray),
      color = MaterialTheme.colorScheme.surface,
      shadowElevation = 2.dp
    ) {
      Text(
        "Select a gesture",
        modifier = Modifier
          .clickable { expanded = true }
          .padding(16.dp)
          .clip(RoundedCornerShape(4.dp))
      )
    }
    DropdownMenu(
      expanded = expanded,
      onDismissRequest = { expanded = false },
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
      items.forEach { gesture ->
        DropdownMenuItem(onClick = {
          onItemClicked(gesture.fileName)
          expanded = false
        }, text = { Text(gesture.name) })
      }
    }
  }
}


