package com.example.smarthome

import android.content.Context

data class Gesture(val label: String, val fileName: String, val name: String) {
  companion object {
    private const val PREFS_NAME = "com.example.smarthome"
    private const val PRACTICE_NUMBER = "_practice_number"
  }

  fun getPracticeNumber(context: Context): Int {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    return prefs.getInt(fileName + PRACTICE_NUMBER, 0)
  }

  fun incrementPracticeNumber(context: Context) {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val currentNumber = getPracticeNumber(context)
    prefs.edit().putInt(fileName + PRACTICE_NUMBER, currentNumber + 1).apply()
  }
}

object Gestures {
  private val h_0 = Gesture("Gesture 0", "h_0", "Num0")
  private val h_1 = Gesture("Gesture 1", "h_1", "Num1")
  private val h_2 = Gesture("Gesture 2", "h_2", "Num2")
  private val h_3 = Gesture("Gesture 3", "h_3", "Num3")
  private val h_4 = Gesture("Gesture 4", "h_4", "Num4")
  private val h_5 = Gesture("Gesture 5", "h_5", "Num5")
  private val h_6 = Gesture("Gesture 6", "h_6", "Num6")
  private val h_7 = Gesture("Gesture 7", "h_7", "Num7")
  private val h_8 = Gesture("Gesture 8", "h_8", "Num8")
  private val h_9 = Gesture("Gesture 9", "h_9", "Num9")
  private val h_decrease_fan_speed =
    Gesture("Decrease fan speed", "h_decrease_fan_speed", "FanDown")
  private val h_fan_off = Gesture("Turn off fan", "h_fan_off", "FanOff")
  private val h_fan_on = Gesture("Turn on fan", "h_fan_on", "FanOn")
  private val h_increase_fan_speed = Gesture("Increase fan speed", "h_increase_fan_speed", "FanUp")
  private val h_light_off = Gesture("Turn off lights", "h_light_off", "LightOff")
  private val h_light_on = Gesture("Turn on lights", "h_light_on", "LightOn")
  private val h_set_thermo =
    Gesture("Set Thermostat to specified temperature", "h_set_thermo", "SetThermo")

  val list = listOf(
    h_light_on,
    h_light_off,
    h_fan_on,
    h_fan_off,
    h_increase_fan_speed,
    h_decrease_fan_speed,
    h_set_thermo,
    h_0,
    h_1,
    h_2,
    h_3,
    h_4,
    h_5,
    h_6,
    h_7,
    h_8,
    h_9,
  )

  fun get(fileName: String): Gesture? {
    return list.find { it.fileName == fileName }
  }
}