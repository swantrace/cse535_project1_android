package com.example.smarthome.pages

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import android.content.Context
import android.net.Uri
import android.widget.VideoView
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.text.style.TextAlign
import com.example.smarthome.Gestures

@SuppressLint("DiscouragedApi")
@Composable
fun GestureDetail(selectedRoute: MutableState<String>, context: Context) {
  val videoFileName = selectedRoute.value.split("/").last()
  val videoResId = context.resources.getIdentifier(videoFileName, "raw", context.packageName)
  val videoPath = "android.resource://${context.packageName}/$videoResId"
  val videoUri = Uri.parse(videoPath)
  val videoViewRef = remember { mutableStateOf<VideoView?>(null) }

  Column(modifier = Modifier
    .fillMaxSize()
    .padding(8.dp, 16.dp, 8.dp, 4.dp)) {

    // display the name of the gesture
    Text(
      text = Gestures[videoFileName]?.label ?: "Unknown",
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 8.dp, start = 4.dp),
      style = MaterialTheme.typography.titleLarge,
      textAlign = TextAlign.Start
    )

    // Limit the size of the video view
    Box(modifier = Modifier
      .fillMaxWidth()
      .weight(1f)) { // Use weight to allow the video to expand, leaving space for buttons
      AndroidView(factory = { context ->
        VideoView(context).apply {
          setVideoURI(videoUri)
          videoViewRef.value = this
        }
      }, modifier = Modifier.fillMaxSize())
    }

    // Place buttons in a row below the video
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(start = 14.dp, end = 14.dp, top = 8.dp, bottom = 8.dp),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Button(
        onClick = { videoViewRef.value?.start() },
        modifier = Modifier
          .weight(1f) // Give each button equal weight
          .padding(end = 4.dp) // Add padding between buttons
      ) {
        Text(text = "PLAY")
      }

      Button(
        onClick = {
          videoViewRef.value?.stopPlayback()
          selectedRoute.value = "gesture_recording/$videoFileName"
        },
        modifier = Modifier
          .weight(1f) // Give each button equal weight
          .padding(start = 4.dp) // Add padding between buttons
      ) {
        Text(text = "PRACTICE")
      }
    }
  }
}
