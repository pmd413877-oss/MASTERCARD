package com.example.data.repository

import android.content.Context
import android.util.Log
import com.example.BuildConfig
import com.example.R
import com.example.data.model.AspectRatioOption
import com.example.data.model.CameraMove
import com.example.data.model.CreationType
import com.example.data.model.CreativeCreation
import com.example.data.model.ImageStyle
import com.example.data.model.MotionGraphicStyle
import com.example.data.model.MotionIntensity
import com.example.data.model.MotionType
import com.example.data.model.VideoDuration
import com.example.data.network.GeminiApiService
import com.example.data.network.GeminiContent
import com.example.data.network.GeminiGenerationConfig
import com.example.data.network.GeminiPart
import com.example.data.network.GeminiRequest
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class CretoAiRepository(private val context: Context) {

  private val TAG = "CretoAiRepository"

  private val _credits = MutableStateFlow(85)
  val credits: StateFlow<Int> = _credits.asStateFlow()

  private val _userApiKey = MutableStateFlow<String>("")
  val userApiKey: StateFlow<String> = _userApiKey.asStateFlow()

  private val _creations = MutableStateFlow<List<CreativeCreation>>(emptyList())
  val creations: StateFlow<List<CreativeCreation>> = _creations.asStateFlow()

  private val moshi: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

  private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(60, TimeUnit.SECONDS)
    .addInterceptor(HttpLoggingInterceptor().apply {
      level = HttpLoggingInterceptor.Level.BASIC
    })
    .build()

  private val geminiApi: GeminiApiService = Retrofit.Builder()
    .baseUrl("https://generativelanguage.googleapis.com/")
    .client(okHttpClient)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()
    .create(GeminiApiService::class.java)

  init {
    seedInitialCreations()
  }

  fun setCustomApiKey(key: String) {
    _userApiKey.value = key.trim()
  }

  fun getEffectiveApiKey(): String {
    val manual = _userApiKey.value
    if (manual.isNotBlank()) return manual
    val buildKey = try {
      BuildConfig.GEMINI_API_KEY
    } catch (_: Exception) {
      ""
    }
    return if (buildKey.isNotBlank() && buildKey != "MY_GEMINI_API_KEY") buildKey else ""
  }

  fun deductCredits(amount: Int): Boolean {
    val current = _credits.value
    if (current >= amount) {
      _credits.value = current - amount
      return true
    }
    return false
  }

  fun claimDailyBonus(): Int {
    val bonus = 25
    _credits.value += bonus
    return _credits.value
  }

  fun toggleFavorite(id: String) {
    _creations.value = _creations.value.map {
      if (it.id == id) it.copy(isFavorite = !it.isFavorite) else it
    }
  }

  fun addCreation(creation: CreativeCreation) {
    _creations.value = listOf(creation) + _creations.value
  }

  suspend fun enhancePrompt(originalPrompt: String, styleContext: String, medium: String): String {
    val apiKey = getEffectiveApiKey()
    if (apiKey.isNotBlank()) {
      try {
        val systemInstruction = """
          You are CretoAI Prompt Architect. Transform this brief user concept into an elite, highly evocative $medium generation prompt tailored for the style '$styleContext'. 
          Specify precise camera optics, volumetric lighting, rich color palette, material shaders, and micro-details.
          Return ONLY the enhanced prompt in 2-3 concise, powerful sentences without prefixes or markdown.
        """.trimIndent()

        val request = GeminiRequest(
          contents = listOf(
            GeminiContent(
              role = "user",
              parts = listOf(
                GeminiPart(text = "$systemInstruction\n\nConcept: $originalPrompt")
              )
            )
          ),
          generationConfig = GeminiGenerationConfig(temperature = 0.7f, maxOutputTokens = 256)
        )

        val response = geminiApi.generateContent(
          model = "gemini-2.5-flash",
          apiKey = apiKey,
          request = request
        )

        if (response.isSuccessful) {
          val text = response.body()?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
          if (!text.isNullOrBlank()) {
            return text.trim()
          }
        }
      } catch (e: Exception) {
        Log.w(TAG, "Gemini API call failed, using intelligent offline enhancer: ${e.message}")
      }
    }

    // Intelligent local creative prompt expansion fallback
    delay(400)
    return buildString {
      append(originalPrompt.trim())
      append(", rendered with exquisite $styleContext aesthetic.")
      when (medium.lowercase()) {
        "image" -> append(" Volumetric raytraced rim lighting, shallow depth of field with 50mm f/1.2 lens, ultra-crisp microtextures, octane color graded.")
        "video" -> append(" Dynamic 60fps cinematic camera tracking, fluid atmospheric motion blur, sweeping anamorphic flares, hyper-detailed temporal consistency.")
        "motion graphics" -> append(" Crisp kinetic vector velocity, glowing neon bezier curves, fluid ease-in-out typography transitions, pulsing geometric aura.")
      }
    }
  }

  suspend fun generateSceneStoryboard(prompt: String, duration: VideoDuration, camera: CameraMove): List<String> {
    delay(300)
    return listOf(
      "Scene 01 (0.0s - 1.5s): Establishing framing. ${camera.visualCue} with focal subject reveal.",
      "Scene 02 (1.5s - ${duration.seconds / 2}.0s): Dynamic camera acceleration, volumetric particle stream tracking along foreground axis.",
      "Scene 03 (${duration.seconds / 2}.0s - ${duration.seconds}.0s): Climactic visual cadence, depth-of-field transition to hero focus."
    )
  }

  private fun seedInitialCreations() {
    _creations.value = listOf(
      CreativeCreation(
        type = CreationType.IMAGE,
        title = "Neon Dragon District",
        prompt = "Cinematic cyberpunk Tokyo neon alley with holographic dragon",
        enhancedPrompt = "Cyberpunk alley in Tokyo drenched in rain with vibrant bioluminescent dragon hovering between neon signs, reflections on wet asphalt, 8k octane render.",
        style = ImageStyle.CYBERPUNK.displayName,
        aspectRatio = AspectRatioOption.RATIO_16_9,
        drawableRes = R.drawable.showcase_cyberpunk_1793526380177,
        isFavorite = true,
        resolution = "4K UHD"
      ),
      CreativeCreation(
        type = CreationType.IMAGE,
        title = "Astral Crystal Valkyrie",
        prompt = "Ethereal anime fantasy warrior girl standing atop floating island",
        enhancedPrompt = "Ethereal anime warrior maiden on a floating crystal peak under aurora borealis twilight, cel-shaded masterwork with vibrant cosmic particles.",
        style = ImageStyle.ANIME.displayName,
        aspectRatio = AspectRatioOption.RATIO_1_1,
        drawableRes = R.drawable.showcase_anime_1793526395546,
        isFavorite = true,
        resolution = "2K QuadHD"
      ),
      CreativeCreation(
        type = CreationType.VIDEO,
        title = "Neural Android Genesis",
        prompt = "Futuristic holographic android robot head with glass skull",
        enhancedPrompt = "Refractive glass cybernetic android head with illuminated neural circuits and optical servos, slow cinematic 360 orbit camera move.",
        style = ImageStyle.THREE_D_RENDER.displayName,
        aspectRatio = AspectRatioOption.RATIO_9_16,
        durationSeconds = 10,
        motionIntensity = MotionIntensity.DYNAMIC.label,
        drawableRes = R.drawable.showcase_3drender_1793526412191,
        isFavorite = false,
        resolution = "1080p 60fps"
      ),
      CreativeCreation(
        type = CreationType.MOTION_GRAPHICS,
        title = "Cyber Kinetic Typo Sequence",
        prompt = "Kinetic typography intro sequence for CretoAI",
        enhancedPrompt = "Staggered kinetic letters 'CRETO AI' zooming with neon cyan and violet motion blur, particle vortex implosion and glowing geometric frame.",
        style = MotionGraphicStyle.NEON_KINETIC.label,
        aspectRatio = AspectRatioOption.RATIO_16_9,
        durationSeconds = 6,
        motionIntensity = MotionIntensity.HYPER.label,
        isFavorite = true,
        resolution = "Vector 60fps",
        animationScriptJson = """{"version":"1.0","type":"kinetic_typography","fps":60,"duration":6.0,"elements":[{"text":"CRETO AI","anim":"elastic_scale","color":"#8B5CF6"},{"text":"CREATIVE SUITE","anim":"fade_tracking","color":"#06B6D4"}]}"""
      )
    )
  }
}
