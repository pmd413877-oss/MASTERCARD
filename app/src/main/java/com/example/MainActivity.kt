package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.AiModule
import com.example.ui.CretoAiViewModel
import com.example.ui.components.ApiKeyConfigDialog
import com.example.ui.components.CreationDetailModal
import com.example.ui.components.CreditRefillDialog
import com.example.ui.components.CretoNavigationDrawerContent
import com.example.ui.components.CretoTopBar
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.ImageGeneratorScreen
import com.example.ui.screens.MotionGraphicsScreen
import com.example.ui.screens.VideoGeneratorScreen
import com.example.ui.theme.CretoAiTheme
import com.example.ui.theme.CretoBackground
import com.example.ui.theme.CretoBorder
import com.example.ui.theme.CretoPrimary
import com.example.ui.theme.CretoPrimaryGlow
import com.example.ui.theme.CretoSurfaceCard
import com.example.ui.theme.CretoTextMuted
import com.example.ui.theme.CretoTextSecondary
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

  private val viewModel: CretoAiViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      CretoAiTheme {
        CretoAiApp(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun CretoAiApp(viewModel: CretoAiViewModel) {
  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
  val scope = rememberCoroutineScope()
  val snackbarHostState = remember { SnackbarHostState() }

  // State observations
  val currentModule by viewModel.currentModule.collectAsStateWithLifecycle()
  val credits by viewModel.credits.collectAsStateWithLifecycle()
  val creations by viewModel.creations.collectAsStateWithLifecycle()
  val activeFilter by viewModel.activeCreationFilter.collectAsStateWithLifecycle()
  val selectedCreation by viewModel.selectedCreation.collectAsStateWithLifecycle()

  // Generator states
  val imagePrompt by viewModel.imagePrompt.collectAsStateWithLifecycle()
  val imageStyle by viewModel.selectedImageStyle.collectAsStateWithLifecycle()
  val imageRatio by viewModel.selectedAspectRatio.collectAsStateWithLifecycle()
  val isEnhancingImage by viewModel.isEnhancingImagePrompt.collectAsStateWithLifecycle()
  val imageGenState by viewModel.imageGenState.collectAsStateWithLifecycle()

  val videoPrompt by viewModel.videoPrompt.collectAsStateWithLifecycle()
  val videoDuration by viewModel.selectedDuration.collectAsStateWithLifecycle()
  val motionIntensity by viewModel.selectedMotionIntensity.collectAsStateWithLifecycle()
  val cameraMove by viewModel.selectedCameraMove.collectAsStateWithLifecycle()
  val isEnhancingVideo by viewModel.isEnhancingVideoPrompt.collectAsStateWithLifecycle()
  val videoGenState by viewModel.videoGenState.collectAsStateWithLifecycle()

  val motionPrompt by viewModel.motionPrompt.collectAsStateWithLifecycle()
  val motionStyle by viewModel.selectedMotionStyle.collectAsStateWithLifecycle()
  val motionType by viewModel.selectedMotionType.collectAsStateWithLifecycle()
  val motionGenState by viewModel.motionGenState.collectAsStateWithLifecycle()

  // Dialogs
  val showRefillDialog by viewModel.showRefillDialog.collectAsStateWithLifecycle()
  val showApiKeyDialog by viewModel.showApiKeyDialog.collectAsStateWithLifecycle()
  val userApiKey by viewModel.userApiKey.collectAsStateWithLifecycle()
  val toastMessage by viewModel.toastMessage.collectAsStateWithLifecycle()

  // Handle Toast/Snackbar triggers
  LaunchedEffect(toastMessage) {
    toastMessage?.let {
      snackbarHostState.showSnackbar(it)
      viewModel.dismissToast()
    }
  }

  // Predictive Back Handler
  BackHandler(enabled = drawerState.isOpen || selectedCreation != null || currentModule != AiModule.DASHBOARD) {
    if (drawerState.isOpen) {
      scope.launch { drawerState.close() }
    } else if (selectedCreation != null) {
      viewModel.selectCreation(null)
    } else if (currentModule != AiModule.DASHBOARD) {
      viewModel.navigateTo(AiModule.DASHBOARD)
    }
  }

  ModalNavigationDrawer(
    drawerState = drawerState,
    drawerContent = {
      CretoNavigationDrawerContent(
        currentModule = currentModule,
        credits = credits,
        onModuleSelected = { module ->
          viewModel.navigateTo(module)
          scope.launch { drawerState.close() }
        },
        onOpenRefill = {
          scope.launch { drawerState.close() }
          viewModel.openRefillDialog(true)
        },
        onOpenApiKey = {
          scope.launch { drawerState.close() }
          viewModel.openApiKeyDialog(true)
        }
      )
    }
  ) {
    Scaffold(
      modifier = Modifier
        .fillMaxSize()
        .background(CretoBackground),
      containerColor = CretoBackground,
      topBar = {
        CretoTopBar(
          title = currentModule.title,
          credits = credits,
          onOpenDrawer = { scope.launch { drawerState.open() } },
          onOpenRefill = { viewModel.openRefillDialog(true) },
          onOpenApiKey = { viewModel.openApiKeyDialog(true) }
        )
      },
      bottomBar = {
        NavigationBar(
          containerColor = CretoSurfaceCard,
          modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
        ) {
          AiModule.entries.forEach { module ->
            val selected = currentModule == module
            NavigationBarItem(
              selected = selected,
              onClick = { viewModel.navigateTo(module) },
              icon = {
                Icon(
                  imageVector = module.icon,
                  contentDescription = module.title,
                  tint = if (selected) CretoPrimaryGlow else CretoTextMuted
                )
              },
              label = {
                Text(
                  text = when (module) {
                    AiModule.DASHBOARD -> "Home"
                    AiModule.IMAGE_GEN -> "Images"
                    AiModule.VIDEO_GEN -> "Videos"
                    AiModule.MOTION_GEN -> "Motion"
                  },
                  fontSize = 11.sp,
                  color = if (selected) Color.White else CretoTextMuted
                )
              },
              colors = NavigationBarItemDefaults.colors(
                indicatorColor = CretoPrimary.copy(alpha = 0.25f)
              ),
              modifier = Modifier.testTag("bottom_nav_${module.name.lowercase()}")
            )
          }
        }
      },
      snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
      ) {
        when (currentModule) {
          AiModule.DASHBOARD -> DashboardScreen(
            credits = credits,
            creations = creations,
            selectedFilter = activeFilter,
            onNavigateToModule = { viewModel.navigateTo(it) },
            onFilterSelected = { viewModel.setCreationFilter(it) },
            onSelectCreation = { viewModel.selectCreation(it) },
            onOpenRefill = { viewModel.openRefillDialog(true) },
            onQuickPromptSelected = { module, quickPrompt ->
              when (module) {
                AiModule.IMAGE_GEN -> viewModel.setImagePrompt(quickPrompt)
                AiModule.VIDEO_GEN -> viewModel.setVideoPrompt(quickPrompt)
                AiModule.MOTION_GEN -> viewModel.setMotionPrompt(quickPrompt)
                else -> Unit
              }
              viewModel.navigateTo(module)
            }
          )

          AiModule.IMAGE_GEN -> ImageGeneratorScreen(
            prompt = imagePrompt,
            selectedStyle = imageStyle,
            selectedRatio = imageRatio,
            isEnhancing = isEnhancingImage,
            generationState = imageGenState,
            onPromptChanged = { viewModel.setImagePrompt(it) },
            onStyleSelected = { viewModel.selectImageStyle(it) },
            onRatioSelected = { viewModel.selectAspectRatio(it) },
            onEnhancePrompt = { viewModel.enhanceImagePrompt() },
            onGenerate = { viewModel.generateImage() },
            onOpenCreationDetail = { viewModel.selectCreation(it) },
            onDownloadImage = { viewModel.showToast("Image saved to device gallery!") },
            onUpscaleImage = { viewModel.showToast("4K Ultra-Res Upscaling complete!") }
          )

          AiModule.VIDEO_GEN -> VideoGeneratorScreen(
            prompt = videoPrompt,
            selectedDuration = videoDuration,
            selectedMotionIntensity = motionIntensity,
            selectedCameraMove = cameraMove,
            isEnhancing = isEnhancingVideo,
            generationState = videoGenState,
            onPromptChanged = { viewModel.setVideoPrompt(it) },
            onDurationSelected = { viewModel.selectVideoDuration(it) },
            onMotionIntensitySelected = { viewModel.selectMotionIntensity(it) },
            onCameraMoveSelected = { viewModel.selectCameraMove(it) },
            onEnhancePrompt = { viewModel.enhanceVideoPrompt() },
            onGenerateVideo = { viewModel.generateVideo() },
            onDownloadVideo = { viewModel.showToast("MP4 video downloaded to device!") }
          )

          AiModule.MOTION_GEN -> MotionGraphicsScreen(
            prompt = motionPrompt,
            selectedStyle = motionStyle,
            selectedType = motionType,
            generationState = motionGenState,
            onPromptChanged = { viewModel.setMotionPrompt(it) },
            onStyleSelected = { viewModel.selectMotionStyle(it) },
            onTypeSelected = { viewModel.selectMotionType(it) },
            onGenerateMotion = { viewModel.generateMotionGraphics() },
            onExportLottie = { viewModel.showToast("Lottie JSON animation exported to clipboard!") },
            onExportMp4 = { viewModel.showToast("Motion Graphic MP4 exported successfully!") }
          )
        }
      }
    }
  }

  // Modals & Dialogs
  if (showRefillDialog) {
    CreditRefillDialog(
      currentCredits = credits,
      onDismiss = { viewModel.openRefillDialog(false) },
      onClaimDaily = { viewModel.claimBonusCredits() }
    )
  }

  if (showApiKeyDialog) {
    ApiKeyConfigDialog(
      currentKey = userApiKey,
      onDismiss = { viewModel.openApiKeyDialog(false) },
      onSaveKey = { viewModel.setCustomApiKey(it) }
    )
  }

  selectedCreation?.let { creation ->
    CreationDetailModal(
      creation = creation,
      onDismiss = { viewModel.selectCreation(null) },
      onToggleFavorite = { viewModel.toggleFavorite(it) },
      onDownload = { viewModel.showToast("Saved to device!") },
      onUpscale = { viewModel.showToast("Asset upscaled to 4K!") },
      onCopyPrompt = { viewModel.showToast("Prompt copied to clipboard!") }
    )
  }
}
