package com.example.smarthome.pages

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.camera.view.video.OnVideoSavedCallback
import androidx.camera.view.video.OutputFileOptions
import androidx.camera.view.video.OutputFileResults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.smarthome.Gesture
import com.example.smarthome.Gestures
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.io.IOException

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GestureRecording(
  selectedRoute: MutableState<String>,
  context: Context,
) {
  val cameraPermissionState: PermissionState =
    rememberPermissionState(android.Manifest.permission.CAMERA)
  val hasPermission = cameraPermissionState.status.isGranted
  val requestPermission = {cameraPermissionState.launchPermissionRequest()}
  GestureRecordingMainContent(
    hasPermission = hasPermission,
    requestPermission = requestPermission,
    selectedRoute = selectedRoute,
    context = context,
  )
}


@Composable
fun GestureRecordingMainContent(
  hasPermission: Boolean,
  requestPermission: () -> Unit,
  selectedRoute: MutableState<String>,
  context: Context,
) {
  if (hasPermission) {
    GestureRecordingContent(selectedRoute, context)
  } else {
    GestureRecordingPermissionRequest(requestPermission)
  }
}

@Composable
fun GestureRecordingPermissionRequest(requestPermission: () -> Unit) {
//  Text(text = "Please grant camera permission to record gesture")
  Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Text(text = "Please grant camera permission to record gesture")
    Button(onClick = requestPermission) {
      Text(text = "Grant permission")
    }
  }
}

@androidx.annotation.OptIn(androidx.camera.view.video.ExperimentalVideo::class)
@Composable
fun GestureRecordingContent(
  selectedRoute: MutableState<String>,
  context: Context,
) {
  val gestureKey = selectedRoute.value.split("/").last()
  val gesture = Gestures[gestureKey]
  val gestureName = gesture?.name ?: "Unknown"
  val gesturePracticeNumber = gesture?.getPracticeNumber(context) ?: 0
  val userLastName = "Hong"
  val videoFileName = "${gestureName}_PRACTICE_${gesturePracticeNumber}_${userLastName}.mp4"
  val videoFile = File(context.filesDir, videoFileName)
  val localContext = LocalContext.current
  val lifecycleOwner = LocalLifecycleOwner.current
  val cameraController = remember {
    LifecycleCameraController(localContext).apply {
      cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
      setEnabledUseCases(LifecycleCameraController.VIDEO_CAPTURE)
    }
  }
  val isRecording = remember {
    mutableStateOf(false)
  }
  val isUploadButtonEnabled = remember {
    mutableStateOf(false)
  }
  val isUploading = remember {
    mutableStateOf(false)
  }

  Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .padding(paddingValues)
    ) {

      // display the nth practice of the gesture


      AndroidView(factory = { context ->
        PreviewView(context).apply {
          this.controller = cameraController
          cameraController.bindToLifecycle(lifecycleOwner)
          implementationMode = PreviewView.ImplementationMode.COMPATIBLE
          layoutParams = ViewGroup.LayoutParams(
            MATCH_PARENT, MATCH_PARENT
          )
          setBackgroundColor(Color.BLACK)
          scaleType = PreviewView.ScaleType.FILL_CENTER
        }
      })

      Text(
        text = "Practice $gesturePracticeNumber of $gestureName",
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 8.dp, start = 16.dp),
        style = MaterialTheme.typography.titleLarge,
        textAlign = TextAlign.Start
      )

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .align(Alignment.BottomCenter)
          .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround
      ) {
        Button(onClick = {
          videoFile.delete()
          selectedRoute.value = "gesture_detail/$gestureKey"
        }) {
          Text(text = "BACK")
        }
        Button(
          onClick = {
            // startRecording or play last recording
            recordVideo(
              context = context,
              cameraController = cameraController,
              videoFile = videoFile,
              isRecording = isRecording,
              isUploadButtonEnabled = isUploadButtonEnabled
            )
          }, enabled = !isRecording.value, colors = ButtonDefaults.buttonColors(
            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
            disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
          )
        ) {
          Text(text = if (isRecording.value) "RECORDING" else "RECORD")
        }
        Button(
          onClick = {
            uploadVideo(videoFile, selectedRoute, gesture, context, isUploading)
          }, enabled = isUploadButtonEnabled.value && !isUploading.value, colors = ButtonDefaults.buttonColors(
            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
            disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
          )
        ) {
          Text(text = if (isUploading.value) "UPLOADING" else "UPLOAD")
        }
      }
    }
  }
}

@androidx.camera.view.video.ExperimentalVideo
fun recordVideo(
  context: Context,
  cameraController: LifecycleCameraController,
  videoFile: File,
  isRecording: MutableState<Boolean>,
  isUploadButtonEnabled: MutableState<Boolean>
) {
  if (cameraController.isRecording) {
    return
  } else {
    isRecording.value = true
    val fileOutputOptions = OutputFileOptions.builder(videoFile).build()
    val executor = ContextCompat.getMainExecutor(context)

    cameraController.startRecording(
      fileOutputOptions,
      executor,
      object : OnVideoSavedCallback {
        override fun onVideoSaved(outputFileResults: OutputFileResults) {
          isUploadButtonEnabled.value = true
          Toast.makeText(context, "Video recording succeeded", Toast.LENGTH_SHORT).show()
        }

        override fun onError(videoCaptureError: Int, message: String, cause: Throwable?) {
          Toast.makeText(context, "Video recording failed", Toast.LENGTH_SHORT).show()
        }
      }
    )

    CoroutineScope(Dispatchers.Main).launch {
      delay(3000)
      isRecording.value = false
      cameraController.stopRecording()
    }
  }
}


fun uploadVideo(
  videoFile: File,
  selectedRoute: MutableState<String>,
  gesture: Gesture?,
  context: Context,
  isUploading: MutableState<Boolean>
) {
  // upload video to server http://10.0.0.172:5000/upload
  val client = OkHttpClient
    .Builder()
    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
    .build()

  val requestBody = MultipartBody.Builder()
    .setType(MultipartBody.FORM)
    .addFormDataPart(
      "file",
      videoFile.name,
      videoFile.asRequestBody("video/mp4".toMediaTypeOrNull())
    )
    .build()

  val request = Request.Builder().url("http://10.0.0.172:5000/upload").post(requestBody).build()

  isUploading.value = true

  client.newCall(request).enqueue(object : Callback {
    override fun onFailure(call: Call, e: IOException) {
      isUploading.value = false
      Toast.makeText(context, "Video upload failed", Toast.LENGTH_SHORT).show()
      e.printStackTrace()
    }

    override fun onResponse(call: Call, response: Response) {
      if (!response.isSuccessful) {
        throw IOException("Unexpected code $response")
      }
      isUploading.value = false
      gesture?.incrementPracticeNumber(context)
      videoFile.delete()
      selectedRoute.value = "gesture_list"
    }
  })
}