package code.cinnamon.gui.screens

import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text
import code.cinnamon.gui.CinnamonScreen
import code.cinnamon.gui.CinnamonGuiManager
import code.cinnamon.gui.components.CinnamonButton
import code.cinnamon.gui.theme.CinnamonTheme
import kotlin.math.*
import code.cinnamon.gui.theme.ThemeConfigManager

class ThemeManagerScreen : CinnamonScreen(Text.literal("Theme Manager")) {
    
    private var showColorPicker = false
    private var selectedColorType: ColorType? = null
    
    // Fixed color picker position (centered on screen)
    private val pickerWidth = 300
    private val pickerHeight = 350
    private var pickerX = 0
    private var pickerY = 0
    
    // Color picker state
    private var hue = 0f
    private var saturation = 1f
    private var brightness = 1f
    private var alpha = 1f
    
    // Scroll offset for color list
    private var scrollOffset = 0
    private val itemHeight = 35
    private val maxVisibleItems = 12
    
    enum class ColorType(val displayName: String, val currentColor: () -> Int, val setter: (Int) -> Unit) {
        CORE_BACKGROUND_PRIMARY("Primary Background", { CinnamonTheme.coreBackgroundPrimary }, { color ->
            CinnamonTheme.coreBackgroundPrimary = color
            CinnamonTheme.updateDependentColors()
        }),
        CORE_BACKGROUND_SECONDARY("Secondary Background", { CinnamonTheme.coreBackgroundSecondary }, { color ->
            CinnamonTheme.coreBackgroundSecondary = color
            CinnamonTheme.updateDependentColors()
        }),
        CORE_ACCENT_PRIMARY("Primary Accent", { CinnamonTheme.coreAccentPrimary }, { color ->
            CinnamonTheme.coreAccentPrimary = color
            CinnamonTheme.updateDependentColors()
        }),
        CORE_ACCENT_SECONDARY("Secondary Accent/Button", { CinnamonTheme.coreAccentSecondary }, { color ->
            CinnamonTheme.coreAccentSecondary = color
            CinnamonTheme.updateDependentColors()
        }),
        CORE_TEXT_PRIMARY("Primary Text", { CinnamonTheme.coreTextPrimary }, { color ->
            CinnamonTheme.coreTextPrimary = color
            CinnamonTheme.updateDependentColors()
        }),
        CORE_TEXT_SECONDARY("Secondary Text", { CinnamonTheme.coreTextSecondary }, { color ->
            CinnamonTheme.coreTextSecondary = color
            CinnamonTheme.updateDependentColors()
        }),
        CORE_BORDER("Border Color", { CinnamonTheme.coreBorder }, { color ->
            CinnamonTheme.coreBorder = color
            CinnamonTheme.updateDependentColors()
        }),
        CORE_STATUS_SUCCESS("Success Color", { CinnamonTheme.coreStatusSuccess }, { color ->
            CinnamonTheme.coreStatusSuccess = color
            CinnamonTheme.updateDependentColors()
        }),
        CORE_STATUS_WARNING("Warning Color", { CinnamonTheme.coreStatusWarning }, { color ->
            CinnamonTheme.coreStatusWarning = color
            CinnamonTheme.updateDependentColors()
        }),
        CORE_STATUS_ERROR("Error Color", { CinnamonTheme.coreStatusError }, { color ->
            CinnamonTheme.coreStatusError = color
            CinnamonTheme.updateDependentColors()
        });
        // The companion object containing adjustBrightness has been removed as it's no longer needed.
    }
    
    override fun init() {
        super.init()
        
        // Calculate fixed picker position (centered on screen)
        pickerX = (width - pickerWidth) / 2
        pickerY = (height - pickerHeight) / 2
    }
    
