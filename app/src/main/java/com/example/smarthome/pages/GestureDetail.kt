package com.example.smarthome.pages

import android.content.Context
import android.net.Uri
import android.widget.MediaController
import android.widget.VideoView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun GestureDetail(selectedRoute: MutableState<String>, context: Context) {
  // TODO: Implement gesture list
  val videoFileName = selectedRoute.value.split("/").last()
  val videoResId = context.resources.getIdentifier(videoFileName, "raw", context.packageName)
  val videoPath = "android.resource://${context.packageName}/$videoResId"
  val videoUri = Uri.parse(videoPath)

  Box(modifier = Modifier.fillMaxSize()) {
    AndroidView(factory = {
      VideoView(it).apply {
        setVideoURI(videoUri)
        val mediaController = MediaController(it)
        setMediaController(mediaController)
        mediaController.setAnchorView(this)
        setOnPreparedListener { mp ->
          mediaController.show(6000)
        }

        setOnCompletionListener {
          mediaController.show(6000)
        }
      }
    })

    Button(
      onClick = {
        selectedRoute.value = "gesture_recording/$videoFileName"
      }, modifier = Modifier
        .align(Alignment.BottomCenter)
        .width(200.dp)
        .padding(16.dp)
    ) {
      Text(text = "PRACTICE")
    }
  }
}

