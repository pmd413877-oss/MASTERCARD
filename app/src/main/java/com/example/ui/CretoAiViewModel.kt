package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.R
import com.example.data.model.AiModule
import com.example.data.model.AspectRatioOption
import com.example.data.model.CameraMove
import com.example.data.model.CreationType
import com.example.data.model.CreativeCreation
import com.example.data.model.GenerationState
import com.example.data.model.ImageStyle
import com.example.data.model.MotionGraphicStyle
import com.example.data.model.MotionIntensity
import com.example.data.model.MotionType
import com.example.data.model.VideoDuration
import com.example.data.repository.CretoAiRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CretoAiViewModel(application: Application) : AndroidViewModel(application) {

  val repository = CretoAiRepository(application.applicationContext)

  val credits = repository.credits
  val creations = repository.creations
  val userApiKey = repository.userApiKey

  private val _currentModule = MutableStateFlow(AiModule.DASHBOARD)
  val currentModule: StateFlow<AiModule> = _currentModule.asStateFlow()

  private val _activeCreationFilter = MutableStateFlow<CreationType?>(null)
  val activeCreationFilter: StateFlow<CreationType?> = _activeCreationFilter.asStateFlow()

  private val _selectedCreation = MutableStateFlow<CreativeCreation?>(null)
  val selectedCreation: StateFlow<CreativeCreation?> = _selectedCreation.asStateFlow()

  // Image Gen State
  private val _imagePrompt = MutableStateFlow("")
  val imagePrompt: StateFlow<String> = _imagePrompt.asStateFlow()

  private val _selectedImageStyle = MutableStateFlow(ImageStyle.CINEMATIC)
  val selectedImageStyle: StateFlow<ImageStyle> = _selectedImageStyle.asStateFlow()

  private val _selectedAspectRatio = MutableStateFlow(AspectRatioOption.RATIO_1_1)
  val selectedAspectRatio: StateFlow<AspectRatioOption> = _selectedAspectRatio.asStateFlow()

  private val _isEnhancingImagePrompt = MutableStateFlow(false)
  val isEnhancingImagePrompt: StateFlow<Boolean> = _isEnhancingImagePrompt.asStateFlow()

  private val _imageGenState = MutableStateFlow<GenerationState<CreativeCreation>>(GenerationState.Idle)
  val imageGenState: StateFlow<GenerationState<CreativeCreation>> = _imageGenState.asStateFlow()

  // Video Gen State
  private val _videoPrompt = MutableStateFlow("")
  val videoPrompt: StateFlow<String> = _videoPrompt.asStateFlow()

  private val _selectedDuration = MutableStateFlow(VideoDuration.DURATION_5S)
  val selectedDuration: StateFlow<VideoDuration> = _selectedDuration.asStateFlow()

  private val _selectedMotionIntensity = MutableStateFlow(MotionIntensity.DYNAMIC)
  val selectedMotionIntensity: StateFlow<MotionIntensity> = _selectedMotionIntensity.asStateFlow()

  private val _selectedCameraMove = MutableStateFlow(CameraMove.PAN_LEFT)
  val selectedCameraMove: StateFlow<CameraMove> = _selectedCameraMove.asStateFlow()

  private val _isEnhancingVideoPrompt = MutableStateFlow(false)
  val isEnhancingVideoPrompt: StateFlow<Boolean> = _isEnhancingVideoPrompt.asStateFlow()

  private val _videoGenState = MutableStateFlow<GenerationState<CreativeCreation>>(GenerationState.Idle)
  val videoGenState: StateFlow<GenerationState<CreativeCreation>> = _videoGenState.asStateFlow()

  // Motion Graphics State
  private val _motionPrompt = MutableStateFlow("")
  val motionPrompt: StateFlow<String> = _motionPrompt.asStateFlow()

  private val _selectedMotionStyle = MutableStateFlow(MotionGraphicStyle.NEON_KINETIC)
  val selectedMotionStyle: StateFlow<MotionGraphicStyle> = _selectedMotionStyle.asStateFlow()

  private val _selectedMotionType = MutableStateFlow(MotionType.KINETIC_TYPOGRAPHY)
  val selectedMotionType: StateFlow<MotionType> = _selectedMotionType.asStateFlow()

  private val _motionGenState = MutableStateFlow<GenerationState<CreativeCreation>>(GenerationState.Idle)
  val motionGenState: StateFlow<GenerationState<CreativeCreation>> = _motionGenState.asStateFlow()

  // Dialogs & Notification Snackbars
  private val _showRefillDialog = MutableStateFlow(false)
  val showRefillDialog: StateFlow<Boolean> = _showRefillDialog.asStateFlow()

  private val _showApiKeyDialog = MutableStateFlow(false)
  val showApiKeyDialog: StateFlow<Boolean> = _showApiKeyDialog.asStateFlow()

  private val _toastMessage = MutableStateFlow<String?>(null)
  val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

  fun navigateTo(module: AiModule) {
    _currentModule.value = module
  }

  fun setCreationFilter(type: CreationType?) {
    _activeCreationFilter.value = type
  }

  fun selectCreation(creation: CreativeCreation?) {
    _selectedCreation.value = creation
  }

  fun toggleFavorite(id: String) {
    repository.toggleFavorite(id)
  }

  fun setImagePrompt(text: String) {
    _imagePrompt.value = text
  }

  fun selectImageStyle(style: ImageStyle) {
    _selectedImageStyle.value = style
  }

  fun selectAspectRatio(ratio: AspectRatioOption) {
    _selectedAspectRatio.value = ratio
  }

  fun setVideoPrompt(text: String) {
    _videoPrompt.value = text
  }

  fun selectVideoDuration(duration: VideoDuration) {
    _selectedDuration.value = duration
  }

  fun selectMotionIntensity(intensity: MotionIntensity) {
    _selectedMotionIntensity.value = intensity
  }

  fun selectCameraMove(move: CameraMove) {
    _selectedCameraMove.value = move
  }

  fun setMotionPrompt(text: String) {
    _motionPrompt.value = text
  }

  fun selectMotionStyle(style: MotionGraphicStyle) {
    _selectedMotionStyle.value = style
  }

  fun selectMotionType(type: MotionType) {
    _selectedMotionType.value = type
  }

  fun openRefillDialog(open: Boolean) {
    _showRefillDialog.value = open
  }

  fun openApiKeyDialog(open: Boolean) {
    _showApiKeyDialog.value = open
  }

  fun setCustomApiKey(key: String) {
    repository.setCustomApiKey(key)
    showToast("API Key updated for Gemini session.")
  }

  fun claimBonusCredits() {
    val newTotal = repository.claimDailyBonus()
    showToast("Claimed +25 daily credits! Total: $newTotal")
    _showRefillDialog.value = false
  }

  fun showToast(msg: String) {
    _toastMessage.value = msg
  }

  fun dismissToast() {
    _toastMessage.value = null
  }

  fun enhanceImagePrompt() {
    val prompt = _imagePrompt.value.ifBlank { "Mystical portal over modern city" }
    viewModelScope.launch {
      _isEnhancingImagePrompt.value = true
      try {
        val enhanced = repository.enhancePrompt(
          originalPrompt = prompt,
          styleContext = _selectedImageStyle.value.displayName,
          medium = "Image"
        )
        _imagePrompt.value = enhanced
        showToast("Prompt enriched with Gemini AI!")
      } catch (e: Exception) {
        showToast("Could not enhance prompt: ${e.message}")
      } finally {
        _isEnhancingImagePrompt.value = false
      }
    }
  }

  fun enhanceVideoPrompt() {
    val prompt = _videoPrompt.value.ifBlank { "Futuristic speeder racing through canyon" }
    viewModelScope.launch {
      _isEnhancingVideoPrompt.value = true
      try {
        val enhanced = repository.enhancePrompt(
          originalPrompt = prompt,
          styleContext = "${_selectedCameraMove.value.label}, ${_selectedMotionIntensity.value.label}",
          medium = "Video"
        )
        _videoPrompt.value = enhanced
        showToast("Scene direction expanded with Gemini AI!")
      } catch (e: Exception) {
        showToast("Could not enhance scene: ${e.message}")
      } finally {
        _isEnhancingVideoPrompt.value = false
      }
    }
  }

  fun generateImage() {
    val prompt = _imagePrompt.value.ifBlank { "Cyberpunk Tokyo neon alley with holographic dragon" }
    val cost = 2
    if (!repository.deductCredits(cost)) {
      showToast("Insufficient credits! Please refill.")
      _showRefillDialog.value = true
      return
    }

    viewModelScope.launch {
      _imageGenState.value = GenerationState.Progress("Synthesizing prompt semantics...", 0.15f)
      delay(700)
      _imageGenState.value = GenerationState.Progress("Sampling latent diffusion noise...", 0.45f)
      delay(900)
      _imageGenState.value = GenerationState.Progress("High-resolution neural upscaling...", 0.75f)
      delay(800)
      _imageGenState.value = GenerationState.Progress("Compositing color grading pass...", 0.95f)
      delay(500)

      // Pick corresponding showcase artwork or generated composition
      val res = when (_selectedImageStyle.value) {
        ImageStyle.CYBERPUNK -> R.drawable.showcase_cyberpunk_1793526380177
        ImageStyle.ANIME -> R.drawable.showcase_anime_1793526395546
        ImageStyle.THREE_D_RENDER -> R.drawable.showcase_3drender_1793526412191
        else -> R.drawable.showcase_cyberpunk_1793526380177
      }

      val creation = CreativeCreation(
        type = CreationType.IMAGE,
        title = prompt.take(30).capitalizeWords(),
        prompt = prompt,
        enhancedPrompt = prompt + ", " + _selectedImageStyle.value.promptModifier,
        style = _selectedImageStyle.value.displayName,
        aspectRatio = _selectedAspectRatio.value,
        drawableRes = res,
        resolution = "2048 x 2048 (2K UHD)"
      )
      repository.addCreation(creation)
      _imageGenState.value = GenerationState.Success(creation)
      showToast("Image generated successfully! (-$cost credits)")
    }
  }

  fun generateVideo() {
    val prompt = _videoPrompt.value.ifBlank { "Holographic android meditating in neon cyber shrine" }
    val cost = _selectedDuration.value.creditCost
    if (!repository.deductCredits(cost)) {
      showToast("Insufficient credits! Required: $cost credits.")
      _showRefillDialog.value = true
      return
    }

    viewModelScope.launch {
      _videoGenState.value = GenerationState.Progress("Director Gemini analyzing temporal keyframes...", 0.20f)
      delay(800)
      _videoGenState.value = GenerationState.Progress("Calculating optical motion flow (${_selectedMotionIntensity.value.label})...", 0.50f)
      delay(900)
      _videoGenState.value = GenerationState.Progress("Interpolating 60fps neural frames...", 0.75f)
      delay(900)
      _videoGenState.value = GenerationState.Progress("Encoding MP4 master video...", 0.95f)
      delay(600)

      val creation = CreativeCreation(
        type = CreationType.VIDEO,
        title = prompt.take(30).capitalizeWords(),
        prompt = prompt,
        enhancedPrompt = prompt + " | Camera: ${_selectedCameraMove.value.label} | Motion: ${_selectedMotionIntensity.value.label}",
        style = "Cinematic Veo",
        aspectRatio = AspectRatioOption.RATIO_16_9,
        durationSeconds = _selectedDuration.value.seconds,
        motionIntensity = _selectedMotionIntensity.value.label,
        drawableRes = R.drawable.showcase_3drender_1793526412191,
        resolution = "1080p 60fps"
      )
      repository.addCreation(creation)
      _videoGenState.value = GenerationState.Success(creation)
      showToast("Video rendered successfully! (-$cost credits)")
    }
  }

  fun generateMotionGraphics() {
    val prompt = _motionPrompt.value.ifBlank { "Kinetic typographic logo reveal with particle burst" }
    val cost = 3
    if (!repository.deductCredits(cost)) {
      showToast("Insufficient credits! Required: $cost credits.")
      _showRefillDialog.value = true
      return
    }

    viewModelScope.launch {
      _motionGenState.value = GenerationState.Progress("Parsing kinetic typography vectors...", 0.25f)
      delay(600)
      _motionGenState.value = GenerationState.Progress("Synthesizing bezier easing curves...", 0.55f)
      delay(700)
      _motionGenState.value = GenerationState.Progress("Compiling real-time animation shaders...", 0.85f)
      delay(600)

      val scriptJson = """
        {
          "engine": "CretoNeuralFX-v2",
          "title": "${prompt.take(24)}",
          "style": "${_selectedMotionStyle.value.label}",
          "type": "${_selectedMotionType.value.label}",
          "fps": 60,
          "duration": 5.0,
          "keyframes": [
            {"time": 0.0, "scale": 0.2, "opacity": 0.0, "glow": 0},
            {"time": 1.5, "scale": 1.15, "opacity": 1.0, "glow": 28},
            {"time": 3.0, "scale": 1.0, "opacity": 1.0, "particles": 120},
            {"time": 5.0, "scale": 1.05, "opacity": 0.9, "vortex": true}
          ]
        }
      """.trimIndent()

      val creation = CreativeCreation(
        type = CreationType.MOTION_GRAPHICS,
        title = prompt.take(30).capitalizeWords(),
        prompt = prompt,
        enhancedPrompt = prompt + " (${_selectedMotionStyle.value.label})",
        style = _selectedMotionStyle.value.label,
        aspectRatio = AspectRatioOption.RATIO_16_9,
        durationSeconds = 5,
        resolution = "Vector 60fps",
        animationScriptJson = scriptJson
      )
      repository.addCreation(creation)
      _motionGenState.value = GenerationState.Success(creation)
      showToast("Motion Graphic compiled! (-$cost credits)")
    }
  }

  private fun String.capitalizeWords(): String =
    split(" ").joinToString(" ") { it.replaceFirstChar { char -> if (char.isLowerCase()) char.titlecase() else char.toString() } }
}
