package code.cinnamon.gui.theme

object CinnamonTheme {
    // Background colors (dark theme similar to Lunar Client)
    const val backgroundTop = 0xFF1a1a1a.toInt()
    const val backgroundBottom = 0xFF0d0d0d.toInt()
    const val headerBackground = 0xFF2d2d2d.toInt()
    const val footerBackground = 0xFF2d2d2d.toInt()
    const val sidebarBackground = 0xFF262626.toInt()
    
    // Content area colors
    const val contentBackground = 0xFF1f1f1f.toInt()
    const val cardBackground = 0xFF333333.toInt()
    const val cardBackgroundHover = 0xFF3d3d3d.toInt()
    
    // Border and accent colors
    const val borderColor = 0xFF404040.toInt()
    const val accentColor = 0xFF00aaff.toInt() // Lunar Client blue
    const val accentColorHover = 0xFF0099dd.toInt()
    const val accentColorPressed = 0xFF0088cc.toInt()
    
    // Text colors
    const val titleColor = 0xFFffffff.toInt()
    const val primaryTextColor = 0xFFe0e0e0.toInt()
    const val secondaryTextColor = 0xFFa0a0a0.toInt()
    const val disabledTextColor = 0xFF606060.toInt()
    
    // Button colors
    const val buttonBackground = 0xFF404040.toInt()
    const val buttonBackgroundHover = 0xFF4a4a4a.toInt()
    const val buttonBackgroundPressed = 0xFF353535.toInt()
    const val buttonBackgroundDisabled = 0xFF2a2a2a.toInt()
    
    // Primary button colors (accent colored)
    const val primaryButtonBackground = accentColor
    const val primaryButtonBackgroundHover = accentColorHover
    const val primaryButtonBackgroundPressed = accentColorPressed
    
    // Status colors
    const val successColor = 0xFF4caf50.toInt()
    const val warningColor = 0xFFff9800.toInt()
    const val errorColor = 0xFFf44336.toInt()
    const val infoColor = 0xFF2196f3.toInt()
    
    // Module colors
    const val moduleEnabledColor = successColor
    const val moduleDisabledColor = 0xFF757575.toInt()
    const val moduleBackgroundEnabled = 0xFF1b5e20.toInt()
    const val moduleBackgroundDisabled = 0xFF424242.toInt()
    
    // Pattern and overlay
    const val patternColor = 0x10ffffff.toInt()
    const val overlayColor = 0x80000000.toInt()
    
    // Animation durations (in milliseconds)
    const val ANIMATION_DURATION_SHORT = 150L
    const val ANIMATION_DURATION_MEDIUM = 250L
    const val ANIMATION_DURATION_LONG = 400L
    
    // Sizing constants
    const val BUTTON_HEIGHT = 32
    const val BUTTON_HEIGHT_SMALL = 24
    const val BUTTON_HEIGHT_LARGE = 40
    const val BORDER_RADIUS = 4
    const val CARD_PADDING = 16
    const val COMPONENT_SPACING = 8
}