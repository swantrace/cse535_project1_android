package com.example.smarthome

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.smarthome.pages.GestureDetail
import com.example.smarthome.pages.GestureList
import com.example.smarthome.pages.GestureRecording

@Composable
fun App() {
    val selectedRoute = remember {
        mutableStateOf(Routes.GESTURE_LIST)
    }
    Box(modifier = Modifier.fillMaxSize()) {
        when (selectedRoute.value.split("/").first()) {
            Routes.GESTURE_LIST -> GestureList(selectedRoute)
            Routes.GESTURE_DETAIL -> GestureDetail(selectedRoute)
            Routes.GESTURE_RECORDING -> GestureRecording(selectedRoute)
        }
    }
}

object Routes {
    const val GESTURE_LIST = "gesture_list"
    const val GESTURE_DETAIL = "gesture_detail"
    const val GESTURE_RECORDING = "gesture_recording"
}