package code.cinnamon.gui.screens

import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text
import code.cinnamon.gui.CinnamonScreen
import code.cinnamon.gui.CinnamonGuiManager
import code.cinnamon.gui.components.CinnamonButton
import code.cinnamon.gui.theme.CinnamonTheme
import kotlin.math.*

class ThemeManagerScreen : CinnamonScreen(Text.literal("Theme Manager")) {
    
    private var showColorPicker = false
    private var selectedColorType: ColorType? = null
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
        GUI_BACKGROUND("GUI Background", { CinnamonTheme.guiBackground }, { /* Set color */ }),
        GUI_BORDER("GUI Border", { CinnamonTheme.guiBorder }, { /* Set color */ }),
        BACKGROUND_TOP("Background Top", { CinnamonTheme.backgroundTop }, { /* Set color */ }),
        BACKGROUND_BOTTOM("Background Bottom", { CinnamonTheme.backgroundBottom }, { /* Set color */ }),
        HEADER_BACKGROUND("Header Background", { CinnamonTheme.headerBackground }, { /* Set color */ }),
        FOOTER_BACKGROUND("Footer Background", { CinnamonTheme.footerBackground }, { /* Set color */ }),
        CONTENT_BACKGROUND("Content Background", { CinnamonTheme.contentBackground }, { /* Set color */ }),
        CARD_BACKGROUND("Card Background", { CinnamonTheme.cardBackground }, { /* Set color */ }),
        ACCENT_COLOR("Accent Color", { CinnamonTheme.accentColor }, { /* Set color */ }),
        PRIMARY_TEXT("Primary Text", { CinnamonTheme.primaryTextColor }, { /* Set color */ }),
        SECONDARY_TEXT("Secondary Text", { CinnamonTheme.secondaryTextColor }, { /* Set color */ }),
        BUTTON_BACKGROUND("Button Background", { CinnamonTheme.buttonBackground }, { /* Set color */ }),
        SUCCESS_COLOR("Success Color", { CinnamonTheme.successColor }, { /* Set color */ }),
        WARNING_COLOR("Warning Color", { CinnamonTheme.warningColor }, { /* Set color */ }),
        ERROR_COLOR("Error Color", { CinnamonTheme.errorColor }, { /* Set color */ })
    }
    
    override fun initializeComponents() {
        val centerX = guiX + guiWidth / 2
        val contentY = getContentY()
        
        // Back button
        addButton(CinnamonButton(
            guiX + PADDING,
            contentY + getContentHeight() - 35,
            100,
            CinnamonTheme.BUTTON_HEIGHT,
            Text.literal("Back"),
            { _, _ -> CinnamonGuiManager.closeCurrentScreen() }
        ))
        
        // Reset to defaults button
        addButton(CinnamonButton(
            centerX - 50,
            contentY + getContentHeight() - 35,
            100,
            CinnamonTheme.BUTTON_HEIGHT,
            Text.literal("Reset"),
            { _, _ -> resetToDefaults() }
        ))
        
        // Save theme button
        addButton(CinnamonButton(
            guiX + guiWidth - PADDING - 100,
            contentY + getContentHeight() - 35,
            100,
            CinnamonTheme.BUTTON_HEIGHT,
            Text.literal("Save"),
            { _, _ -> saveTheme() },
            true
        ))
    }
    
    override fun renderContent(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        val centerX = guiX + guiWidth / 2
        val contentY = getContentY()
        
        // Draw title area
        val titleText = Text.literal("THEME MANAGER")
        val titleWidth = textRenderer.getWidth(titleText)
        context.drawText(
            textRenderer,
            titleText,
            centerX - titleWidth / 2,
            contentY + 20,
            CinnamonTheme.accentColor,
            true
        )
        
        // Draw subtitle
        val subtitleText = Text.literal("Customize Your Interface Colors")
        val subtitleWidth = textRenderer.getWidth(subtitleText)
        context.drawText(
            textRenderer,
            subtitleText,
            centerX - subtitleWidth / 2,
            contentY + 40,
            CinnamonTheme.secondaryTextColor,
            false
        )
        
        // Add glow effect around title
        val glowColor = 0x20ffffff
        context.fill(
            centerX - titleWidth / 2 - 2,
            contentY + 18,
            centerX + titleWidth / 2 + 2,
            contentY + 32,
            glowColor
        )
        
        // Render color list
        renderColorList(context, mouseX, mouseY)
        
        // Render color picker if open
        if (showColorPicker) {
            renderColorPicker(context, mouseX, mouseY)
        }
    }
    
    private fun renderColorList(context: DrawContext, mouseX: Int, mouseY: Int) {
        val listX = guiX + 40
        val listY = getContentY() + 80
        val listWidth = guiWidth - 80
        val listHeight = getContentHeight() - 160
        
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
        val pickerWidth = 280
        val pickerHeight = 320
        val pickerBg = 0xE0202020.toInt()
        
        // Semi-transparent overlay
        context.fill(0, 0, width, height, 0x80000000.toInt())
        
        // Picker background
        context.fill(pickerX, pickerY, pickerX + pickerWidth, pickerY + pickerHeight, pickerBg)
        context.drawBorder(pickerX, pickerY, pickerWidth, pickerHeight, CinnamonTheme.accentColor)
        
        // Title
        val title = selectedColorType?.displayName ?: "Color Picker"
        val titleWidth = textRenderer.getWidth(title)
        context.drawText(
            textRenderer,
            Text.literal(title),
            pickerX + (pickerWidth - titleWidth) / 2,
            pickerY + 10,
            CinnamonTheme.primaryTextColor,
            false
        )
        
        // Color wheel area
        val wheelSize = 180
        val wheelX = pickerX + (pickerWidth - wheelSize) / 2
        val wheelY = pickerY + 35
        
        renderColorWheel(context, wheelX, wheelY, wheelSize)
        
        // Brightness slider
        val sliderY = wheelY + wheelSize + 15
        renderBrightnessSlider(context, pickerX + 20, sliderY, pickerWidth - 40, 20)
        
        // Alpha slider
        val alphaY = sliderY + 30
        renderAlphaSlider(context, pickerX + 20, alphaY, pickerWidth - 40, 20)
        
        // Preview and hex input
        val previewY = alphaY + 35
        renderColorPreview(context, pickerX + 20, previewY, pickerWidth - 40, 25)
        
        // Buttons
        val buttonY = previewY + 35
        val buttonWidth = 80
        val buttonSpacing = 20
        val totalButtonWidth = buttonWidth * 2 + buttonSpacing
        val buttonStartX = pickerX + (pickerWidth - totalButtonWidth) / 2
        
        // Apply button
        val applyHovered = mouseX >= buttonStartX && mouseX < buttonStartX + buttonWidth && 
                          mouseY >= buttonY && mouseY < buttonY + 25
        context.fill(
            buttonStartX, buttonY, buttonStartX + buttonWidth, buttonY + 25,
            if (applyHovered) CinnamonTheme.accentColorHover else CinnamonTheme.accentColor
        )
        context.drawText(
            textRenderer,
            Text.literal("Apply"),
            buttonStartX + (buttonWidth - textRenderer.getWidth("Apply")) / 2,
            buttonY + 8,
            0xFFFFFFFF.toInt(),
            false
        )
        
        // Cancel button
        val cancelX = buttonStartX + buttonWidth + buttonSpacing
        val cancelHovered = mouseX >= cancelX && mouseX < cancelX + buttonWidth && 
                           mouseY >= buttonY && mouseY < buttonY + 25
        context.fill(
            cancelX, buttonY, cancelX + buttonWidth, buttonY + 25,
            if (cancelHovered) CinnamonTheme.buttonBackgroundHover else CinnamonTheme.buttonBackground
        )
        context.drawText(
            textRenderer,
            Text.literal("Cancel"),
            cancelX + (buttonWidth - textRenderer.getWidth("Cancel")) / 2,
            buttonY + 8,
            CinnamonTheme.primaryTextColor,
            false
        )
    }
    
    private fun renderColorWheel(context: DrawContext, x: Int, y: Int, size: Int) {
        val centerX = x + size / 2
        val centerY = y + size / 2
        val radius = size / 2 - 5
        
        // Simple color wheel representation using filled rectangles
        val steps = 32
        for (i in 0 until steps) {
            val angle = (i * 360.0 / steps).toFloat()
            val hueColor = hsvToRgb(angle, 1f, 1f)
            
            val x1 = centerX + (cos(Math.toRadians(angle.toDouble())) * radius * 0.7).toInt()
            val y1 = centerY + (sin(Math.toRadians(angle.toDouble())) * radius * 0.7).toInt()
            val x2 = centerX + (cos(Math.toRadians(angle.toDouble())) * radius).toInt()
            val y2 = centerY + (sin(Math.toRadians(angle.toDouble())) * radius).toInt()
            
            // Draw radial line
            context.drawHorizontalLine(minOf(x1, x2), maxOf(x1, x2), y1, hueColor)
        }
        
        // Center saturation/brightness area
        val innerSize = (radius * 0.6).toInt()
        context.fill(centerX - innerSize, centerY - innerSize, centerX + innerSize, centerY + innerSize, 
                    hsvToRgb(hue, saturation, brightness))
        
        // Selection indicator
        val selX = centerX + (cos(Math.toRadians(hue.toDouble())) * radius * saturation * 0.8).toInt()
        val selY = centerY + (sin(Math.toRadians(hue.toDouble())) * radius * saturation * 0.8).toInt()
        context.drawBorder(selX - 3, selY - 3, 6, 6, 0xFFFFFFFF.toInt())
    }
    
    private fun renderBrightnessSlider(context: DrawContext, x: Int, y: Int, width: Int, height: Int) {
        // Gradient background
        for (i in 0 until width) {
            val brightness = i.toFloat() / width
            val color = hsvToRgb(hue, saturation, brightness)
            context.fill(x + i, y, x + i + 1, y + height, color)
        }
        
        context.drawBorder(x, y, width, height, CinnamonTheme.borderColor)
        
        // Slider handle
        val handleX = x + (brightness * width).toInt() - 2
        context.fill(handleX, y - 2, handleX + 4, y + height + 2, 0xFFFFFFFF.toInt())
    }
    
    private fun renderAlphaSlider(context: DrawContext, x: Int, y: Int, width: Int, height: Int) {
        // Checkerboard background
        val checkerSize = 8
        for (i in 0 until width step checkerSize) {
            for (j in 0 until height step checkerSize) {
                val color = if ((i / checkerSize + j / checkerSize) % 2 == 0) 0xFFCCCCCC.toInt() else 0xFF999999.toInt()
                context.fill(x + i, y + j, x + i + checkerSize, y + j + checkerSize, color)
            }
        }
        
        // Alpha gradient
        val baseColor = hsvToRgb(hue, saturation, brightness)
        for (i in 0 until width) {
            val alpha = i.toFloat() / width
            val color = (baseColor and 0x00FFFFFF) or ((alpha * 255).toInt() shl 24)
            context.fill(x + i, y, x + i + 1, y + height, color)
        }
        
        context.drawBorder(x, y, width, height, CinnamonTheme.borderColor)
        
        // Slider handle
        val handleX = x + (alpha * width).toInt() - 2
        context.fill(handleX, y - 2, handleX + 4, y + height + 2, 0xFFFFFFFF.toInt())
    }
    
    private fun renderColorPreview(context: DrawContext, x: Int, y: Int, width: Int, height: Int) {
        val currentColor = hsvToRgb(hue, saturation, brightness)
        val finalColor = (currentColor and 0x00FFFFFF) or ((alpha * 255).toInt() shl 24)
        
        // Checkerboard background
        val checkerSize = 8
        for (i in 0 until width step checkerSize) {
            for (j in 0 until height step checkerSize) {
                val color = if ((i / checkerSize + j / checkerSize) % 2 == 0) 0xFFCCCCCC.toInt() else 0xFF999999.toInt()
                context.fill(x + i, y + j, x + i + checkerSize, y + j + checkerSize, color)
            }
        }
        
        context.fill(x, y, x + width, y + height, finalColor)
        context.drawBorder(x, y, width, height, CinnamonTheme.borderColor)
        
        // Hex value
        val hexValue = String.format("#%08X", finalColor)
        val textX = x + (width - textRenderer.getWidth(hexValue)) / 2
        val textY = y + (height - textRenderer.fontHeight) / 2
        context.drawText(textRenderer, Text.literal(hexValue), textX, textY, 0xFFFFFFFF.toInt(), true)
    }
    
    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (showColorPicker) {
            return handleColorPickerClick(mouseX.toInt(), mouseY.toInt(), button)
        }
        
        // Handle color list clicks
        val listX = guiX + 40
        val listY = getContentY() + 80
        val listWidth = guiWidth - 80
        val listHeight = getContentHeight() - 160
        
        if (mouseX >= listX && mouseX < listX + listWidth && mouseY >= listY && mouseY < listY + listHeight) {
            val colors = ColorType.values()
            val clickedIndex = ((mouseY - listY - 10 + scrollOffset) / itemHeight).toInt()
            
            if (clickedIndex >= 0 && clickedIndex < colors.size) {
                openColorPicker(colors[clickedIndex], mouseX.toInt(), mouseY.toInt())
                return true
            }
        }
        
        return super.mouseClicked(mouseX, mouseY, button)
    }
    
    private fun handleColorPickerClick(mouseX: Int, mouseY: Int, button: Int): Boolean {
        val pickerWidth = 280
        val pickerHeight = 320
        
        // Check if clicking outside picker to close
        if (mouseX < pickerX || mouseX >= pickerX + pickerWidth || mouseY < pickerY || mouseY >= pickerY + pickerHeight) {
            showColorPicker = false
            return true
        }
        
        // Handle button clicks
        val buttonY = pickerY + 275
        val buttonWidth = 80
        val buttonSpacing = 20
        val totalButtonWidth = buttonWidth * 2 + buttonSpacing
        val buttonStartX = pickerX + (pickerWidth - totalButtonWidth) / 2
        
        if (mouseY >= buttonY && mouseY < buttonY + 25) {
            if (mouseX >= buttonStartX && mouseX < buttonStartX + buttonWidth) {
                // Apply button
                applyColor()
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
        val pickerWidth = 280
        val wheelSize = 180
        val wheelX = pickerX + (pickerWidth - wheelSize) / 2
        val wheelY = pickerY + 35
        val centerX = wheelX + wheelSize / 2
        val centerY = wheelY + wheelSize / 2
        val radius = wheelSize / 2 - 5
        
        // Color wheel click
        if (mouseX >= wheelX && mouseX < wheelX + wheelSize && mouseY >= wheelY && mouseY < wheelY + wheelSize) {
            val dx = mouseX - centerX
            val dy = mouseY - centerY
            val distance = sqrt((dx * dx + dy * dy).toDouble()).toFloat()
            
            if (distance <= radius) {
                hue = (atan2(dy.toDouble(), dx.toDouble()) * 180 / PI).toFloat()
                if (hue < 0) hue += 360
                saturation = minOf(1f, distance / radius)
            }
        }
        
        // Brightness slider click
        val sliderY = wheelY + wheelSize + 15
        if (mouseX >= pickerX + 20 && mouseX < pickerX + pickerWidth - 20 && mouseY >= sliderY && mouseY < sliderY + 20) {
            brightness = ((mouseX - pickerX - 20).toFloat() / (pickerWidth - 40)).coerceIn(0f, 1f)
        }
        
        // Alpha slider click
        val alphaY = sliderY + 30
        if (mouseX >= pickerX + 20 && mouseX < pickerX + pickerWidth - 20 && mouseY >= alphaY && mouseY < alphaY + 20) {
            alpha = ((mouseX - pickerX - 20).toFloat() / (pickerWidth - 40)).coerceIn(0f, 1f)
        }
    }
    
    override fun mouseScrolled(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double): Boolean {
        if (!showColorPicker) {
            val listY = getContentY() + 80
            val listHeight = getContentHeight() - 160
            
            if (mouseY >= listY && mouseY < listY + listHeight) {
                scrollOffset = (scrollOffset - verticalAmount.toInt() * 20).coerceAtLeast(0)
                return true
            }
        }
        
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)
    }
    
    private fun openColorPicker(colorType: ColorType, x: Int, y: Int) {
        selectedColorType = colorType
        showColorPicker = true
        
        // Position picker near click but keep it on screen
        val pickerWidth = 280
        val pickerHeight = 320
        pickerX = (x - pickerWidth / 2).coerceIn(50, width - pickerWidth - 50)
        pickerY = (y - pickerHeight / 2).coerceIn(50, height - pickerHeight - 50)
        
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
            colorType.setter(colorWithAlpha)
        }
    }
    
    private fun resetToDefaults() {
        // Reset all colors to their default values
        // This would restore the original CinnamonTheme values
    }
    
    private fun saveTheme() {
        // Save the current theme configuration to file
        // This would persist the theme changes
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
}