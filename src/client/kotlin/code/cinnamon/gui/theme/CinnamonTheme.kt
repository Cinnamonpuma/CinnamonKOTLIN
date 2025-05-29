package code.cinnamon.gui.theme

object CinnamonTheme {

    // 1. Core Color Properties (Mutable)
    var coreBackgroundPrimary = 0xE61a1a1a.toInt()     // Primary Background (e.g., old guiBackground)
    var coreBackgroundSecondary = 0xE61f1f1f.toInt()   // Secondary Background (e.g., old contentBackground)
    var coreAccentPrimary = 0xFF00aaff.toInt()         // Primary Accent (e.g., old accentColor)
    var coreAccentSecondary = 0xE6404040.toInt()       // Secondary Accent/Button (e.g., old buttonBackground)
    var coreTextPrimary = 0xFFe0e0e0.toInt()           // Primary Text (e.g., old primaryTextColor)
    var coreTextSecondary = 0xFFa0a0a0.toInt()         // Secondary Text (e.g., old secondaryTextColor)
    var coreBorder = 0xFF404040.toInt()               // Border Color (e.g., old guiBorder)
    var coreStatusSuccess = 0xFF4caf50.toInt()         // Success Color
    var coreStatusWarning = 0xFFff9800.toInt()         // Warning Color
    var coreStatusError = 0xFFf44336.toInt()           // Error Color

    // 2. Independent Special Effect Colors (Mutable)
    var patternColor = 0x10ffffff.toInt()
    var overlayColor = 0x80000000.toInt()
    var glassHighlight = 0x20ffffff.toInt()
    var glassShadow = 0x40000000.toInt()

    // 3. Derived Color Properties (Mutable, updated by updateDependentColors)
    // These need to be 'var' because they are calculated and reassigned.
    // Initialize with a placeholder or calculation based on initial core defaults.
    // The init block will call updateDependentColors to set them correctly.
    var cardBackgroundHover: Int = 0
    var accentColorHover: Int = 0
    var accentColorPressed: Int = 0
    var primaryButtonBackground: Int = 0
    var primaryButtonBackgroundHover: Int = 0
    var primaryButtonBackgroundPressed: Int = 0
    var buttonBackgroundHover: Int = 0
    var buttonBackgroundPressed: Int = 0
    var moduleEnabledColor: Int = 0


    // --- Old Color Properties: Mapped to Core or Derived Colors via Getters ---

    // Backgrounds
    val guiBackground: Int get() = coreBackgroundPrimary
    val backgroundTop: Int get() = coreBackgroundPrimary 
    val backgroundBottom: Int get() = adjustBrightness(coreBackgroundPrimary, -0.05f) // Slight adjustment for depth
    
    val headerBackground: Int get() = coreBackgroundSecondary
    val footerBackground: Int get() = coreBackgroundSecondary
    val contentBackground: Int get() = coreBackgroundSecondary
    val sidebarBackground: Int get() = coreBackgroundSecondary 
    
    // Content Area
    val cardBackground: Int get() = coreBackgroundSecondary 
    // cardBackgroundHover is a derived 'var'

    // Borders & Accents
    val borderColor: Int get() = coreBorder
    val accentColor: Int get() = coreAccentPrimary
    // accentColorHover, accentColorPressed are derived 'var's

    // Text
    val titleColor: Int get() = coreTextPrimary
    val primaryTextColor: Int get() = coreTextPrimary
    val secondaryTextColor: Int get() = coreTextSecondary
    val disabledTextColor: Int get() = adjustBrightness(coreTextSecondary, -0.2f)

    // Buttons
    val buttonBackground: Int get() = coreAccentSecondary
    // buttonBackgroundHover, buttonBackgroundPressed are derived 'var's
    val buttonBackgroundDisabled: Int get() = adjustBrightness(coreAccentSecondary, -0.3f)

    // Primary Buttons (Accent Colored)
    // primaryButtonBackground, primaryButtonBackgroundHover, primaryButtonBackgroundPressed are derived 'var's

    // Status Colors
    val successColor: Int get() = coreStatusSuccess
    val warningColor: Int get() = coreStatusWarning
    val errorColor: Int get() = coreStatusError
    val infoColor: Int get() = coreAccentPrimary // Mapped to primary accent

