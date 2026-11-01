package com.example.data.model

enum class ImageStyle(val displayName: String, val promptModifier: String) {
  CINEMATIC("Cinematic", "cinematic lighting, 35mm film photograph, shallow depth of field, anamorphic flare, Kodak Portra 400"),
  PHOTOREALISTIC("Photorealistic", "hyperrealistic 8k photograph, Hasselblad camera, natural studio lighting, ultra-sharp detail"),
  ANIME("Anime", "Makoto Shinkai and Studio Ghibli anime aesthetic, vibrant atmospheric lighting, cel shaded, masterwork illustration"),
  THREE_D_RENDER("3D Render", "octane render 3D, raytraced reflections, Unreal Engine 5, smooth claymorphism and subsurface scattering"),
  CYBERPUNK("Cyberpunk", "cyberpunk aesthetic, neon violet and cyan glow, rain soaked reflections, volumetric fog, futuristic megacity"),
  SYNTHWAVE("Retro Synthwave", "1980s retro synthwave, neon grid horizon, chrome reflections, sunset purple gradient, outrun style")
}

enum class AspectRatioOption(val label: String, val ratioWidth: Float, val ratioHeight: Float, val dimensionsText: String) {
  RATIO_1_1("1:1", 1f, 1f, "1024 x 1024 (Square)"),
  RATIO_16_9("16:9", 16f, 9f, "1920 x 1080 (Landscape)"),
  RATIO_9_16("9:16", 9f, 16f, "1080 x 1920 (Portrait / Reel)")
}

enum class VideoDuration(val label: String, val seconds: Int, val creditCost: Int) {
  DURATION_5S("5 Seconds", 5, 5),
  DURATION_10S("10 Seconds", 10, 8),
  DURATION_15S("15 Seconds", 15, 12)
}

enum class MotionIntensity(val label: String, val description: String, val speedFactor: Float) {
  SUBTLE("Subtle (0.3x)", "Gentle camera drift & ambient atmosphere", 0.3f),
  DYNAMIC("Dynamic (0.7x)", "Balanced cinematic movement & tracking", 0.7f),
  HYPER("Hyper (1.2x)", "Fast action, speed ramps & energetic camera sweeps", 1.2f)
}

enum class CameraMove(val label: String, val visualCue: String) {
  PAN_LEFT("Pan Left", "Smooth leftward lateral sweep"),
  DOLLY_IN("Dolly In", "Forward push-in toward focal center"),
  ORBIT("Orbit 360", "Circular orbital rotation around subject"),
  DRONE("Drone Aerial", "Rising crane reveal and overhead expanse"),
  STATIC("Static Tripod", "Locked focus with deep subject parallax")
}

enum class MotionGraphicStyle(val label: String, val visualKeywords: String) {
  FLAT_2D("Flat 2D Animation", "Clean Bauhaus shapes, vibrant primary solids, fluid 2D transitions"),
  MINIMAL_VECTOR("Minimalist Vector", "Fine mono lines, geometric precision, architectural negative space"),
  NEON_KINETIC("Neon Kinetic", "High-contrast glowing paths, particle trails, electric violet and cyan aura"),
  HOLOGRAPHIC_3D("3D Holographic Glitch", "Futuristic UI HUD, chromatic aberration, layered wireframe grid"),
  LIQUID_MORPH("Liquid Morph", "Organic fluid metaballs, smooth bezier deformation, glossy gradients")
}

enum class MotionType(val label: String, val description: String) {
  KINETIC_TYPOGRAPHY("Kinetic Typography", "Dynamic animated title sequences & bold text momentum"),
  LOGO_REVEAL("Logo Reveal", "Brand signature transition with glowing energy sweeps"),
  EXPLAINER("Explainer Vector", "Storyboarding concepts with animated iconography"),
  HUD_PARTICLES("HUD & Cyber UI", "High-tech data telemetry & pulsing orbital rings")
}

sealed interface GenerationState<out T> {
  data object Idle : GenerationState<Nothing>
  data class Progress(val stage: String, val percent: Float) : GenerationState<Nothing>
  data class Success<T>(val result: T) : GenerationState<T>
  data class Error(val message: String) : GenerationState<Nothing>
}
