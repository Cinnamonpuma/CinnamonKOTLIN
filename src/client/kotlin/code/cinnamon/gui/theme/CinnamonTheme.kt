package code.cinnamon.gui.theme

object CinnamonTheme {
    // Make colors mutable variables instead of constants
    var guiBackground = 0xE61a1a1a.toInt()
    var guiBorder = 0xFF404040.toInt()
    
    // Background colors (dark theme similar to Lunar Client)
    var backgroundTop = 0xFF1a1a1a.toInt()
    var backgroundBottom = 0xFF0d0d0d.toInt()
    var headerBackground = 0xF0262626.toInt()
    var footerBackground = 0xF0262626.toInt()
    var sidebarBackground = 0xFF262626.toInt()
    
    // Content area colors
    var contentBackground = 0xE61f1f1f.toInt()
    var cardBackground = 0xE6333333.toInt()
    var cardBackgroundHover = 0xE63d3d3d.toInt()
    
    // Border and accent colors
    var borderColor = 0xFF404040.toInt()
    var accentColor = 0xFF00aaff.toInt()
    var accentColorHover = 0xFF0099dd.toInt()
    var accentColorPressed = 0xFF0088cc.toInt()
    
    // Text colors
    var titleColor = 0xFFffffff.toInt()
    var primaryTextColor = 0xFFe0e0e0.toInt()
    var secondaryTextColor = 0xFFa0a0a0.toInt()
    var disabledTextColor = 0xFF606060.toInt()
    
    // Button colors
    var buttonBackground = 0xE6404040.toInt()
    var buttonBackgroundHover = 0xE64a4a4a.toInt()
    var buttonBackgroundPressed = 0xE6353535.toInt()
    var buttonBackgroundDisabled = 0xE62a2a2a.toInt()
    
    // Primary button colors (accent colored)
    var primaryButtonBackground = 0xE600aaff.toInt()
    var primaryButtonBackgroundHover = accentColorHover
    var primaryButtonBackgroundPressed = accentColorPressed
    
    // Status colors
    var successColor = 0xFF4caf50.toInt()
    var warningColor = 0xFFff9800.toInt()
    var errorColor = 0xFFf44336.toInt()
    var infoColor = 0xFF2196f3.toInt()
    
    // Module colors
    var moduleEnabledColor = successColor
    var moduleDisabledColor = 0xFF757575.toInt()
    var moduleBackgroundEnabled = 0xE61b5e20.toInt()
    var moduleBackgroundDisabled = 0xE6424242.toInt()
    
    // Pattern and overlay
    var patternColor = 0x10ffffff.toInt()
    var overlayColor = 0x80000000.toInt()
    
    // Glass effect colors
    var glassHighlight = 0x20ffffff.toInt()
    var glassShadow = 0x40000000.toInt()
    
    // Store default values for reset functionality
    private val defaultColors = mutableMapOf<String, Int>()
    
    init {
        // Store default values when the object is created
        saveDefaults()
    }
    
    private fun saveDefaults() {
        defaultColors["guiBackground"] = guiBackground
        defaultColors["guiBorder"] = guiBorder
        defaultColors["backgroundTop"] = backgroundTop
        defaultColors["backgroundBottom"] = backgroundBottom
        defaultColors["headerBackground"] = headerBackground
        defaultColors["footerBackground"] = footerBackground
        defaultColors["contentBackground"] = contentBackground
        defaultColors["cardBackground"] = cardBackground
        defaultColors["accentColor"] = accentColor
        defaultColors["primaryTextColor"] = primaryTextColor
        defaultColors["secondaryTextColor"] = secondaryTextColor
        defaultColors["buttonBackground"] = buttonBackground
        defaultColors["successColor"] = successColor
        defaultColors["warningColor"] = warningColor
        defaultColors["errorColor"] = errorColor
    }
    
    fun resetToDefaults() {
        guiBackground = defaultColors["guiBackground"] ?: 0xE61a1a1a.toInt()
        guiBorder = defaultColors["guiBorder"] ?: 0xFF404040.toInt()
        backgroundTop = defaultColors["backgroundTop"] ?: 0xFF1a1a1a.toInt()
        backgroundBottom = defaultColors["backgroundBottom"] ?: 0xFF0d0d0d.toInt()
        headerBackground = defaultColors["headerBackground"] ?: 0xF0262626.toInt()
        footerBackground = defaultColors["footerBackground"] ?: 0xF0262626.toInt()
        contentBackground = defaultColors["contentBackground"] ?: 0xE61f1f1f.toInt()
        cardBackground = defaultColors["cardBackground"] ?: 0xE6333333.toInt()
        accentColor = defaultColors["accentColor"] ?: 0xFF00aaff.toInt()
        primaryTextColor = defaultColors["primaryTextColor"] ?: 0xFFe0e0e0.toInt()
        secondaryTextColor = defaultColors["secondaryTextColor"] ?: 0xFFa0a0a0.toInt()
        buttonBackground = defaultColors["buttonBackground"] ?: 0xE6404040.toInt()
        successColor = defaultColors["successColor"] ?: 0xFF4caf50.toInt()
        warningColor = defaultColors["warningColor"] ?: 0xFFff9800.toInt()
        errorColor = defaultColors["errorColor"] ?: 0xFFf44336.toInt()
        
        // Update dependent colors
        updateDependentColors()
    }
    
    private fun updateDependentColors() {
        // Update colors that depend on other colors
        accentColorHover = adjustBrightness(accentColor, -0.1f)
        accentColorPressed = adjustBrightness(accentColor, -0.2f)
        primaryButtonBackground = (accentColor and 0x00FFFFFF) or 0xE6000000.toInt()
        primaryButtonBackgroundHover = accentColorHover
        primaryButtonBackgroundPressed = accentColorPressed
        moduleEnabledColor = successColor
        cardBackgroundHover = adjustBrightness(cardBackground, 0.1f)
        buttonBackgroundHover = adjustBrightness(buttonBackground, 0.1f)
        buttonBackgroundPressed = adjustBrightness(buttonBackground, -0.1f)
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
    
    // Animation durations (in milliseconds) - keep as constants
    const val ANIMATION_DURATION_SHORT = 150L
    const val ANIMATION_DURATION_MEDIUM = 250L
    const val ANIMATION_DURATION_LONG = 400L
    
    // Sizing constants - keep as constants
    const val BUTTON_HEIGHT = 32
    const val BUTTON_HEIGHT_SMALL = 24
    const val BUTTON_HEIGHT_LARGE = 40
    const val BORDER_RADIUS = 4
    const val CARD_PADDING = 16
    const val COMPONENT_SPACING = 8
}