    // Module Colors
    // moduleEnabledColor is a derived 'var'
    val moduleDisabledColor: Int get() = adjustBrightness(coreAccentSecondary, -0.1f)
    val moduleBackgroundEnabled: Int get() = adjustBrightness(coreStatusSuccess, -0.3f) // Darker version of success
    val moduleBackgroundDisabled: Int get() = adjustBrightness(coreAccentSecondary, -0.2f)


    // Store default values for reset functionality
    private val defaultColors = mutableMapOf<String, Int>()
    
    init {
        saveDefaults()
        updateDependentColors() // Ensure derived colors are calculated on initialization
    }
    
    private fun saveDefaults() {
        defaultColors.clear() // Clear any old defaults if this were ever called multiple times
        defaultColors["coreBackgroundPrimary"] = coreBackgroundPrimary
        defaultColors["coreBackgroundSecondary"] = coreBackgroundSecondary
        defaultColors["coreAccentPrimary"] = coreAccentPrimary
        defaultColors["coreAccentSecondary"] = coreAccentSecondary
        defaultColors["coreTextPrimary"] = coreTextPrimary
        defaultColors["coreTextSecondary"] = coreTextSecondary
        defaultColors["coreBorder"] = coreBorder
        defaultColors["coreStatusSuccess"] = coreStatusSuccess
        defaultColors["coreStatusWarning"] = coreStatusWarning
        defaultColors["coreStatusError"] = coreStatusError

        defaultColors["patternColor"] = patternColor
        defaultColors["overlayColor"] = overlayColor
        defaultColors["glassHighlight"] = glassHighlight
        defaultColors["glassShadow"] = glassShadow
    }
    
    fun resetToDefaults() {
        coreBackgroundPrimary = defaultColors["coreBackgroundPrimary"] ?: 0xE61a1a1a.toInt()
        coreBackgroundSecondary = defaultColors["coreBackgroundSecondary"] ?: 0xE61f1f1f.toInt()
        coreAccentPrimary = defaultColors["coreAccentPrimary"] ?: 0xFF00aaff.toInt()
        coreAccentSecondary = defaultColors["coreAccentSecondary"] ?: 0xE6404040.toInt()
        coreTextPrimary = defaultColors["coreTextPrimary"] ?: 0xFFe0e0e0.toInt()
        coreTextSecondary = defaultColors["coreTextSecondary"] ?: 0xFFa0a0a0.toInt()
        coreBorder = defaultColors["coreBorder"] ?: 0xFF404040.toInt()
        coreStatusSuccess = defaultColors["coreStatusSuccess"] ?: 0xFF4caf50.toInt()
        coreStatusWarning = defaultColors["coreStatusWarning"] ?: 0xFFff9800.toInt()
        coreStatusError = defaultColors["coreStatusError"] ?: 0xFFf44336.toInt()

        patternColor = defaultColors["patternColor"] ?: 0x10ffffff.toInt()
        overlayColor = defaultColors["overlayColor"] ?: 0x80000000.toInt()
        glassHighlight = defaultColors["glassHighlight"] ?: 0x20ffffff.toInt()
        glassShadow = defaultColors["glassShadow"] ?: 0x40000000.toInt()
        
        updateDependentColors() // Recalculate all derived colors
    }
    
    fun updateDependentColors() {
        // Update 'var' properties that depend on core colors
        cardBackgroundHover = adjustBrightness(coreBackgroundSecondary, 0.1f)
        
        accentColorHover = adjustBrightness(coreAccentPrimary, -0.1f)
        accentColorPressed = adjustBrightness(coreAccentPrimary, -0.2f)
        
        primaryButtonBackground = (coreAccentPrimary and 0x00FFFFFF) or 0xE6000000.toInt() // Retain original alpha logic
        primaryButtonBackgroundHover = accentColorHover 
        primaryButtonBackgroundPressed = accentColorPressed
        
        buttonBackgroundHover = adjustBrightness(coreAccentSecondary, 0.1f)
        buttonBackgroundPressed = adjustBrightness(coreAccentSecondary, -0.1f)
        
        moduleEnabledColor = coreStatusSuccess
        
        // Note: 'val' properties with 'get()' are automatically "updated" as they always read the current core color.
        // No need to list them here (e.g. guiBackground, borderColor, titleColor, etc.)
        // Also, specific derivations in getters (like disabledTextColor) are also always live.
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