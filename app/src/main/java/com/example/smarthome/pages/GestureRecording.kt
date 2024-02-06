package com.example.smarthome.pages

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.smarthome.Gestures
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import java.io.File
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GestureRecording(selectedRoute: MutableState<String>, context: Context) {
  val gestureKey = selectedRoute.value.split("/").last()
  val gesture = Gestures[gestureKey]
  val gestureName = gesture?.name ?: "Unknown"
  val gesturePracticeNumber = gesture?.getPracticeNumber(context) ?: 0
  val userLastName = "Hong"
  val videoFileName = "${gestureName}_PRACTICE_${gesturePracticeNumber}_${userLastName}.mp4"
  val videoFile = File(context.filesDir, videoFileName)

  val cameraPermissionState: PermissionState =
    rememberPermissionState(android.Manifest.permission.CAMERA)
  val hasPermission = cameraPermissionState.status.isGranted
  val requestPermission = cameraPermissionState::launchPermissionRequest

  GestureRecordingMainContent(hasPermission, requestPermission, videoFile = videoFile)

  Button(onClick = { selectedRoute.value = "gesture_list" }) {
    Text(text = "Back")
  }
}

@Composable
fun GestureRecordingMainContent(
  hasPermission: Boolean,
  requestPermission: () -> Unit,
  videoFile: File
) {
  if (hasPermission) {
    GestureRecordingContent(videoFile)
  } else {
    GestureRecordingPermissionRequest(requestPermission)
  }
}

@Composable
fun GestureRecordingPermissionRequest(requestPermission: () -> Unit) {
//  Text(text = "Please grant camera permission to record gesture")
  Column(
    modifier = Modifier.width(200.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Text(text = "Please grant camera permission to record gesture")
    Button(onClick = requestPermission) {
      Text(text = "Grant permission")
    }
  }
}

@Composable
fun GestureRecordingContent(videoFile: File) {
  Scaffold(modifier = Modifier.fillMaxSize()) {paddingValues ->
    Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
      AndroidView(factory = { context ->
        PreviewView(context).apply {
          implementationMode = PreviewView.ImplementationMode.COMPATIBLE
          layoutParams = ViewGroup.LayoutParams(
            MATCH_PARENT, MATCH_PARENT
          )
          setBackgroundColor(Color.BLACK)
          scaleType = PreviewView.ScaleType.FILL_CENTER
        }
      })
    }
  }
}