package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Toll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AiModule
import com.example.data.model.CreationType
import com.example.data.model.CreativeCreation
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DashboardScreen(
  credits: Int,
  creations: List<CreativeCreation>,
  selectedFilter: CreationType?,
  onNavigateToModule: (AiModule) -> Unit,
  onFilterSelected: (CreationType?) -> Unit,
  onSelectCreation: (CreativeCreation) -> Unit,
  onOpenRefill: () -> Unit,
  onQuickPromptSelected: (AiModule, String) -> Unit,
  modifier: Modifier = Modifier
) {
  val filteredCreations = if (selectedFilter == null) {
    creations
  } else {
    creations.filter { it.type == selectedFilter }
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(18.dp)
  ) {
    // Welcome Hero Studio Banner
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp))
          .background(
            Brush.linearGradient(
              listOf(
                CretoPrimary.copy(alpha = 0.35f),
                CretoSurfaceElevated,
                CretoSecondary.copy(alpha = 0.2f)
              )
            )
          )
          .border(1.dp, CretoPrimary.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
          .padding(20.dp)
      ) {
        Column {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
          ) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(CretoSecondary.copy(alpha = 0.2f))
                .border(1.dp, CretoSecondary.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text(
                text = "GEMINI 3.8 + IMAGEN 3",
                style = MaterialTheme.typography.labelSmall,
                color = CretoSecondary,
                fontSize = 10.sp
              )
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier
                .clickable { onOpenRefill() }
                .clip(RoundedCornerShape(12.dp))
                .background(CretoAccentGold.copy(alpha = 0.2f))
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Icon(imageVector = Icons.Default.Toll, contentDescription = null, tint = CretoAccentGold, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text(text = "$credits credits", color = CretoAccentGold, style = MaterialTheme.typography.labelSmall)
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          Text(
            text = "Welcome to CretoAI",
            style = MaterialTheme.typography.displayMedium,
            color = Color.White
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Your next-gen multi-modal AI Creative Suite for ultra-high-resolution images, cinematic videos, and kinetic vector animations.",
            style = MaterialTheme.typography.bodyLarge,
            color = CretoTextSecondary,
            fontSize = 13.sp,
            lineHeight = 18.sp
          )
        }
      }
    }

    // Quick Action Cards (Image, Video, Motion)
    item {
      Text(
        text = "CREATIVE ENGINES",
        style = MaterialTheme.typography.labelMedium,
        color = CretoTextMuted,
        letterSpacing = 1.2.sp
      )
      Spacer(modifier = Modifier.height(8.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        QuickActionModuleCard(
          title = "Image Gen",
          subtitle = "Photoreal & Anime",
          icon = Icons.Default.Image,
          accentColor = CretoPrimary,
          onClick = { onNavigateToModule(AiModule.IMAGE_GEN) },
          modifier = Modifier.weight(1f).testTag("action_image_gen")
        )
        QuickActionModuleCard(
          title = "Video Gen",
          subtitle = "Veo 2 Cinematic",
          icon = Icons.Default.Movie,
          accentColor = CretoSecondary,
          onClick = { onNavigateToModule(AiModule.VIDEO_GEN) },
          modifier = Modifier.weight(1f).testTag("action_video_gen")
        )
        QuickActionModuleCard(
          title = "Motion FX",
          subtitle = "Kinetic Vector",
          icon = Icons.Default.Animation,
          accentColor = CretoAccent,
          onClick = { onNavigateToModule(AiModule.MOTION_GEN) },
          modifier = Modifier.weight(1f).testTag("action_motion_gen")
        )
      }
    }

    // Quick Inspiration Prompt Chips
    item {
      Text(
        text = "QUICK INSPIRATION",
        style = MaterialTheme.typography.labelMedium,
        color = CretoTextMuted,
        letterSpacing = 1.2.sp
      )
      Spacer(modifier = Modifier.height(6.dp))
      FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        InspirationChip(
          label = "Cyberpunk rain city",
          icon = Icons.Default.Image,
          onClick = { onQuickPromptSelected(AiModule.IMAGE_GEN, "Cyberpunk Tokyo neon alley with rain reflections") }
        )
        InspirationChip(
          label = "Anime floating island",
          icon = Icons.Default.Image,
          onClick = { onQuickPromptSelected(AiModule.IMAGE_GEN, "Ethereal anime warrior atop floating crystal sanctuary") }
        )
        InspirationChip(
          label = "Drone canyon flight",
          icon = Icons.Default.Movie,
          onClick = { onQuickPromptSelected(AiModule.VIDEO_GEN, "FPV drone flying through glowing futuristic canyon at sunset") }
        )
        InspirationChip(
          label = "Kinetic typographic reveal",
          icon = Icons.Default.Animation,
          onClick = { onQuickPromptSelected(AiModule.MOTION_GEN, "CRETO AI bold kinetic typographic reveal with glowing particle shockwave") }
        )
      }
    }

    // Recent Creations Section
    item {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
      ) {
        Column {
          Text(
            text = "RECENT CREATIONS",
            style = MaterialTheme.typography.labelMedium,
            color = CretoTextMuted,
            letterSpacing = 1.2.sp
          )
          Text(
            text = "${creations.size} projects generated",
            style = MaterialTheme.typography.bodyMedium,
            color = CretoTextSecondary,
            fontSize = 12.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Filter Chips
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        item {
          FilterChip(
            selected = selectedFilter == null,
            onClick = { onFilterSelected(null) },
            label = { Text("All (${creations.size})") },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = CretoPrimary,
              selectedLabelColor = Color.White,
              containerColor = CretoSurfaceVariant,
              labelColor = CretoTextSecondary
            )
          )
        }
        item {
          FilterChip(
            selected = selectedFilter == CreationType.IMAGE,
            onClick = { onFilterSelected(CreationType.IMAGE) },
            label = { Text("Images") },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = CretoPrimary,
              selectedLabelColor = Color.White,
              containerColor = CretoSurfaceVariant,
              labelColor = CretoTextSecondary
            )
          )
        }
        item {
          FilterChip(
            selected = selectedFilter == CreationType.VIDEO,
            onClick = { onFilterSelected(CreationType.VIDEO) },
            label = { Text("Videos") },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = CretoPrimary,
              selectedLabelColor = Color.White,
              containerColor = CretoSurfaceVariant,
              labelColor = CretoTextSecondary
            )
          )
        }
        item {
          FilterChip(
            selected = selectedFilter == CreationType.MOTION_GRAPHICS,
            onClick = { onFilterSelected(CreationType.MOTION_GRAPHICS) },
            label = { Text("Motion FX") },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = CretoPrimary,
              selectedLabelColor = Color.White,
              containerColor = CretoSurfaceVariant,
              labelColor = CretoTextSecondary
            )
          )
        }
      }
    }

    // Creations Cards Grid
    items(filteredCreations) { creation ->
      CreationItemCard(
        creation = creation,
        onClick = { onSelectCreation(creation) }
      )
    }

    item {
      Spacer(modifier = Modifier.height(30.dp))
    }
  }
}

