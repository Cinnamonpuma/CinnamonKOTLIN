package code.cinnamon.gui.theme

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import java.io.File
import java.nio.file.Paths

@Serializable
data class ThemeConfig(
    val guiBackground: Int = 0xE61a1a1a.toInt(),
    val guiBorder: Int = 0xFF404040.toInt(),
    val backgroundTop: Int = 0xFF1a1a1a.toInt(),
    val backgroundBottom: Int = 0xFF0d0d0d.toInt(),
    val headerBackground: Int = 0xF0262626.toInt(),
    val footerBackground: Int = 0xF0262626.toInt(),
    val contentBackground: Int = 0xE61f1f1f.toInt(),
    val cardBackground: Int = 0xE6333333.toInt(),
    val accentColor: Int = 0xFF00aaff.toInt(),
    val primaryTextColor: Int = 0xFFe0e0e0.toInt(),
    val secondaryTextColor: Int = 0xFFa0a0a0.toInt(),
    val buttonBackground: Int = 0xE6404040.toInt(),
    val successColor: Int = 0xFF4caf50.toInt(),
    val warningColor: Int = 0xFFff9800.toInt(),
    val errorColor: Int = 0xFFf44336.toInt()
)

object ThemeConfigManager {
    private val json = Json { prettyPrint = true }
    private val configDir = Paths.get("config", "cinnamon").toFile()
    private val themeFile = File(configDir, "theme.json")
    
    init {
        // Create config directory if it doesn't exist
        if (!configDir.exists()) {
            configDir.mkdirs()
        }
    }
    
    fun saveTheme() {
        try {
            val config = ThemeConfig(
                guiBackground = CinnamonTheme.guiBackground,
                guiBorder = CinnamonTheme.guiBorder,
                backgroundTop = CinnamonTheme.backgroundTop,
                backgroundBottom = CinnamonTheme.backgroundBottom,
                headerBackground = CinnamonTheme.headerBackground,
                footerBackground = CinnamonTheme.footerBackground,
                contentBackground = CinnamonTheme.contentBackground,
                cardBackground = CinnamonTheme.cardBackground,
                accentColor = CinnamonTheme.accentColor,
                primaryTextColor = CinnamonTheme.primaryTextColor,
                secondaryTextColor = CinnamonTheme.secondaryTextColor,
                buttonBackground = CinnamonTheme.buttonBackground,
                successColor = CinnamonTheme.successColor,
                warningColor = CinnamonTheme.warningColor,
                errorColor = CinnamonTheme.errorColor
            )

           
            
            val jsonString = json.encodeToString(config)
            themeFile.writeText(jsonString)
            println("Theme saved successfully to ${themeFile.absolutePath}")
        } catch (e: Exception) {
            println("Failed to save theme: ${e.message}")
        }
    }
    
    fun loadTheme() {
        try {
            if (!themeFile.exists()) return
            
            val jsonString = themeFile.readText()
            val config = json.decodeFromString<ThemeConfig>(jsonString)
            
            // Apply loaded theme
            CinnamonTheme.guiBackground = config.guiBackground
            CinnamonTheme.guiBorder = config.guiBorder
            CinnamonTheme.backgroundTop = config.backgroundTop
            CinnamonTheme.backgroundBottom = config.backgroundBottom
            CinnamonTheme.headerBackground = config.headerBackground
            CinnamonTheme.footerBackground = config.footerBackground
            CinnamonTheme.contentBackground = config.contentBackground
            CinnamonTheme.cardBackground = config.cardBackground
            CinnamonTheme.accentColor = config.accentColor
            CinnamonTheme.primaryTextColor = config.primaryTextColor
            CinnamonTheme.secondaryTextColor = config.secondaryTextColor
            CinnamonTheme.buttonBackground = config.buttonBackground
            CinnamonTheme.successColor = config.successColor
            CinnamonTheme.warningColor = config.warningColor
            CinnamonTheme.errorColor = config.errorColor
            
            // Update dependent colors
            updateDependentColors()
        } catch (e: Exception) {
            println("Failed to load theme: ${e.message}")
        }
    }
    
    private fun updateDependentColors() {
        CinnamonTheme.accentColorHover = adjustBrightness(CinnamonTheme.accentColor, -0.1f)
        CinnamonTheme.accentColorPressed = adjustBrightness(CinnamonTheme.accentColor, -0.2f)
        CinnamonTheme.primaryButtonBackground = (CinnamonTheme.accentColor and 0x00FFFFFF) or 0xE6000000.toInt()
        CinnamonTheme.primaryButtonBackgroundHover = CinnamonTheme.accentColorHover
        CinnamonTheme.primaryButtonBackgroundPressed = CinnamonTheme.accentColorPressed
        CinnamonTheme.moduleEnabledColor = CinnamonTheme.successColor
        CinnamonTheme.cardBackgroundHover = adjustBrightness(CinnamonTheme.cardBackground, 0.1f)
        CinnamonTheme.buttonBackgroundHover = adjustBrightness(CinnamonTheme.buttonBackground, 0.1f)
        CinnamonTheme.buttonBackgroundPressed = adjustBrightness(CinnamonTheme.buttonBackground, -0.1f)
    }
    
    private fun adjustBrightness(color: Int, factor: Float): Int {
        val alpha = (color shr 24) and 0xFF
        val red = ((color shr 16) and 0xFF).toFloat()
        val green = ((color shr 8) and 0xFF).toFloat()
        val blue = (color and 0xFF).toFloat()
        
        val newRed = (red + (if (factor > 0) (255 - red) * factor else red * factor)).coerceIn(0f, 255f).toInt()
        val newGreen = (green + (if (factor > 0) (255 - green) * factor else green * factor)).coerceIn(0f, 255f).toInt()
        val newBlue = (blue + (if (factor > 0) (255 - blue) * factor else blue * factor)).coerceIn(0f, 255f).toInt()
        
        return (alpha shl 24) or (newRed shl 16) or (newGreen shl 8) or newBlue
    }
}