package com.example.smarthome

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleCoroutineScope
import com.example.smarthome.pages.GestureDetail
import com.example.smarthome.pages.GestureList
import com.example.smarthome.pages.GestureRecording

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(lifecycleScope: LifecycleCoroutineScope) {
  val context = LocalContext.current
  val selectedRoute = remember {
    mutableStateOf(Routes.gesture_list.route)
  }
  Scaffold(topBar = {
    TopAppBar(
      colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary,
      ),
      title = {
        Text(
          text = "Smart Home${
            (Routes[selectedRoute.value
              .split("/")
              .first()] as Route).title.let { " - $it" }
          }",
          color = MaterialTheme.colorScheme.onPrimary,
        )
      }
    )
  }) { padding ->
    Box(
      contentAlignment = Alignment.TopStart,
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
    ) {
      when (selectedRoute.value.split("/").first()) {
        Routes.gesture_list.route      -> GestureList(selectedRoute)
        Routes.gesture_detail.route    -> GestureDetail(selectedRoute, context)
        Routes.gesture_recording.route -> GestureRecording(selectedRoute, context)
      }
    }
  }
}

data class Route(val route: String, val title: String)
object Routes {
  val gesture_list = Route("gesture_list", "Gesture List")
  val gesture_detail = Route("gesture_detail", "Gesture Detail")
  val gesture_recording = Route("gesture_recording", "Gesture Recording")
  operator fun get(route: String): Any {
    return when (route) {
      gesture_list.route      -> gesture_list
      gesture_detail.route    -> gesture_detail
      gesture_recording.route -> gesture_recording
      else                    -> Route("", "")
    }
  }
}