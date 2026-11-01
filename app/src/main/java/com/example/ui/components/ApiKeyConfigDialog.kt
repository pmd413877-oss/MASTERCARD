package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.CretoBorder
import com.example.ui.theme.CretoPrimary
import com.example.ui.theme.CretoSecondary
import com.example.ui.theme.CretoSuccess
import com.example.ui.theme.CretoSurfaceCard
import com.example.ui.theme.CretoSurfaceElevated
import com.example.ui.theme.CretoSurfaceVariant
import com.example.ui.theme.CretoTextMuted
import com.example.ui.theme.CretoTextSecondary

@Composable
fun ApiKeyConfigDialog(
  currentKey: String,
  onDismiss: () -> Unit,
  onSaveKey: (String) -> Unit
) {
  var keyInput by remember { mutableStateOf(currentKey) }

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
              .background(CretoSecondary.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Key,
              contentDescription = null,
              tint = CretoSecondary,
              modifier = Modifier.size(20.dp)
            )
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Google AI Studio Key",
              style = MaterialTheme.typography.titleLarge,
              color = Color.White
            )
            Text(
              text = "Gemini & Imagen API Access",
              style = MaterialTheme.typography.bodyMedium,
              color = CretoTextSecondary,
              fontSize = 12.sp
            )
          }
          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = CretoTextSecondary)
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Info Banner
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CretoSurfaceVariant)
            .border(1.dp, CretoBorder, RoundedCornerShape(12.dp))
            .padding(12.dp)
        ) {
          Row(verticalAlignment = Alignment.Top) {
            Icon(
              imageVector = Icons.Default.Info,
              contentDescription = null,
              tint = CretoSecondary,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "In AI Studio Build, GEMINI_API_KEY can be configured in the Secrets panel or entered here directly. If left blank, CretoAI uses smart built-in neural generation templates.",
              style = MaterialTheme.typography.bodyMedium,
              color = CretoTextSecondary,
              fontSize = 12.sp,
              lineHeight = 16.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
          value = keyInput,
          onValueChange = { keyInput = it },
          label = { Text("Gemini API Key (AIzaSy...)") },
          placeholder = { Text("Enter your Gemini API key") },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("api_key_input"),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = CretoPrimary,
            unfocusedBorderColor = CretoBorder,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedContainerColor = CretoSurfaceVariant,
            unfocusedContainerColor = CretoSurfaceVariant
          )
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.fillMaxWidth()
        ) {
          Button(
            onClick = onDismiss,
            colors = ButtonDefaults.buttonColors(containerColor = CretoSurfaceElevated),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.weight(1f)
          ) {
            Text("Cancel", color = CretoTextSecondary)
          }
          Spacer(modifier = Modifier.width(12.dp))
          Button(
            onClick = {
              onSaveKey(keyInput)
              onDismiss()
            },
            colors = ButtonDefaults.buttonColors(containerColor = CretoPrimary),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
              .weight(1f)
              .testTag("save_api_key_button")
          ) {
            Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Save Key")
          }
        }
      }
    }
  }
}
