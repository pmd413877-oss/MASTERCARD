package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Loop
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Toll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CreativeCreation
import com.example.data.model.GenerationState
import com.example.data.model.MotionGraphicStyle
import com.example.data.model.MotionType
import com.example.ui.theme.CretoAccent
import com.example.ui.theme.CretoAccentGold
import com.example.ui.theme.CretoBorder
import com.example.ui.theme.CretoPrimary
import com.example.ui.theme.CretoPrimaryGlow
import com.example.ui.theme.CretoSecondary
import com.example.ui.theme.CretoSurfaceCard
import com.example.ui.theme.CretoSurfaceElevated
import com.example.ui.theme.CretoSurfaceVariant
import com.example.ui.theme.CretoTextMuted
import com.example.ui.theme.CretoTextSecondary
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MotionGraphicsScreen(
  prompt: String,
  selectedStyle: MotionGraphicStyle,
  selectedType: MotionType,
  generationState: GenerationState<CreativeCreation>,
  onPromptChanged: (String) -> Unit,
  onStyleSelected: (MotionGraphicStyle) -> Unit,
  onTypeSelected: (MotionType) -> Unit,
  onGenerateMotion: () -> Unit,
  onExportLottie: () -> Unit,
  onExportMp4: () -> Unit,
  modifier: Modifier = Modifier
) {
  val scrollState = rememberScrollState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(scrollState)
      .padding(horizontal = 16.dp, vertical = 8.dp)
  ) {
    Text(
      text = "AI Motion Graphics",
      style = MaterialTheme.typography.displayMedium,
      color = Color.White
    )
    Text(
      text = "Kinetic typography, logo reveals, and vector explainer animations",
      style = MaterialTheme.typography.bodyMedium,
      color = CretoTextSecondary,
      fontSize = 12.sp
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Specialized Motion Prompt Input
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .background(CretoSurfaceCard)
        .border(1.dp, CretoBorder, RoundedCornerShape(16.dp))
        .padding(14.dp)
    ) {
      Column {
        Text(
          text = "ANIMATION SPECIFICATION",
          style = MaterialTheme.typography.labelMedium,
          color = CretoTextMuted,
          letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
          value = prompt,
          onValueChange = onPromptChanged,
          placeholder = {
            Text(
              "Describe typography movement, logo reveal, or vector transitions (e.g. Kinetic typography 'CRETO AI' with neon particle shockwave)",
              color = CretoTextMuted,
              fontSize = 13.sp
            )
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .testTag("motion_prompt_input"),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = CretoAccent,
            unfocusedBorderColor = CretoBorder,
            focusedContainerColor = CretoSurfaceVariant,
            unfocusedContainerColor = CretoSurfaceVariant,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
          )
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Animation Type Selector
    Text(
      text = "ANIMATION TYPE",
      style = MaterialTheme.typography.labelMedium,
      color = CretoTextMuted,
      letterSpacing = 1.sp
    )
    Spacer(modifier = Modifier.height(8.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      MotionType.entries.forEach { type ->
        val selected = selectedType == type
        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(10.dp))
            .background(
              if (selected) CretoAccent.copy(alpha = 0.25f) else CretoSurfaceVariant
            )
            .border(
              1.dp,
              if (selected) CretoAccent else CretoBorder,
              RoundedCornerShape(10.dp)
            )
            .clickable { onTypeSelected(type) }
            .padding(vertical = 10.dp, horizontal = 4.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = type.label,
            style = MaterialTheme.typography.labelSmall,
            color = if (selected) Color.White else CretoTextSecondary,
            fontSize = 10.sp,
            maxLines = 1
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Style Controls (Flat 2D, Minimalist vector, Neon kinetic, 3D Glitch, Liquid Morph)
    Text(
      text = "MOTION GRAPHIC STYLE",
      style = MaterialTheme.typography.labelMedium,
      color = CretoTextMuted,
      letterSpacing = 1.sp
    )
    Spacer(modifier = Modifier.height(8.dp))

    FlowRow(
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      MotionGraphicStyle.entries.forEach { style ->
        val selected = selectedStyle == style
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(
              if (selected) CretoPrimary.copy(alpha = 0.35f) else CretoSurfaceVariant
            )
            .border(
              1.dp,
              if (selected) CretoPrimaryGlow else CretoBorder,
              RoundedCornerShape(10.dp)
            )
            .clickable { onStyleSelected(style) }
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .testTag("motion_style_${style.name.lowercase()}")
        ) {
          Text(
            text = style.label,
            style = MaterialTheme.typography.labelMedium,
            color = if (selected) Color.White else CretoTextSecondary
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Generate Motion Graphics Button
    val isGenerating = generationState is GenerationState.Progress
    Button(
      onClick = onGenerateMotion,
      enabled = !isGenerating,
      modifier = Modifier
        .fillMaxWidth()
        .height(52.dp)
        .testTag("generate_motion_button"),
      colors = ButtonDefaults.buttonColors(
        containerColor = CretoAccent
      ),
      shape = RoundedCornerShape(14.dp)
    ) {
      if (isGenerating) {
        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.5.dp)
        Spacer(modifier = Modifier.width(10.dp))
        Text("Compiling Kinetic Shaders...", color = Color.White, style = MaterialTheme.typography.titleMedium)
      } else {
        Icon(imageVector = Icons.Default.Animation, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text("Generate Motion Graphics", color = Color.White, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.width(10.dp))
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Black.copy(alpha = 0.3f))
            .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.Toll, contentDescription = null, tint = CretoAccentGold, modifier = Modifier.size(12.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("3 Credits", color = CretoAccentGold, style = MaterialTheme.typography.labelSmall)
          }
        }
      }
    }

    // Progress State
    AnimatedVisibility(visible = generationState is GenerationState.Progress) {
      val progress = generationState as? GenerationState.Progress
      if (progress != null) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(CretoSurfaceCard)
            .border(1.dp, CretoBorder, RoundedCornerShape(14.dp))
            .padding(16.dp)
        ) {
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              CircularProgressIndicator(
                color = CretoAccent,
                modifier = Modifier.size(18.dp),
                strokeWidth = 2.5.dp
              )
              Spacer(modifier = Modifier.width(10.dp))
              Text(
                text = progress.stage,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontSize = 13.sp
              )
            }
            Spacer(modifier = Modifier.height(10.dp))
            LinearProgressIndicator(
              progress = { progress.percent },
              modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
              color = CretoAccent,
              trackColor = CretoSurfaceVariant
            )
          }
        }
      }
    }

    // Dedicated Rendering Preview Window
    Spacer(modifier = Modifier.height(24.dp))
    Text(
      text = "REAL-TIME RENDERING PREVIEW WINDOW",
      style = MaterialTheme.typography.labelMedium,
      color = CretoTextMuted,
      letterSpacing = 1.sp
    )
    Spacer(modifier = Modifier.height(8.dp))

    val currentCreation = (generationState as? GenerationState.Success)?.result
    MotionPreviewWindow(
      creationTitle = currentCreation?.title ?: prompt.ifBlank { "CRETO AI" },
      style = selectedStyle,
      onExportLottie = onExportLottie,
      onExportMp4 = onExportMp4
    )

    Spacer(modifier = Modifier.height(40.dp))
  }
}

@Composable
fun MotionPreviewWindow(
  creationTitle: String,
  style: MotionGraphicStyle,
  onExportLottie: () -> Unit,
  onExportMp4: () -> Unit
) {
  var isPlaying by remember { mutableStateOf(true) }
  var animProgress by remember { mutableFloatStateOf(0f) }
  var speedMultiplier by remember { mutableFloatStateOf(1.0f) }
  var showCodeModal by remember { mutableStateOf(false) }

  // 60 FPS animation ticker
  LaunchedEffect(isPlaying, speedMultiplier) {
    while (isPlaying) {
      delay(33) // ~30-60fps
      animProgress += (0.015f * speedMultiplier)
      if (animProgress > 1f) {
        animProgress = 0f
      }
    }
  }

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(18.dp))
      .background(CretoSurfaceCard)
      .border(1.dp, CretoBorder, RoundedCornerShape(18.dp))
  ) {
    Column {
      // Live Canvas Rendering Screen
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(240.dp)
          .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
          .background(Color(0xFF07060D)),
        contentAlignment = Alignment.Center
      ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
          val centerX = size.width / 2f
          val centerY = size.height / 2f
          val phase = animProgress * 2f * Math.PI.toFloat()

          // Background ambient grid
          val gridSpacing = 40f
          for (x in 0..(size.width / gridSpacing).toInt()) {
            drawLine(
              color = Color.White.copy(alpha = 0.04f),
              start = Offset(x * gridSpacing, 0f),
              end = Offset(x * gridSpacing, size.height),
              strokeWidth = 1f
            )
          }

          // Dynamic pulsing geometric rings
          val pulseRadius = 70f + 25f * sin(phase)
          drawCircle(
            brush = Brush.radialGradient(
              listOf(CretoPrimaryGlow.copy(alpha = 0.3f), Color.Transparent),
              center = Offset(centerX, centerY),
              radius = pulseRadius * 1.5f
            ),
            radius = pulseRadius * 1.5f,
            center = Offset(centerX, centerY)
          )

          drawCircle(
            color = CretoSecondary,
            radius = pulseRadius,
            center = Offset(centerX, centerY),
            style = Stroke(width = 2.5f)
          )

          // Orbiting particles
          val particleCount = 12
          for (i in 0 until particleCount) {
            val angle = phase + (i * 2f * Math.PI.toFloat() / particleCount)
            val dist = 100f + 15f * cos(angle * 2)
            val px = centerX + cos(angle) * dist
            val py = centerY + sin(angle) * dist
            drawCircle(
              color = if (i % 2 == 0) CretoAccent else CretoSecondary,
              radius = 4f,
              center = Offset(px, py)
            )
          }

          // Rotating accent square
          drawRect(
            color = CretoAccent.copy(alpha = 0.4f),
            topLeft = Offset(centerX - 35f, centerY - 35f),
            size = androidx.compose.ui.geometry.Size(70f, 70f),
            style = Stroke(width = 1.5f)
          )
        }

        // Animated typography centered text
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            text = creationTitle.take(18).uppercase(),
            style = MaterialTheme.typography.displayMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            letterSpacing = 3.sp
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "• ${style.label} •",
            style = MaterialTheme.typography.labelSmall,
            color = CretoSecondary,
            fontSize = 11.sp
          )
        }

        // Live badge
        Box(
          modifier = Modifier
            .align(Alignment.TopStart)
            .padding(10.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(CretoAccent.copy(alpha = 0.2f))
            .border(1.dp, CretoAccent.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
          Text("LIVE 60FPS SHADER", color = CretoAccent, style = MaterialTheme.typography.labelSmall, fontSize = 9.sp)
        }
      }

      // Interactive Controls Timeline
      Column(modifier = Modifier.padding(14.dp)) {
        Slider(
          value = animProgress,
          onValueChange = {
            animProgress = it
            isPlaying = false
          },
          valueRange = 0f..1f,
          modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
            .testTag("motion_timeline_scrubber"),
          colors = SliderDefaults.colors(
            thumbColor = CretoAccent,
            activeTrackColor = CretoAccent,
            inactiveTrackColor = CretoSurfaceVariant
          )
        )

        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.fillMaxWidth()
        ) {
          IconButton(onClick = { isPlaying = !isPlaying }) {
            Icon(
              imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
              contentDescription = null,
              tint = Color.White
            )
          }

          // Speed cycle button
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(CretoSurfaceVariant)
              .clickable {
                speedMultiplier = when (speedMultiplier) {
                  1.0f -> 2.0f
                  2.0f -> 0.5f
                  else -> 1.0f
                }
              }
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Text("${speedMultiplier}x", color = CretoSecondary, style = MaterialTheme.typography.labelSmall)
          }

          Spacer(modifier = Modifier.weight(1f))

          // Export Lottie JSON
          Button(
            onClick = onExportLottie,
            colors = ButtonDefaults.buttonColors(containerColor = CretoSurfaceVariant),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.testTag("export_lottie_button")
          ) {
            Icon(imageVector = Icons.Default.Code, contentDescription = null, tint = CretoSecondary, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Lottie JSON", color = Color.White, style = MaterialTheme.typography.labelSmall)
          }

          Spacer(modifier = Modifier.width(8.dp))

          // Export MP4
          Button(
            onClick = onExportMp4,
            colors = ButtonDefaults.buttonColors(containerColor = CretoAccent),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.testTag("export_motion_mp4_button")
          ) {
            Icon(imageVector = Icons.Default.Download, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Export MP4", color = Color.White, style = MaterialTheme.typography.labelSmall)
          }
        }
      }
    }
  }
}
