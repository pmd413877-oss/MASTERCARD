package com.example.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewQuilt
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Movie
import androidx.compose.ui.graphics.vector.ImageVector

enum class AiModule(
  val title: String,
  val subtitle: String,
  val icon: ImageVector,
  val badge: String? = null
) {
  DASHBOARD(
    title = "Dashboard",
    subtitle = "Creative Studio Hub",
    icon = Icons.AutoMirrored.Filled.ViewQuilt
  ),
  IMAGE_GEN(
    title = "AI Image Studio",
    subtitle = "Multi-style text-to-image generator",
    icon = Icons.Default.Image,
    badge = "Imagen 3"
  ),
  VIDEO_GEN(
    title = "AI Video Studio",
    subtitle = "Cinematic temporal video generation",
    icon = Icons.Default.Movie,
    badge = "Veo 2"
  ),
  MOTION_GEN(
    title = "Motion & Animation",
    subtitle = "Kinetic typography & vector reveals",
    icon = Icons.Default.Animation,
    badge = "Neural FX"
  )
}
