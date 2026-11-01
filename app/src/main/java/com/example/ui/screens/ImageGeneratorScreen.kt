package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Hd
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AspectRatioOption
import com.example.data.model.CreativeCreation
import com.example.data.model.GenerationState
import com.example.data.model.ImageStyle
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ImageGeneratorScreen(
  prompt: String,
  selectedStyle: ImageStyle,
  selectedRatio: AspectRatioOption,
  isEnhancing: Boolean,
  generationState: GenerationState<CreativeCreation>,
  onPromptChanged: (String) -> Unit,
  onStyleSelected: (ImageStyle) -> Unit,
  onRatioSelected: (AspectRatioOption) -> Unit,
  onEnhancePrompt: () -> Unit,
  onGenerate: () -> Unit,
  onOpenCreationDetail: (CreativeCreation) -> Unit,
  onDownloadImage: () -> Unit,
  onUpscaleImage: () -> Unit,
  modifier: Modifier = Modifier
) {
  val scrollState = rememberScrollState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(scrollState)
      .padding(horizontal = 16.dp, vertical = 8.dp)
  ) {
    // Header Title
    Text(
      text = "AI Image Generator",
      style = MaterialTheme.typography.displayMedium,
      color = Color.White
    )
    Text(
      text = "Powered by Google AI Studio Imagen 3 and Gemini prompt director",
      style = MaterialTheme.typography.bodyMedium,
      color = CretoTextSecondary,
      fontSize = 12.sp
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Prompt Input Card
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
            text = "PROMPT",
            style = MaterialTheme.typography.labelMedium,
            color = CretoTextMuted,
            letterSpacing = 1.sp
          )
          Spacer(modifier = Modifier.weight(1f))
          // Gemini Prompt Enhancer Button
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(CretoPrimary.copy(alpha = 0.2f))
              .border(1.dp, CretoPrimary.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
              .clickable(enabled = !isEnhancing) { onEnhancePrompt() }
              .padding(horizontal = 8.dp, vertical = 4.dp)
              .testTag("enhance_prompt_button")
          ) {
            if (isEnhancing) {
              CircularProgressIndicator(
                color = CretoPrimaryGlow,
                modifier = Modifier.size(12.dp),
                strokeWidth = 2.dp
              )
            } else {
              Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = CretoPrimaryGlow,
                modifier = Modifier.size(14.dp)
              )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = if (isEnhancing) "Enhancing..." else "Gemini Enhance",
              style = MaterialTheme.typography.labelSmall,
              color = CretoPrimaryGlow,
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
              "Describe the scene, subject, camera optics, or lighting (e.g. Cyberpunk Tokyo neon alley with holographic dragon)",
              color = CretoTextMuted,
              fontSize = 13.sp
            )
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .testTag("image_prompt_input"),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = CretoPrimary,
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

    // Style Selectors (Cinematic, Photorealistic, Anime, 3D Render, Cyberpunk)
    Text(
      text = "ARTISTIC STYLE",
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
      ImageStyle.entries.forEach { style ->
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
            .testTag("style_${style.name.lowercase()}")
        ) {
          Text(
            text = style.displayName,
            style = MaterialTheme.typography.labelMedium,
            color = if (selected) Color.White else CretoTextSecondary
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Aspect Ratio Selector (1:1, 16:9, 9:16)
    Text(
      text = "ASPECT RATIO",
      style = MaterialTheme.typography.labelMedium,
      color = CretoTextMuted,
      letterSpacing = 1.sp
    )
    Spacer(modifier = Modifier.height(8.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      AspectRatioOption.entries.forEach { ratio ->
        val selected = selectedRatio == ratio
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
            .clickable { onRatioSelected(ratio) }
            .padding(10.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Visual ratio mini-frame
            Box(
              modifier = Modifier
                .size(
                  width = when (ratio) {
                    AspectRatioOption.RATIO_1_1 -> 24.dp
                    AspectRatioOption.RATIO_16_9 -> 32.dp
                    AspectRatioOption.RATIO_9_16 -> 18.dp
                  },
                  height = when (ratio) {
                    AspectRatioOption.RATIO_1_1 -> 24.dp
                    AspectRatioOption.RATIO_16_9 -> 18.dp
                    AspectRatioOption.RATIO_9_16 -> 32.dp
                  }
                )
                .border(
                  1.5.dp,
                  if (selected) CretoSecondary else CretoTextMuted,
                  RoundedCornerShape(3.dp)
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = ratio.label,
              style = MaterialTheme.typography.titleMedium,
              color = if (selected) Color.White else CretoTextSecondary,
              fontSize = 12.sp
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Generate Image Button (Cost 2 Credits)
    val isGenerating = generationState is GenerationState.Progress
    Button(
      onClick = onGenerate,
      enabled = !isGenerating,
      modifier = Modifier
        .fillMaxWidth()
        .height(52.dp)
        .testTag("generate_image_button"),
      colors = ButtonDefaults.buttonColors(
        containerColor = CretoPrimary
      ),
      shape = RoundedCornerShape(14.dp)
    ) {
      if (isGenerating) {
        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.5.dp)
        Spacer(modifier = Modifier.width(10.dp))
        Text("Synthesizing Neural Pass...", color = Color.White, style = MaterialTheme.typography.titleMedium)
      } else {
        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text("Generate Image", color = Color.White, style = MaterialTheme.typography.titleMedium)
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
            Text("2 Credits", color = CretoAccentGold, style = MaterialTheme.typography.labelSmall)
          }
        }
      }
    }

    // Generation Progress Box
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
                color = CretoPrimary,
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
              color = CretoPrimary,
              trackColor = CretoSurfaceVariant
            )
          }
        }
      }
    }

    // Rendered Output Preview
    val outputCreation = (generationState as? GenerationState.Success)?.result
    if (outputCreation != null) {
      Spacer(modifier = Modifier.height(24.dp))
      Text(
        text = "GENERATED OUTPUT",
        style = MaterialTheme.typography.labelMedium,
        color = CretoTextMuted,
        letterSpacing = 1.sp
      )
      Spacer(modifier = Modifier.height(8.dp))

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(18.dp))
          .background(CretoSurfaceCard)
          .border(1.dp, CretoBorder, RoundedCornerShape(18.dp))
          .clickable { onOpenCreationDetail(outputCreation) }
      ) {
        Column {
          if (outputCreation.drawableRes != null) {
            Image(
              painter = painterResource(id = outputCreation.drawableRes),
              contentDescription = outputCreation.title,
              modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(outputCreation.aspectRatio.ratioWidth / outputCreation.aspectRatio.ratioHeight)
                .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp)),
              contentScale = ContentScale.Crop
            )
          }

          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = outputCreation.title,
              style = MaterialTheme.typography.titleLarge,
              color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Style: ${outputCreation.style} • ${outputCreation.resolution}",
              style = MaterialTheme.typography.bodyMedium,
              color = CretoSecondary,
              fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Button(
                onClick = onUpscaleImage,
                colors = ButtonDefaults.buttonColors(containerColor = CretoSurfaceVariant),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f).testTag("upscale_button")
              ) {
                Icon(imageVector = Icons.Default.Hd, contentDescription = null, tint = CretoSecondary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Upscale 4K", color = Color.White, style = MaterialTheme.typography.labelMedium)
              }
              Button(
                onClick = onDownloadImage,
                colors = ButtonDefaults.buttonColors(containerColor = CretoPrimary),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f).testTag("download_button")
              ) {
                Icon(imageVector = Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Download", color = Color.White, style = MaterialTheme.typography.labelMedium)
              }
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(40.dp))
  }
}