@Composable
private fun QuickActionModuleCard(
  title: String,
  subtitle: String,
  icon: ImageVector,
  accentColor: Color,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(14.dp))
      .background(CretoSurfaceCard)
      .border(1.dp, CretoBorder, RoundedCornerShape(14.dp))
      .clickable { onClick() }
      .padding(12.dp)
  ) {
    Column {
      Box(
        modifier = Modifier
          .size(36.dp)
          .clip(CircleShape)
          .background(accentColor.copy(alpha = 0.2f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(imageVector = icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(18.dp))
      }
      Spacer(modifier = Modifier.height(10.dp))
      Text(text = title, style = MaterialTheme.typography.titleMedium, color = Color.White, fontSize = 13.sp)
      Text(text = subtitle, style = MaterialTheme.typography.bodyMedium, color = CretoTextMuted, fontSize = 10.sp, maxLines = 1)
    }
  }
}

@Composable
private fun InspirationChip(
  label: String,
  icon: ImageVector,
  onClick: () -> Unit
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .clip(RoundedCornerShape(20.dp))
      .background(CretoSurfaceVariant)
      .border(1.dp, CretoBorder, RoundedCornerShape(20.dp))
      .clickable { onClick() }
      .padding(horizontal = 10.dp, vertical = 6.dp)
  ) {
    Icon(imageVector = icon, contentDescription = null, tint = CretoSecondary, modifier = Modifier.size(13.dp))
    Spacer(modifier = Modifier.width(6.dp))
    Text(text = label, style = MaterialTheme.typography.labelSmall, color = Color.White, fontSize = 11.sp)
  }
}

@Composable
private fun CreationItemCard(
  creation: CreativeCreation,
  onClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .background(CretoSurfaceCard)
      .border(1.dp, CretoBorder, RoundedCornerShape(16.dp))
      .clickable { onClick() }
      .testTag("creation_card_${creation.id}")
  ) {
    Column {
      if (creation.drawableRes != null) {
        Image(
          painter = painterResource(id = creation.drawableRes),
          contentDescription = creation.title,
          modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
          contentScale = ContentScale.Crop
        )
      } else {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(
              Brush.linearGradient(listOf(CretoSurfaceElevated, CretoSurfaceVariant))
            ),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Animation,
            contentDescription = null,
            tint = CretoAccent,
            modifier = Modifier.size(40.dp)
          )
        }
      }

      Column(modifier = Modifier.padding(14.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(4.dp))
              .background(CretoPrimary.copy(alpha = 0.2f))
              .padding(horizontal = 6.dp, vertical = 2.dp)
          ) {
            Text(
              text = creation.type.label.uppercase(),
              style = MaterialTheme.typography.labelSmall,
              color = CretoPrimaryGlow,
              fontSize = 9.sp
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = creation.style,
            style = MaterialTheme.typography.bodyMedium,
            color = CretoTextSecondary,
            fontSize = 11.sp
          )
          Spacer(modifier = Modifier.weight(1f))
          Text(
            text = creation.resolution,
            style = MaterialTheme.typography.labelSmall,
            color = CretoTextMuted,
            fontSize = 10.sp
          )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = creation.title,
          style = MaterialTheme.typography.titleLarge,
          color = Color.White,
          fontSize = 15.sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = creation.prompt,
          style = MaterialTheme.typography.bodyMedium,
          color = CretoTextMuted,
          fontSize = 12.sp,
          maxLines = 2
        )
      }
    }
  }
}
