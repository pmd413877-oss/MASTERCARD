package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Toll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CretoAccentGold
import com.example.ui.theme.CretoBackground
import com.example.ui.theme.CretoBorder
import com.example.ui.theme.CretoPrimary
import com.example.ui.theme.CretoSecondary
import com.example.ui.theme.CretoSurface
import com.example.ui.theme.CretoSurfaceVariant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CretoTopBar(
  title: String,
  credits: Int,
  onOpenDrawer: () -> Unit,
  onOpenRefill: () -> Unit,
  onOpenApiKey: () -> Unit,
  modifier: Modifier = Modifier
) {
  TopAppBar(
    modifier = modifier,
    colors = TopAppBarDefaults.topAppBarColors(
      containerColor = CretoBackground
    ),
    navigationIcon = {
      IconButton(
        onClick = onOpenDrawer,
        modifier = Modifier.testTag("menu_button")
      ) {
        Icon(
          imageVector = Icons.Default.Menu,
          contentDescription = "Open Navigation Menu",
          tint = Color.White
        )
      }
    },
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        // Glowing app spark icon
        Box(
          modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(
              Brush.linearGradient(listOf(CretoPrimary, CretoSecondary))
            ),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(18.dp)
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
          text = title,
          style = MaterialTheme.typography.titleLarge,
          color = Color.White
        )
      }
    },
    actions = {
      // API Key action button
      IconButton(
        onClick = onOpenApiKey,
        modifier = Modifier.testTag("api_key_button")
      ) {
        Icon(
          imageVector = Icons.Default.Key,
          contentDescription = "Gemini API Key Settings",
          tint = CretoSecondary
        )
      }

      // Credits display chip with refill on click
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .padding(end = 12.dp)
          .clip(RoundedCornerShape(20.dp))
          .background(CretoSurfaceVariant)
          .border(1.dp, CretoBorder, RoundedCornerShape(20.dp))
          .clickable { onOpenRefill() }
          .padding(horizontal = 10.dp, vertical = 6.dp)
          .testTag("credits_pill")
      ) {
        Icon(
          imageVector = Icons.Default.Toll,
          contentDescription = "Credits",
          tint = CretoAccentGold,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "$credits",
          style = MaterialTheme.typography.labelLarge,
          color = CretoAccentGold,
          fontSize = 13.sp
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = "+",
          style = MaterialTheme.typography.labelLarge,
          color = CretoSecondary,
          fontSize = 14.sp
        )
      }
    }
  )
}
