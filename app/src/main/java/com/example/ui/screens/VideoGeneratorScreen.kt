package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CameraMove
import com.example.data.model.CreativeCreation
import com.example.data.model.GenerationState
import com.example.data.model.MotionIntensity
import com.example.data.model.VideoDuration
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

@Composable
fun VideoGeneratorScreen(
  prompt: String,
  selectedDuration: VideoDuration,
  selectedMotionIntensity: MotionIntensity,
  selectedCameraMove: CameraMove,
  isEnhancing: Boolean,
  generationState: GenerationState<CreativeCreation>,
  onPromptChanged: (String) -> Unit,
  onDurationSelected: (VideoDuration) -> Unit,
  onMotionIntensitySelected: (MotionIntensity) -> Unit,
  onCameraMoveSelected: (CameraMove) -> Unit,
  onEnhancePrompt: () -> Unit,
  onGenerateVideo: () -> Unit,
  onDownloadVideo: () -> Unit,
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
      text = "AI Video Generator",
      style = MaterialTheme.typography.displayMedium,
      color = Color.White
    )
    Text(
      text = "Temporal neural video generation powered by Veo 2 architecture",
      style = MaterialTheme.typography.bodyMedium,
      color = CretoTextSecondary,
      fontSize = 12.sp
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Scene Prompt Card
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .background(CretoSurfaceCard)
        .border(1.dp, CretoBorder, RoundedCornerShape(16.dp))
        .padding(14.dp)
    ) {
      Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "SCENE PROMPT",
            style = MaterialTheme.typography.labelMedium,
            color = CretoTextMuted,
            letterSpacing = 1.sp
          )
          Spacer(modifier = Modifier.weight(1f))
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(CretoSecondary.copy(alpha = 0.2f))
              .border(1.dp, CretoSecondary.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
              .clickable(enabled = !isEnhancing) { onEnhancePrompt() }
              .padding(horizontal = 8.dp, vertical = 4.dp)
              .testTag("expand_scene_button")
          ) {
            if (isEnhancing) {
              CircularProgressIndicator(
                color = CretoSecondary,
                modifier = Modifier.size(12.dp),
                strokeWidth = 2.dp
              )
            } else {
              Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = CretoSecondary,
                modifier = Modifier.size(14.dp)
              )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = if (isEnhancing) "Directing..." else "Gemini Director",
              style = MaterialTheme.typography.labelSmall,
              color = CretoSecondary,
              fontSize = 11.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = prompt,
          onValueChange = onPromptChanged,
          placeholder = {
            Text(
              "Describe the dynamic video scene, actions, camera path, and atmosphere (e.g. Holographic android meditating in neon shrine)",
              color = CretoTextMuted,
              fontSize = 13.sp
            )
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .testTag("video_prompt_input"),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = CretoSecondary,
            unfocusedBorderColor = CretoBorder,
            focusedContainerColor = CretoSurfaceVariant,
            unfocusedContainerColor = CretoSurfaceVariant,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
          ),
          trailingIcon = {
            if (prompt.isNotBlank()) {
              IconButton(onClick = { onPromptChanged("") }) {
                Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear", tint = CretoTextMuted)
              }
            }
          }
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Duration Selector (5s, 10s, 15s)
    Text(
      text = "DURATION",
      style = MaterialTheme.typography.labelMedium,
      color = CretoTextMuted,
      letterSpacing = 1.sp
    )
    Spacer(modifier = Modifier.height(8.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      VideoDuration.entries.forEach { duration ->
        val selected = selectedDuration == duration
        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(
              if (selected) CretoSecondary.copy(alpha = 0.25f) else CretoSurfaceVariant
            )
            .border(
              1.dp,
              if (selected) CretoSecondary else CretoBorder,
              RoundedCornerShape(12.dp)
            )
            .clickable { onDurationSelected(duration) }
            .padding(10.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = duration.label,
              style = MaterialTheme.typography.titleMedium,
              color = if (selected) Color.White else CretoTextSecondary,
              fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = "${duration.creditCost} credits",
              style = MaterialTheme.typography.labelSmall,
              color = CretoAccentGold,
              fontSize = 10.sp
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Motion Intensity Controls
    Text(
      text = "MOTION INTENSITY",
      style = MaterialTheme.typography.labelMedium,
      color = CretoTextMuted,
      letterSpacing = 1.sp
    )
    Spacer(modifier = Modifier.height(8.dp))

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
      MotionIntensity.entries.forEach { intensity ->
        val selected = selectedMotionIntensity == intensity
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
              if (selected) CretoSurfaceElevated else CretoSurfaceVariant
            )
            .border(
              1.dp,
              if (selected) CretoPrimaryGlow else CretoBorder,
              RoundedCornerShape(12.dp)
            )
            .clickable { onMotionIntensitySelected(intensity) }
            .padding(12.dp)
            .testTag("motion_${intensity.name.lowercase()}")
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(if (selected) CretoPrimaryGlow else CretoBorder)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = intensity.label,
                style = MaterialTheme.typography.titleMedium,
                color = if (selected) Color.White else CretoTextSecondary,
                fontSize = 13.sp
              )
              Text(
                text = intensity.description,
                style = MaterialTheme.typography.bodyMedium,
                color = CretoTextMuted,
                fontSize = 11.sp
              )
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Camera Moves
    Text(
      text = "CAMERA TRAJECTORY",
      style = MaterialTheme.typography.labelMedium,
      color = CretoTextMuted,
      letterSpacing = 1.sp
    )
    Spacer(modifier = Modifier.height(8.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      CameraMove.entries.take(4).forEach { move ->
        val selected = selectedCameraMove == move
        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(
              if (selected) CretoPrimary.copy(alpha = 0.3f) else CretoSurfaceVariant
            )
            .border(
              1.dp,
              if (selected) CretoPrimary else CretoBorder,
              RoundedCornerShape(8.dp)
            )
            .clickable { onCameraMoveSelected(move) }
            .padding(vertical = 8.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = move.label,
            style = MaterialTheme.typography.labelSmall,
            color = if (selected) Color.White else CretoTextSecondary,
            fontSize = 10.sp
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Generate Video Button
    val isGenerating = generationState is GenerationState.Progress
    Button(
      onClick = onGenerateVideo,
      enabled = !isGenerating,
      modifier = Modifier
        .fillMaxWidth()
        .height(52.dp)
        .testTag("generate_video_button"),
      colors = ButtonDefaults.buttonColors(
        containerColor = CretoSecondary
      ),
      shape = RoundedCornerShape(14.dp)
    ) {
      if (isGenerating) {
        CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(20.dp), strokeWidth = 2.5.dp)
        Spacer(modifier = Modifier.width(10.dp))
        Text("Rendering Temporal Video...", color = Color.Black, style = MaterialTheme.typography.titleMedium)
      } else {
        Icon(imageVector = Icons.Default.Movie, contentDescription = null, tint = Color.Black, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text("Generate Video", color = Color.Black, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.width(10.dp))
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Black.copy(alpha = 0.2f))
            .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.Toll, contentDescription = null, tint = Color.Black, modifier = Modifier.size(12.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("${selectedDuration.creditCost} Credits", color = Color.Black, style = MaterialTheme.typography.labelSmall)
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
                color = CretoSecondary,
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
              color = CretoSecondary,
              trackColor = CretoSurfaceVariant
            )
          }
        }
      }
    }

    // Video Player Output
    val outputCreation = (generationState as? GenerationState.Success)?.result
    if (outputCreation != null) {
      Spacer(modifier = Modifier.height(24.dp))
      Text(
        text = "GENERATED VIDEO PREVIEW",
        style = MaterialTheme.typography.labelMedium,
        color = CretoTextMuted,
        letterSpacing = 1.sp
      )
      Spacer(modifier = Modifier.height(8.dp))

      CretoVideoPlayer(
        creation = outputCreation,
        onDownload = onDownloadVideo
      )
    }

    Spacer(modifier = Modifier.height(40.dp))
  }
}

@Composable
fun CretoVideoPlayer(
  creation: CreativeCreation,
  onDownload: () -> Unit
) {
  var isPlaying by remember { mutableStateOf(true) }
  var progressSec by remember { mutableFloatStateOf(0f) }
  val totalSec = creation.durationSeconds?.toFloat() ?: 10f

  // Simulated real-time 60fps playback animation loop
  LaunchedEffect(isPlaying) {
    while (isPlaying) {
      delay(50)
      progressSec += 0.05f
      if (progressSec >= totalSec) {
        progressSec = 0f
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
      // Visual Viewport with real-time animated Canvas overlay
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(230.dp)
          .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
          .background(Color.Black),
        contentAlignment = Alignment.Center
      ) {
        if (creation.drawableRes != null) {
          Image(
            painter = painterResource(id = creation.drawableRes),
            contentDescription = creation.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
          )
        }

        // Real-time canvas rendering simulated camera pan, flare, and particle flow
        Canvas(modifier = Modifier.fillMaxSize()) {
          val sweepX = (progressSec / totalSec) * size.width
          // Cinematic anamorphic lens flare
          drawCircle(
            brush = Brush.radialGradient(
              listOf(CretoSecondary.copy(alpha = 0.45f), Color.Transparent),
              center = Offset(sweepX, size.height * 0.35f),
              radius = 140f
            ),
            radius = 140f,
            center = Offset(sweepX, size.height * 0.35f)
          )
          // Atmospheric motion line
          drawLine(
            brush = Brush.horizontalGradient(
              listOf(Color.Transparent, CretoPrimaryGlow.copy(alpha = 0.6f), Color.Transparent),
              startX = sweepX - 100f,
              endX = sweepX + 100f
            ),
            start = Offset(sweepX - 100f, size.height * 0.35f),
            end = Offset(sweepX + 100f, size.height * 0.35f),
            strokeWidth = 2.5f
          )
        }

        // Center Play/Pause Overlay indicator when tapped
        Box(
          modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = 0.6f))
            .clickable { isPlaying = !isPlaying },
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
            contentDescription = "Toggle Playback",
            tint = Color.White,
            modifier = Modifier.size(30.dp)
          )
        }

        // Format badge
        Box(
          modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(10.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(Color.Black.copy(alpha = 0.7f))
            .padding(horizontal = 6.dp, vertical = 3.dp)
        ) {
          Text(
            text = "60 FPS • 1080p MP4",
            style = MaterialTheme.typography.labelSmall,
            color = CretoSecondary,
            fontSize = 10.sp
          )
        }
      }

      // Player Controls Bar
      Column(modifier = Modifier.padding(14.dp)) {
        // Scrubber
        Slider(
          value = progressSec,
          onValueChange = {
            progressSec = it
            isPlaying = false
          },
          valueRange = 0f..totalSec,
          modifier = Modifier
            .fillMaxWidth()
            .height(24.dp)
            .testTag("video_scrubber"),
          colors = SliderDefaults.colors(
            thumbColor = CretoSecondary,
            activeTrackColor = CretoSecondary,
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

          val curInt = progressSec.toInt()
          val totInt = totalSec.toInt()
          Text(
            text = "00:${if (curInt < 10) "0$curInt" else "$curInt"} / 00:${if (totInt < 10) "0$totInt" else "$totInt"}",
            style = MaterialTheme.typography.labelMedium,
            color = CretoTextSecondary
          )

          Spacer(modifier = Modifier.weight(1f))

          Button(
            onClick = onDownload,
            colors = ButtonDefaults.buttonColors(containerColor = CretoSecondary),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.testTag("download_mp4_button")
          ) {
            Icon(imageVector = Icons.Default.Download, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Download MP4", color = Color.Black, style = MaterialTheme.typography.labelMedium)
          }
        }
      }
    }
  }
}
