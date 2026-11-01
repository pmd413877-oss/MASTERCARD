package com.example.data.model

import java.util.UUID

enum class CreationType(val label: String) {
  IMAGE("Image"),
  VIDEO("Video"),
  MOTION_GRAPHICS("Motion FX")
}

data class CreativeCreation(
  val id: String = UUID.randomUUID().toString(),
  val type: CreationType,
  val title: String,
  val prompt: String,
  val enhancedPrompt: String? = null,
  val style: String,
  val aspectRatio: AspectRatioOption = AspectRatioOption.RATIO_1_1,
  val durationSeconds: Int? = null,
  val motionIntensity: String? = null,
  val drawableRes: Int? = null,
  val localUri: String? = null,
  val timestamp: Long = System.currentTimeMillis(),
  val isFavorite: Boolean = false,
  val resolution: String = "1080p",
  val animationScriptJson: String? = null
)
