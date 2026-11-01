package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Hd
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.CreationType
import com.example.data.model.CreativeCreation
import com.example.ui.theme.CretoAccent
import com.example.ui.theme.CretoAccentGold
import com.example.ui.theme.CretoBackground
import com.example.ui.theme.CretoBorder
import com.example.ui.theme.CretoPrimary
import com.example.ui.theme.CretoPrimaryGlow
import com.example.ui.theme.CretoSecondary
import com.example.ui.theme.CretoSurfaceCard
import com.example.ui.theme.CretoSurfaceElevated
import com.example.ui.theme.CretoSurfaceVariant
import com.example.ui.theme.CretoTextMuted
import com.example.ui.theme.CretoTextSecondary

@Composable
fun CreationDetailModal(
  creation: CreativeCreation,
  onDismiss: () -> Unit,
  onToggleFavorite: (String) -> Unit,
  onDownload: () -> Unit,
  onUpscale: () -> Unit,
  onCopyPrompt: (String) -> Unit
) {
  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxSize()
        .background(CretoBackground),
      color = CretoBackground
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .verticalScroll(rememberScrollState())
          .padding(16.dp)
      ) {
        // Modal Header
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.fillMaxWidth()
        ) {
          IconButton(onClick = onDismiss, modifier = Modifier.testTag("detail_close_button")) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
          }
          Spacer(modifier = Modifier.width(8.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = creation.title,
              style = MaterialTheme.typography.titleLarge,
              color = Color.White,
              maxLines = 1
            )
            Text(
              text = "${creation.type.label} • ${creation.style}",
              style = MaterialTheme.typography.bodyMedium,
              color = CretoSecondary,
              fontSize = 12.sp
            )
          }
          IconButton(onClick = { onToggleFavorite(creation.id) }) {
            Icon(
              imageVector = if (creation.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
              contentDescription = "Favorite",
              tint = if (creation.isFavorite) CretoAccent else CretoTextSecondary
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Visual Preview Media
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CretoSurfaceCard)
            .border(1.dp, CretoBorder, RoundedCornerShape(16.dp)),
          contentAlignment = Alignment.Center
        ) {
          if (creation.drawableRes != null) {
            Image(
              painter = painterResource(id = creation.drawableRes),
              contentDescription = creation.title,
              modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(creation.aspectRatio.ratioWidth / creation.aspectRatio.ratioHeight)
                .clip(RoundedCornerShape(16.dp)),
              contentScale = ContentScale.Crop
            )
          } else {
            // Motion Graphics or Vector preview container
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .background(
                  Brush.linearGradient(listOf(CretoSurfaceElevated, CretoSurfaceVariant))
                ),
              contentAlignment = Alignment.Center
            ) {
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                  text = creation.title,
                  style = MaterialTheme.typography.headlineLarge,
                  color = CretoPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                  text = "Neural Motion Vector Render",
                  style = MaterialTheme.typography.bodyMedium,
                  color = CretoTextSecondary
                )
              }
            }
          }

          // Resolution badge
          Box(
            modifier = Modifier
              .align(Alignment.BottomEnd)
              .padding(12.dp)
              .clip(RoundedCornerShape(6.dp))
              .background(Color.Black.copy(alpha = 0.7f))
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Text(
              text = creation.resolution,
              style = MaterialTheme.typography.labelSmall,
              color = Color.White
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Actions Row (Upscale, Download, Copy)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Button(
            onClick = onUpscale,
            colors = ButtonDefaults.buttonColors(containerColor = CretoSurfaceVariant),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.weight(1f)
          ) {
            Icon(imageVector = Icons.Default.Hd, contentDescription = null, tint = CretoSecondary, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Upscale 4K", color = Color.White, style = MaterialTheme.typography.labelMedium)
          }

          Button(
            onClick = onDownload,
            colors = ButtonDefaults.buttonColors(containerColor = CretoPrimary),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
              .weight(1f)
              .testTag("detail_download_button")
          ) {
            Icon(imageVector = Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Download", color = Color.White, style = MaterialTheme.typography.labelMedium)
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Prompt Card
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(CretoSurfaceCard)
            .border(1.dp, CretoBorder, RoundedCornerShape(14.dp))
            .padding(16.dp)
        ) {
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "ORIGINAL PROMPT",
                style = MaterialTheme.typography.labelMedium,
                color = CretoTextMuted,
                letterSpacing = 1.sp
              )
              Spacer(modifier = Modifier.weight(1f))
              IconButton(onClick = { onCopyPrompt(creation.prompt) }, modifier = Modifier.size(24.dp)) {
                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy Prompt", tint = CretoSecondary, modifier = Modifier.size(16.dp))
              }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = creation.prompt,
              style = MaterialTheme.typography.bodyLarge,
              color = Color.White
            )

            if (!creation.enhancedPrompt.isNullOrBlank()) {
              Spacer(modifier = Modifier.height(14.dp))
              Text(
                text = "GEMINI ENHANCED PROMPT",
                style = MaterialTheme.typography.labelMedium,
                color = CretoPrimaryGlow,
                letterSpacing = 1.sp
              )
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = creation.enhancedPrompt,
                style = MaterialTheme.typography.bodyMedium,
                color = CretoTextSecondary,
                fontSize = 12.sp,
                lineHeight = 16.sp
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Metadata grid
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          MetaInfoPill(label = "Aspect Ratio", value = creation.aspectRatio.label, modifier = Modifier.weight(1f))
          MetaInfoPill(label = "Style", value = creation.style, modifier = Modifier.weight(1f))
          if (creation.durationSeconds != null) {
            MetaInfoPill(label = "Duration", value = "${creation.durationSeconds}s", modifier = Modifier.weight(1f))
          }
        }
      }
    }
  }
}

@Composable
private fun MetaInfoPill(label: String, value: String, modifier: Modifier = Modifier) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(10.dp))
      .background(CretoSurfaceVariant)
      .border(1.dp, CretoBorder, RoundedCornerShape(10.dp))
      .padding(10.dp)
  ) {
    Column {
      Text(text = label, style = MaterialTheme.typography.labelSmall, color = CretoTextMuted, fontSize = 10.sp)
      Spacer(modifier = Modifier.height(2.dp))
      Text(text = value, style = MaterialTheme.typography.titleMedium, color = Color.White, fontSize = 13.sp, maxLines = 1)
    }
  }
}
