package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Toll
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.CretoAccentGold
import com.example.ui.theme.CretoBorder
import com.example.ui.theme.CretoPrimary
import com.example.ui.theme.CretoSecondary
import com.example.ui.theme.CretoSurfaceCard
import com.example.ui.theme.CretoSurfaceElevated
import com.example.ui.theme.CretoSurfaceVariant
import com.example.ui.theme.CretoTextMuted
import com.example.ui.theme.CretoTextSecondary

@Composable
fun CreditRefillDialog(
  currentCredits: Int,
  onDismiss: () -> Unit,
  onClaimDaily: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = CretoSurfaceCard,
      modifier = Modifier
        .fillMaxWidth()
        .border(1.dp, CretoBorder, RoundedCornerShape(20.dp))
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        // Header
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.fillMaxWidth()
        ) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(CretoAccentGold.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Toll,
              contentDescription = null,
              tint = CretoAccentGold,
              modifier = Modifier.size(20.dp)
            )
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Studio Credits",
              style = MaterialTheme.typography.titleLarge,
              color = Color.White
            )
            Text(
              text = "Balance: $currentCredits Credits",
              style = MaterialTheme.typography.bodyMedium,
              color = CretoAccentGold
            )
          }
          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = CretoTextSecondary)
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Daily Bonus Claim Box
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(
              Brush.linearGradient(
                listOf(CretoPrimary.copy(alpha = 0.25f), CretoSecondary.copy(alpha = 0.15f))
              )
            )
            .border(1.dp, CretoPrimary.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
            .padding(14.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.CardGiftcard,
              contentDescription = null,
              tint = CretoAccentGold,
              modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Daily Studio Bonus",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
              )
              Text(
                text = "+25 Free Credits Every 24h",
                style = MaterialTheme.typography.bodyMedium,
                color = CretoTextSecondary,
                fontSize = 12.sp
              )
            }
            Button(
              onClick = onClaimDaily,
              colors = ButtonDefaults.buttonColors(containerColor = CretoAccentGold),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.testTag("claim_daily_button")
            ) {
              Text("Claim", color = Color.Black, style = MaterialTheme.typography.labelLarge)
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
          text = "CREDIT PACKS",
          style = MaterialTheme.typography.labelMedium,
          color = CretoTextMuted,
          letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        CreditTierItem(title = "Creator Pass", credits = "+100 Credits", desc = "50 Images / 12 Videos", tag = "POPULAR")
        Spacer(modifier = Modifier.height(8.dp))
        CreditTierItem(title = "Pro Studio Pass", credits = "+500 Credits", desc = "Unlimited Fast Generative Priority", tag = "UNLIMITED")

        Spacer(modifier = Modifier.height(16.dp))

        Button(
          onClick = onDismiss,
          colors = ButtonDefaults.buttonColors(containerColor = CretoSurfaceElevated),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text("Done", color = Color.White)
        }
      }
    }
  }
}

@Composable
private fun CreditTierItem(
  title: String,
  credits: String,
  desc: String,
  tag: String
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .background(CretoSurfaceVariant)
      .border(1.dp, CretoBorder, RoundedCornerShape(12.dp))
      .padding(12.dp)
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(text = title, style = MaterialTheme.typography.titleMedium, color = Color.White)
          Spacer(modifier = Modifier.width(8.dp))
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(4.dp))
              .background(CretoSecondary.copy(alpha = 0.2f))
              .padding(horizontal = 6.dp, vertical = 2.dp)
          ) {
            Text(text = tag, color = CretoSecondary, style = MaterialTheme.typography.labelSmall, fontSize = 9.sp)
          }
        }
        Text(text = desc, style = MaterialTheme.typography.bodyMedium, color = CretoTextSecondary, fontSize = 11.sp)
      }
      Text(text = credits, style = MaterialTheme.typography.labelLarge, color = CretoAccentGold)
    }
  }
}
