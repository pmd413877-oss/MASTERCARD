package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Toll
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AiModule
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
fun CretoNavigationDrawerContent(
  currentModule: AiModule,
  credits: Int,
  onModuleSelected: (AiModule) -> Unit,
  onOpenRefill: () -> Unit,
  onOpenApiKey: () -> Unit,
  modifier: Modifier = Modifier
) {
  ModalDrawerSheet(
    modifier = modifier.width(310.dp),
    drawerContainerColor = CretoBackground
  ) {
    Column(
      modifier = Modifier
        .fillMaxHeight()
        .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
      // Suite Header Banner
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 20.dp, top = 8.dp)
      ) {
        Box(
          modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
              Brush.linearGradient(listOf(CretoPrimary, CretoSecondary))
            ),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
          )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "CretoAI",
              style = MaterialTheme.typography.displayMedium,
              color = Color.White,
              fontSize = 22.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(CretoPrimary.copy(alpha = 0.2f))
                .border(1.dp, CretoPrimary.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = "STUDIO",
                style = MaterialTheme.typography.labelSmall,
                color = CretoPrimaryGlow,
                fontSize = 10.sp
              )
            }
          }
          Text(
            text = "Full Creative AI Suite",
            style = MaterialTheme.typography.bodyMedium,
            color = CretoTextSecondary,
            fontSize = 12.sp
          )
        }
      }

      // User Studio Credits Card
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(14.dp))
          .background(CretoSurfaceCard)
          .border(1.dp, CretoBorder, RoundedCornerShape(14.dp))
          .padding(14.dp)
      ) {
        Column {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
          ) {
            Icon(
              imageVector = Icons.Default.Toll,
              contentDescription = null,
              tint = CretoAccentGold,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Available Balance",
              style = MaterialTheme.typography.bodyMedium,
              color = CretoTextSecondary,
              fontSize = 12.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(CretoAccentGold.copy(alpha = 0.15f))
                .clickable { onOpenRefill() }
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text(
                text = "+ Refill",
                style = MaterialTheme.typography.labelSmall,
                color = CretoAccentGold,
                fontSize = 11.sp
              )
            }
          }
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "$credits Credits",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White,
            fontSize = 20.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))
      Text(
        text = "CREATIVE MODULES",
        style = MaterialTheme.typography.labelMedium,
        color = CretoTextMuted,
        letterSpacing = 1.2.sp,
        modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
      )

      // Navigation Items for the 4 core modules
      AiModule.entries.forEach { module ->
        val selected = currentModule == module
        NavigationDrawerItem(
          icon = {
            Icon(
              imageVector = module.icon,
              contentDescription = null,
              tint = if (selected) CretoPrimaryGlow else CretoTextSecondary
            )
          },
          label = {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.fillMaxWidth()
            ) {
              Text(
                text = module.title,
                style = MaterialTheme.typography.titleMedium,
                color = if (selected) Color.White else CretoTextSecondary
              )
              if (module.badge != null) {
                Spacer(modifier = Modifier.weight(1f))
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                      if (selected) CretoPrimary.copy(alpha = 0.3f) else CretoSurfaceElevated
                    )
                    .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                  Text(
                    text = module.badge,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (selected) CretoSecondary else CretoTextMuted,
                    fontSize = 10.sp
                  )
                }
              }
            }
          },
          selected = selected,
          onClick = { onModuleSelected(module) },
          colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = CretoSurfaceVariant,
            unselectedContainerColor = Color.Transparent
          ),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .padding(vertical = 3.dp)
            .testTag("nav_item_${module.name.lowercase()}")
        )
      }

      Spacer(modifier = Modifier.weight(1f))
      HorizontalDivider(color = CretoBorder, modifier = Modifier.padding(vertical = 12.dp))

      // Gemini API Settings Option
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(10.dp))
          .clickable { onOpenApiKey() }
          .padding(horizontal = 12.dp, vertical = 10.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Key,
          contentDescription = null,
          tint = CretoSecondary,
          modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
          Text(
            text = "Gemini API Keys",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            fontSize = 14.sp
          )
          Text(
            text = "Google AI Studio integration",
            style = MaterialTheme.typography.bodyMedium,
            color = CretoTextMuted,
            fontSize = 11.sp
          )
        }
      }
    }
  }
}