    override fun initializeComponents() {
        val centerX = guiX + guiWidth / 2
        val contentY = getContentY()
        val buttonY = contentY + getContentHeight() - 45 // Fixed button position
        
        // Clear existing buttons to prevent duplicates
        clearButtons()
        
        // Back button - fixed position
        addButton(CinnamonButton(
            guiX + PADDING,
            buttonY,
            100,
            CinnamonTheme.BUTTON_HEIGHT,
            Text.literal("Back"),
            { _, _ -> CinnamonGuiManager.openMainMenu() } // Changed to openMainMenu
        ))
        
        // Reset to defaults button - fixed position
        addButton(CinnamonButton(
            centerX - 50,
            buttonY,
            100,
            CinnamonTheme.BUTTON_HEIGHT,
            Text.literal("Reset"),
            { _, _ -> resetToDefaults() }
        ))
        
        // Save theme button - fixed position
        addButton(CinnamonButton(
            guiX + guiWidth - PADDING - 100,
            buttonY,
            100,
            CinnamonTheme.BUTTON_HEIGHT,
            Text.literal("Save"),
            { _, _ -> saveTheme() },
            false // Changed from true to false
        ))
    }
    
    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        // Always render the base screen
        super.render(context, mouseX, mouseY, delta)
        
        // Render color picker on top if open
        if (showColorPicker) {
            renderColorPicker(context, mouseX, mouseY)
        }
    }
    
    override fun renderContent(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        // Don't render content if color picker is open to prevent overlap
        if (showColorPicker) {
            return
        }
        
        val centerX = guiX + guiWidth / 2
        val contentY = getContentY()
        
        // Removed title, subtitle, and glow effect rendering as the screen title is handled by CinnamonScreen.renderHeader
        
        // Render color list
        renderColorList(context, mouseX, mouseY, contentY) // Pass contentY
    }
    
    private fun renderColorList(context: DrawContext, mouseX: Int, mouseY: Int, contentYPos: Int) { // Added parameter
        val listX = guiX + 40
        val listY = contentYPos + 20 // Use parameter
        val listWidth = guiWidth - 80
        val listHeight = getContentHeight() - 170 // Leave space for buttons
        
        // Background for color list
        context.fill(listX, listY, listX + listWidth, listY + listHeight, CinnamonTheme.contentBackground)
        context.drawBorder(listX, listY, listWidth, listHeight, CinnamonTheme.borderColor)
        
        // Render color items
        val colors = ColorType.values()
        var currentY = listY + 10 - scrollOffset
        
        for ((index, colorType) in colors.withIndex()) {
            if (currentY > listY + listHeight) break
            if (currentY + itemHeight < listY) {
                currentY += itemHeight
                continue
            }
            
            renderColorItem(context, colorType, listX + 10, currentY, listWidth - 20, itemHeight - 5, mouseX, mouseY)
            currentY += itemHeight
        }
        
        // Scroll indicators
        if (scrollOffset > 0) {
            context.drawText(textRenderer, Text.literal("▲"), listX + listWidth - 20, listY + 5, CinnamonTheme.accentColor, false)
        }
        if (colors.size * itemHeight > listHeight + scrollOffset) {
            context.drawText(textRenderer, Text.literal("▼"), listX + listWidth - 20, listY + listHeight - 15, CinnamonTheme.accentColor, false)
        }
    }
    
    private fun renderColorItem(context: DrawContext, colorType: ColorType, x: Int, y: Int, width: Int, height: Int, mouseX: Int, mouseY: Int) {
        val isHovered = mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height
        val backgroundColor = if (isHovered) CinnamonTheme.cardBackgroundHover else CinnamonTheme.cardBackground
        
        // Item background
        context.fill(x, y, x + width, y + height, backgroundColor)
        context.drawBorder(x, y, width, height, CinnamonTheme.borderColor)
        
        // Color preview square
        val colorSquareSize = height - 10
        val currentColor = colorType.currentColor()
        context.fill(x + 10, y + 5, x + 10 + colorSquareSize, y + 5 + colorSquareSize, currentColor)
        context.drawBorder(x + 10, y + 5, colorSquareSize, colorSquareSize, 0xFFFFFFFF.toInt())
        
        // Color name
        context.drawText(
            textRenderer,
            Text.literal(colorType.displayName),
            x + 20 + colorSquareSize,
            y + (height - textRenderer.fontHeight) / 2,
            CinnamonTheme.primaryTextColor,
            false
        )
        
        // Color hex value
        val hexValue = String.format("#%08X", currentColor)
        val hexWidth = textRenderer.getWidth(hexValue)
        context.drawText(
            textRenderer,
            Text.literal(hexValue),
            x + width - hexWidth - 10,
            y + (height - textRenderer.fontHeight) / 2,
            CinnamonTheme.secondaryTextColor,
            false
        )
    }
    
    private fun renderColorPicker(context: DrawContext, mouseX: Int, mouseY: Int) {
        // Define consistent Y positions for rendering
        val titleY_render = pickerY + 5 // Adjusted for tighter packing
        val wheelY_render = pickerY + 20
        val wheelSize = 180 // Unchanged
        val sliderHeight = 20 // Unchanged
        val brightnessSliderY_render = wheelY_render + wheelSize + 5 // pickerY + 20 + 180 + 5 = pickerY + 205
        val alphaSliderY_render = brightnessSliderY_render + sliderHeight + 5 // pickerY + 205 + 20 + 5 = pickerY + 230
        val buttonsY_render = pickerY + 290 // Target Y for buttons (used by click handler)
        val previewBoxHeight = 30 // Unchanged
        val previewBoxY_render = buttonsY_render - previewBoxHeight - 5 // pickerY + 290 - 30 - 5 = pickerY + 255

        // Full screen overlay to block interaction with background
        context.fill(0, 0, width, height, 0xC0000000.toInt())
        
        // Picker background
        val pickerBg = 0xF0202020.toInt()
        context.fill(pickerX, pickerY, pickerX + pickerWidth, pickerY + pickerHeight, pickerBg)
        context.drawBorder(pickerX, pickerY, pickerWidth, pickerHeight, CinnamonTheme.accentColor)
        
        // Title
        val title = selectedColorType?.displayName ?: "Color Picker"
        val titleWidth = textRenderer.getWidth(title)
        context.drawText(
            textRenderer,
            Text.literal(title),
            pickerX + (pickerWidth - titleWidth) / 2,
            titleY_render, // Use new title Y
            CinnamonTheme.primaryTextColor,
            true
        )
        
        // Color wheel area
        // val wheelSize = 180 // Defined above
        val wheelX = pickerX + (pickerWidth - wheelSize) / 2 // X position remains the same
        // val wheelY_render = pickerY + 20 // Defined above
        
        renderColorWheel(context, wheelX, wheelY_render, wheelSize)
        
        // Brightness slider
        // val sliderHeight = 20 // Defined above
        // val brightnessSliderY_render = wheelY_render + wheelSize + 5 // Defined above
        renderBrightnessSlider(context, pickerX + 20, brightnessSliderY_render, pickerWidth - 40, sliderHeight)
        
        // Alpha slider
        // val alphaSliderY_render = brightnessSliderY_render + sliderHeight + 5 // Defined above
        renderAlphaSlider(context, pickerX + 20, alphaSliderY_render, pickerWidth - 40, sliderHeight)
        
        // Preview and hex input
        // val previewBoxHeight = 30 // Defined above
        // val previewBoxY_render = buttonsY_render - previewBoxHeight - 5 // Defined above
        renderColorPreview(context, pickerX + 20, previewBoxY_render, pickerWidth - 40, previewBoxHeight)
        
        // Buttons
        // val buttonsY_render = pickerY + 290 // Defined above
        val buttonWidth = 80
        val buttonSpacing = 20
        val totalButtonWidth = buttonWidth * 2 + buttonSpacing
        val buttonStartX = pickerX + (pickerWidth - totalButtonWidth) / 2
        
        // Apply button
        val applyHovered = mouseX >= buttonStartX && mouseX < buttonStartX + buttonWidth && 
                          mouseY >= buttonsY_render && mouseY < buttonsY_render + 25
        context.fill(
            buttonStartX, buttonsY_render, buttonStartX + buttonWidth, buttonsY_render + 25,
            if (applyHovered) CinnamonTheme.accentColorHover else CinnamonTheme.accentColor
        )
        context.drawText(
            textRenderer,
            Text.literal("Apply"),
            buttonStartX + (buttonWidth - textRenderer.getWidth("Apply")) / 2,
            buttonsY_render + 8,
            0xFFFFFFFF.toInt(),
            false
        )
        
        // Cancel button
        val cancelX = buttonStartX + buttonWidth + buttonSpacing
        val cancelHovered = mouseX >= cancelX && mouseX < cancelX + buttonWidth && 
                           mouseY >= buttonsY_render && mouseY < buttonsY_render + 25
        context.fill(
            cancelX, buttonsY_render, cancelX + buttonWidth, buttonsY_render + 25,
            if (cancelHovered) CinnamonTheme.buttonBackgroundHover else CinnamonTheme.buttonBackground
        )
        context.drawText(
            textRenderer,
            Text.literal("Cancel"),
            cancelX + (buttonWidth - textRenderer.getWidth("Cancel")) / 2,
            buttonsY_render + 8,
            CinnamonTheme.primaryTextColor,
            false
        )
    }
    
    private fun renderColorWheel(context: DrawContext, x: Int, y: Int, size: Int) {
        val centerX = x + size / 2
        val centerY = y + size / 2
        val radius = size / 2 - 5
        
        // Draw color wheel using concentric circles
        for (r in 0 until radius step 2) {
            for (angle in 0 until 360 step 4) {
                val rad = Math.toRadians(angle.toDouble())
                val px = centerX + (cos(rad) * r).toInt()
                val py = centerY + (sin(rad) * r).toInt()
                
                val sat = r.toFloat() / radius
                val hueColor = hsvToRgb(angle.toFloat(), sat, brightness)
                
                context.fill(px, py, px + 2, py + 2, hueColor)
            }
        }
        
        // Draw selection indicator
        val selRadius = saturation * radius
        val selAngle = Math.toRadians(hue.toDouble())
        val selX = centerX + (cos(selAngle) * selRadius).toInt()
        val selY = centerY + (sin(selAngle) * selRadius).toInt()
        
        // White border
        context.drawBorder(selX - 4, selY - 4, 8, 8, 0xFFFFFFFF.toInt())
        // Black inner border for visibility
        context.drawBorder(selX - 3, selY - 3, 6, 6, 0xFF000000.toInt())
    }
    
    private fun renderBrightnessSlider(context: DrawContext, x: Int, y: Int, width: Int, height: Int) {
        // Gradient background
        for (i in 0 until width) {
            val brightnessVal = i.toFloat() / width
            val color = hsvToRgb(hue, saturation, brightnessVal)
            context.fill(x + i, y, x + i + 1, y + height, color)
        }
        
        context.drawBorder(x, y, width, height, CinnamonTheme.borderColor)
        
        // Slider handle
        val handleX = x + (brightness * width).toInt() - 2
        context.fill(handleX, y - 2, handleX + 4, y + height + 2, 0xFFFFFFFF.toInt())
        context.drawBorder(handleX - 1, y - 3, 6, height + 4, 0xFF000000.toInt())
    }
    
    private fun renderAlphaSlider(context: DrawContext, x: Int, y: Int, width: Int, height: Int) {
        // Checkerboard background
        val checkerSize = 8
        for (i in 0 until width step checkerSize) {
            for (j in 0 until height step checkerSize) {
                val color = if ((i / checkerSize + j / checkerSize) % 2 == 0) 0xFFCCCCCC.toInt() else 0xFF999999.toInt()
                val endX = minOf(x + i + checkerSize, x + width)
                val endY = minOf(y + j + checkerSize, y + height)
                context.fill(x + i, y + j, endX, endY, color)
            }
        }
        
        // Alpha gradient
        val baseColor = hsvToRgb(hue, saturation, brightness)
        for (i in 0 until width) {
            val alphaVal = i.toFloat() / width
            val color = (baseColor and 0x00FFFFFF) or ((alphaVal * 255).toInt() shl 24)
            context.fill(x + i, y, x + i + 1, y + height, color)
        }
        
        context.drawBorder(x, y, width, height, CinnamonTheme.borderColor)
        
        // Slider handle
        val handleX = x + (alpha * width).toInt() - 2
        context.fill(handleX, y - 2, handleX + 4, y + height + 2, 0xFFFFFFFF.toInt())
        context.drawBorder(handleX - 1, y - 3, 6, height + 4, 0xFF000000.toInt())
    }
    
    private fun renderColorPreview(context: DrawContext, x: Int, y: Int, width: Int, height: Int) {
        // Checkerboard background for transparency preview
        val checkerSize = 8
        for (i in 0 until width step checkerSize) {
            for (j in 0 until height step checkerSize) {
                val color = if ((i / checkerSize + j / checkerSize) % 2 == 0) 0xFFCCCCCC.toInt() else 0xFF999999.toInt()
                val endX = minOf(x + i + checkerSize, x + width)
                val endY = minOf(y + j + checkerSize, y + height)
                context.fill(x + i, y + j, endX, endY, color)
            }
        }
        
        val currentColor = hsvToRgb(hue, saturation, brightness)
        val finalColor = (currentColor and 0x00FFFFFF) or ((alpha * 255).toInt() shl 24)
        
        context.fill(x, y, x + width, y + height, finalColor)
        context.drawBorder(x, y, width, height, CinnamonTheme.borderColor)
        
        // Hex value
        val hexValue = String.format("#%08X", finalColor)
        val textX = x + (width - textRenderer.getWidth(hexValue)) / 2
        val textY = y + (height - textRenderer.fontHeight) / 2
        
        // Draw text with outline for better visibility
        context.drawText(textRenderer, Text.literal(hexValue), textX + 1, textY + 1, 0xFF000000.toInt(), false)
        context.drawText(textRenderer, Text.literal(hexValue), textX, textY, 0xFFFFFFFF.toInt(), false)
    }
    
    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (showColorPicker) {
            // println("[ThemeManagerScreen] mouseClicked detected while showColorPicker is true.") // <--- REMOVE THIS LINE
            val mX = mouseX.toInt()
            val mY = mouseY.toInt()

            // Check if the click occurred within the bounds of the color picker
            if (mX >= pickerX && mX < pickerX + pickerWidth && mY >= pickerY && mY < pickerY + pickerHeight) {
                // Click is INSIDE the color picker's main rectangle.
                // Let handleColorPickerClick manage interaction with picker components.
                return handleColorPickerClick(mX, mY, button)
            } else {
                // Click is OUTSIDE the color picker's main rectangle, but the overlay is active.
                // This means the user clicked on the overlay part that is not the picker itself.
                // Consume the event and close the picker.
                showColorPicker = false
                return true 
            }
        }
        
        // Handle color list clicks
        val listX = guiX + 40
        val listY = getContentY() + 80
        val listWidth = guiWidth - 80
        val listHeight = getContentHeight() - 170
        
        if (mouseX >= listX && mouseX < listX + listWidth && mouseY >= listY && mouseY < listY + listHeight) {
            val colors = ColorType.values()
            val clickedIndex = ((mouseY - listY - 10 + scrollOffset) / itemHeight).toInt()
            
            if (clickedIndex >= 0 && clickedIndex < colors.size) {
                openColorPicker(colors[clickedIndex])
                return true
            }
        }
        
        return super.mouseClicked(mouseX, mouseY, button)
    }
    
    private fun handleColorPickerClick(mouseX: Int, mouseY: Int, button: Int): Boolean {
        // println("[ThemeManagerScreen] handleColorPickerClick called. mouseX: $mouseX, mouseY: $mouseY") // <--- REMOVE THIS LINE
        // NOTE: The check for clicks outside the picker's main rectangle has been moved to mouseClicked.
        // This method now assumes the click is within pickerX, pickerY, pickerWidth, pickerHeight.
        
        // Handle button clicks
        val buttonY = pickerY + 290 // This is the target Y for buttons
        val buttonWidth = 80
        val buttonSpacing = 20
        val totalButtonWidth = buttonWidth * 2 + buttonSpacing
        val buttonStartX = pickerX + (pickerWidth - totalButtonWidth) / 2
        
        if (mouseY >= buttonY && mouseY < buttonY + 25) {
            if (mouseX >= buttonStartX && mouseX < buttonStartX + buttonWidth) {
                // Apply button
                applyColor()
                ThemeConfigManager.saveTheme() // Save theme after applying color
                showColorPicker = false
                return true
            } else if (mouseX >= buttonStartX + buttonWidth + buttonSpacing && mouseX < buttonStartX + buttonWidth * 2 + buttonSpacing) {
                // Cancel button
                showColorPicker = false
                return true
            }
        }
        
        // Handle color wheel, brightness, and alpha slider clicks
        handleColorControlClicks(mouseX, mouseY)
        
        return true
    }
    
    private fun handleColorControlClicks(mouseX: Int, mouseY: Int) {
        val wheelSize = 180
        val wheelX = pickerX + (pickerWidth - wheelSize) / 2
        val wheelY_click = pickerY + 20 // Consistent with wheelY_render
        val centerX = wheelX + wheelSize / 2
        val centerY = wheelY_click + wheelSize / 2
        val radius = wheelSize / 2 - 5
        
        // Color wheel click
        if (mouseX >= wheelX && mouseX < wheelX + wheelSize && mouseY >= wheelY_click && mouseY < wheelY_click + wheelSize) {
            val dx = mouseX - centerX
            val dy = mouseY - centerY
            val distance = sqrt((dx * dx + dy * dy).toDouble()).toFloat()
            
            if (distance <= radius) {
                hue = (atan2(dy.toDouble(), dx.toDouble()) * 180 / PI).toFloat()
                if (hue < 0) hue += 360
                saturation = minOf(1f, distance / radius)
            }
        }
        
        val sliderHeight = 20 // Consistent with rendering
        // Brightness slider click
        val brightnessSliderY_click = pickerY + 205 // Consistent with brightnessSliderY_render
        if (mouseX >= pickerX + 20 && mouseX < pickerX + pickerWidth - 20 && mouseY >= brightnessSliderY_click && mouseY < brightnessSliderY_click + sliderHeight) {
            brightness = ((mouseX - pickerX - 20).toFloat() / (pickerWidth - 40)).coerceIn(0f, 1f)
        }
        
        // Alpha slider click
        val alphaSliderY_click = pickerY + 230 // Consistent with alphaSliderY_render
        if (mouseX >= pickerX + 20 && mouseX < pickerX + pickerWidth - 20 && mouseY >= alphaSliderY_click && mouseY < alphaSliderY_click + sliderHeight) {
            alpha = ((mouseX - pickerX - 20).toFloat() / (pickerWidth - 40)).coerceIn(0f, 1f)
        }
    }
    
    override fun mouseScrolled(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double): Boolean {
        if (!showColorPicker) {
            val listY = getContentY() + 80
            val listHeight = getContentHeight() - 170
            
            if (mouseY >= listY && mouseY < listY + listHeight) {
                val maxScroll = maxOf(0, ColorType.values().size * itemHeight - listHeight)
                scrollOffset = (scrollOffset - verticalAmount.toInt() * 20).coerceIn(0, maxScroll)
                return true
            }
        }
        
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)
    }
    
    private fun openColorPicker(colorType: ColorType) {
        selectedColorType = colorType
        showColorPicker = true
        
        // Initialize color picker with current color
        val currentColor = colorType.currentColor()
        val hsv = rgbToHsv(currentColor)
        hue = hsv[0]
        saturation = hsv[1]
        brightness = hsv[2]
        alpha = ((currentColor ushr 24) and 0xFF) / 255f
    }
    
    private fun applyColor() {
        selectedColorType?.let { colorType ->
            val finalColor = hsvToRgb(hue, saturation, brightness)
            val colorWithAlpha = (finalColor and 0x00FFFFFF) or ((alpha * 255).toInt() shl 24)
            
            // Logging before setter
            println("[ThemeManagerScreen] Applying color for: ${colorType.displayName}")
            println("[ThemeManagerScreen] HSVA values: H=${hue}, S=${saturation}, V=${brightness}, A=${alpha}")
            println("[ThemeManagerScreen] Calculated RGBA: ${String.format("#%08X", colorWithAlpha)}")
            
            colorType.setter(colorWithAlpha)
            
            // Logging after setter
            println("[ThemeManagerScreen] Setter called for ${colorType.displayName}. Current theme value: ${String.format("#%08X", colorType.currentColor())}")

            this.initializeComponents() // Refresh UI components
        }
    }
    
    private fun resetToDefaults() {
        // Reset all colors to their default values
        CinnamonTheme.resetToDefaults()
    }
    
    private fun saveTheme() {
        // Save the current theme configuration to file
        ThemeConfigManager.saveTheme()
        
        // Optional: Add some visual feedback
        // You could show a toast or temporary status message here
    }
    
    // Color conversion utilities
    private fun hsvToRgb(h: Float, s: Float, v: Float): Int {
        val c = v * s
        val x = c * (1 - abs(((h / 60) % 2) - 1))
        val m = v - c
        
        val (r, g, b) = when {
            h < 60 -> Triple(c, x, 0f)
            h < 120 -> Triple(x, c, 0f)
            h < 180 -> Triple(0f, c, x)
            h < 240 -> Triple(0f, x, c)
            h < 300 -> Triple(x, 0f, c)
            else -> Triple(c, 0f, x)
        }
        
        val red = ((r + m) * 255).toInt().coerceIn(0, 255)
        val green = ((g + m) * 255).toInt().coerceIn(0, 255)
        val blue = ((b + m) * 255).toInt().coerceIn(0, 255)
        
        return (255 shl 24) or (red shl 16) or (green shl 8) or blue
    }
    
    private fun rgbToHsv(color: Int): FloatArray {
        val r = ((color shr 16) and 0xFF) / 255f
        val g = ((color shr 8) and 0xFF) / 255f
        val b = (color and 0xFF) / 255f
        
        val max = maxOf(r, g, b)
        val min = minOf(r, g, b)
        val delta = max - min
        
        val h = when {
            delta == 0f -> 0f
            max == r -> 60 * (((g - b) / delta) % 6)
            max == g -> 60 * (((b - r) / delta) + 2)
            else -> 60 * (((r - g) / delta) + 4)
        }
        
        val s = if (max == 0f) 0f else delta / max
        val v = max
        
        return floatArrayOf(if (h < 0) h + 360 else h, s, v)
    }
    
    override fun renderFooter(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        // Don't render footer if color picker is open
        if (showColorPicker) {
            return
        }
        
        super.renderFooter(context, mouseX, mouseY, delta)
        
        // Draw theme status
        val statusText = Text.literal("Theme Editor")
        context.drawText(
            textRenderer,
            statusText,
            guiX + PADDING,
            getFooterY() + (FOOTER_HEIGHT - textRenderer.fontHeight) / 2,
            CinnamonTheme.infoColor,
            false
        )
        
        // Draw color count
        val colorCountText = Text.literal("${ColorType.values().size} Colors Available")
        val colorCountWidth = textRenderer.getWidth(colorCountText)
        context.drawText(
            textRenderer,
            colorCountText,
            guiX + guiWidth - colorCountWidth - PADDING,
            getFooterY() + (FOOTER_HEIGHT - textRenderer.fontHeight) / 2,
            CinnamonTheme.secondaryTextColor,
            false
        )
    }
    
    // Helper function to clear buttons (you may need to implement this in your base class)
    private fun clearButtons() {
        // This would clear the button list to prevent duplicates
        // Implementation depends on your CinnamonScreen base class
    }
}