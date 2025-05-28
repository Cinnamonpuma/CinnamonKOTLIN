package code.cinnamon.gui.screens

import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text
import net.minecraft.client.util.InputUtil
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import org.lwjgl.glfw.GLFW
import code.cinnamon.gui.CinnamonScreen
import code.cinnamon.gui.CinnamonGuiManager
import code.cinnamon.gui.components.CinnamonButton
import code.cinnamon.gui.theme.CinnamonTheme
import code.cinnamon.keybindings.KeybindingManager
import kotlin.math.max
import kotlin.math.min

class KeybindingsScreen : CinnamonScreen(Text.literal("Keybindings")) {
    private var selectedKeybinding: String? = null
    private var isListening = false
    private var scrollOffset = 0
    private val keybindingHeight = 45
    private val keybindingSpacing = 5
    private val maxScrollOffset get() = max(0, getKeybindingEntries().size * (keybindingHeight + keybindingSpacing) - getKeybindingListHeight())
    
    data class KeybindingEntry(
        val name: String,
        val displayName: String,
        val description: String,
        val currentKey: Int
    )
    
    override fun initializeComponents() {
        // Back button
        addButton(CinnamonButton(
            guiX + PADDING,
            getFooterY() + 8,
            60,
            CinnamonTheme.BUTTON_HEIGHT_SMALL,
            Text.literal("Back"),
            { _, _ -> CinnamonGuiManager.openMainMenu() }
        ))
        
        // Reset All button
        addButton(CinnamonButton(
            guiX + guiWidth - PADDING - 80,
            getFooterY() + 8,
            80,
            CinnamonTheme.BUTTON_HEIGHT_SMALL,
            Text.literal("Reset All"),
            { _, _ -> resetAllKeybindings() }
        ))
        
        // Save button
        addButton(CinnamonButton(
            guiX + guiWidth - PADDING - 170,
            getFooterY() + 8,
            80,
            CinnamonTheme.BUTTON_HEIGHT_SMALL,
            Text.literal("Save"),
            { _, _ -> saveKeybindings() },
            true
        ))
    }
    
    override fun renderContent(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        val contentX = getContentX()
        val contentY = getContentY()
        val contentWidth = getContentWidth()
        val contentHeight = getContentHeight()
        
        // Content background
        context.fill(
            contentX,
            contentY,
            contentX + contentWidth,
            contentY + contentHeight,
            CinnamonTheme.contentBackground
        )
        
        // Header section
        val headerHeight = 50
        context.fill(
            contentX,
            contentY,
            contentX + contentWidth,
            contentY + headerHeight,
            CinnamonTheme.cardBackground
        )
        
        // Title
        context.drawText(
            textRenderer,
            Text.literal("Key Bindings"),
            contentX + 15,
            contentY + 12,
            CinnamonTheme.titleColor,
            true
        )
        
        // Subtitle
        context.drawText(
            textRenderer,
            Text.literal("Click on a keybinding to change it"),
            contentX + 15,
            contentY + 28,
            CinnamonTheme.secondaryTextColor,
            false
        )
        
        // Header border
        context.fill(
            contentX,
            contentY + headerHeight - 1,
            contentX + contentWidth,
            contentY + headerHeight,
            CinnamonTheme.borderColor
        )
        
        // Listening indicator
        if (isListening && selectedKeybinding != null) {
            val indicatorText = "Press a key to bind to ${getDisplayName(selectedKeybinding!!)} (ESC to cancel)"
            val indicatorWidth = textRenderer.getWidth(indicatorText)
            val indicatorX = contentX + (contentWidth - indicatorWidth) / 2
            val indicatorY = contentY + headerHeight + 10
            
            // Background
            context.fill(
                indicatorX - 10,
                indicatorY - 5,
                indicatorX + indicatorWidth + 10,
                indicatorY + textRenderer.fontHeight + 5,
                CinnamonTheme.warningColor
            )
            
            context.drawText(
                textRenderer,
                Text.literal(indicatorText),
                indicatorX,
                indicatorY,
                CinnamonTheme.titleColor,
                true
            )
        }
        
        // Keybinding list
        val listY = contentY + headerHeight + (if (isListening) 35 else 10)
        val listHeight = getKeybindingListHeight() - (if (isListening) 45 else 10)
        
        renderKeybindingList(context, contentX + 10, listY, contentWidth - 20, listHeight, mouseX, mouseY)
        
        // Scroll indicator
        if (maxScrollOffset > 0) {
            renderScrollbar(context, contentX + contentWidth - 8, listY, 6, listHeight)
        }
    }
    
    private fun renderKeybindingList(context: DrawContext, x: Int, y: Int, width: Int, height: Int, mouseX: Int, mouseY: Int) {
        val entries = getKeybindingEntries()
        
        // Enable clipping
        context.enableScissor(x, y, x + width, y + height)
        
        entries.forEachIndexed { index, entry ->
            val entryY = y - scrollOffset + index * (keybindingHeight + keybindingSpacing)
            
            // Only render if visible
            if (entryY + keybindingHeight >= y && entryY <= y + height) {
                renderKeybindingEntry(context, x, entryY, width, keybindingHeight, entry, mouseX, mouseY)
            }
        }
        
        context.disableScissor()
    }
    
    private fun renderKeybindingEntry(context: DrawContext, x: Int, y: Int, width: Int, height: Int, entry: KeybindingEntry, mouseX: Int, mouseY: Int) {
        val isHovered = mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height
        val isSelected = selectedKeybinding == entry.name
        val isListeningToThis = isListening && isSelected
        
        val backgroundColor = when {
            isListeningToThis -> CinnamonTheme.warningColor
            isSelected -> CinnamonTheme.accentColor
            isHovered -> CinnamonTheme.cardBackgroundHover
            else -> CinnamonTheme.cardBackground
        }
        
        // Entry background
        drawRoundedRect(context, x, y, width, height, backgroundColor)
        
        // Entry border
        val borderColor = when {
            isListeningToThis -> CinnamonTheme.titleColor
            isSelected -> CinnamonTheme.accentColor
            else -> CinnamonTheme.borderColor
        }
        drawRoundedBorder(context, x, y, width, height, borderColor)
        
        // Keybinding name
        val nameColor = if (isListeningToThis) CinnamonTheme.titleColor else CinnamonTheme.primaryTextColor
        context.drawText(
            textRenderer,
            Text.literal(entry.displayName),
            x + 12,
            y + 8,
            nameColor,
            true
        )
        
        // Keybinding description
        val descColor = if (isListeningToThis) CinnamonTheme.titleColor else CinnamonTheme.secondaryTextColor
        context.drawText(
            textRenderer,
            Text.literal(entry.description),
            x + 12,
            y + 22,
            descColor,
            false
        )
        
        // Current key display
        val keyName = getKeyName(entry.currentKey)
        val keyWidth = textRenderer.getWidth(keyName)
        val keyButtonWidth = maxOf(keyWidth + 20, 60)
        val keyButtonX = x + width - keyButtonWidth - 10
        val keyButtonY = y + (height - 24) / 2
        
        // Key button background
        val keyButtonBg = when {
            isListeningToThis -> CinnamonTheme.primaryButtonBackgroundPressed
            isHovered && mouseX >= keyButtonX && mouseX < keyButtonX + keyButtonWidth -> CinnamonTheme.buttonBackgroundHover
            else -> CinnamonTheme.buttonBackground
        }
        
        drawRoundedRect(context, keyButtonX, keyButtonY, keyButtonWidth, 24, keyButtonBg)
        drawRoundedBorder(context, keyButtonX, keyButtonY, keyButtonWidth, 24, CinnamonTheme.borderColor)
        
        // Key text
        val keyTextColor = if (isListeningToThis) CinnamonTheme.titleColor else CinnamonTheme.primaryTextColor
        context.drawText(
            textRenderer,
            Text.literal(keyName),
            keyButtonX + (keyButtonWidth - keyWidth) / 2,
            keyButtonY + 8,
            keyTextColor,
            false
        )
        
        // Conflict indicator (if key is used by multiple bindings)
        if (hasConflict(entry)) {
            context.fill(x + width - 25, y + 5, x + width - 5, y + 10, CinnamonTheme.errorColor)
        }
    }
    
    private fun renderScrollbar(context: DrawContext, x: Int, y: Int, width: Int, height: Int) {
        // Scrollbar track
        context.fill(x, y, x + width, y + height, CinnamonTheme.borderColor)
        
        if (maxScrollOffset > 0) {
            // Scrollbar thumb
            val thumbHeight = max(20, (height * height) / (maxScrollOffset + height))
            val thumbY = y + (scrollOffset * (height - thumbHeight)) / maxScrollOffset
            
            context.fill(x + 1, thumbY.toInt(), x + width - 1, thumbY.toInt() + thumbHeight, CinnamonTheme.accentColor)
        }
    }
    
    private fun drawRoundedRect(context: DrawContext, x: Int, y: Int, width: Int, height: Int, color: Int) {
        context.fill(x + 2, y, x + width - 2, y + height, color)
        context.fill(x, y + 2, x + width, y + height - 2, color)
        context.fill(x + 1, y + 1, x + 2, y + 2, color)
        context.fill(x + width - 2, y + 1, x + width - 1, y + 2, color)
        context.fill(x + 1, y + height - 2, x + 2, y + height - 1, color)
        context.fill(x + width - 2, y + height - 2, x + width - 1, y + height - 1, color)
    }
    
    private fun drawRoundedBorder(context: DrawContext, x: Int, y: Int, width: Int, height: Int, color: Int) {
        context.fill(x + 2, y, x + width - 2, y + 1, color)
        context.fill(x + 2, y + height - 1, x + width - 2, y + height, color)
        context.fill(x, y + 2, x + 1, y + height - 2, color)
        context.fill(x + width - 1, y + 2, x + width, y + height - 2, color)
        context.fill(x + 1, y + 1, x + 2, y + 2, color)
        context.fill(x + width - 2, y + 1, x + width - 1, y + 2, color)
        context.fill(x + 1, y + height - 2, x + 2, y + height - 1, color)
        context.fill(x + width - 2, y + height - 2, x + width - 1, y + height - 1, color)
    }
    
    private fun getKeybindingEntries(): List<KeybindingEntry> {
        val keybindings = KeybindingManager.getAllKeybindings()
        return keybindings.map { (name, keyBinding) ->
            KeybindingEntry(
                name = name,
                displayName = getDisplayName(name),
                description = getDescription(name),
                currentKey = KeyBindingHelper.getBoundKeyOf(keyBinding).code
            )
        }
    }
    
    private fun getDisplayName(name: String): String {
        return when (name) {
            "cinnamon.toggle_speed" -> "Toggle Speed"
            "cinnamon.toggle_flight" -> "Toggle Flight"
            "cinnamon.toggle_nofall" -> "Toggle No Fall"
            else -> name.replace("cinnamon.", "").replace("_", " ").replaceFirstChar { it.uppercase() }
        }
    }
    
    private fun getDescription(name: String): String {
        return when (name) {
            "cinnamon.toggle_speed" -> "Toggles the speed module on/off"
            "cinnamon.toggle_flight" -> "Toggles the flight module on/off"
            "cinnamon.toggle_nofall" -> "Toggles the no fall damage module on/off"
            else -> "Custom keybinding"
        }
    }
    
    private fun getKeyName(keyCode: Int): String {
        return when (keyCode) {
            GLFW.GLFW_KEY_UNKNOWN -> "None"
            else -> InputUtil.fromKeyCode(keyCode, 0).localizedText.string
        }
    }
    
    private fun hasConflict(entry: KeybindingEntry): Boolean {
        val entries = getKeybindingEntries()
        return entries.count { it.currentKey == entry.currentKey && it.currentKey != GLFW.GLFW_KEY_UNKNOWN } > 1
    }
    
    private fun getKeybindingListHeight(): Int {
        return getContentHeight() - 60 // Account for header and padding
    }
    
    private fun resetAllKeybindings() {
        // Reset to default values
        KeybindingManager.updateKeybinding("cinnamon.toggle_speed", GLFW.GLFW_KEY_V)
        KeybindingManager.updateKeybinding("cinnamon.toggle_flight", GLFW.GLFW_KEY_F)
        KeybindingManager.updateKeybinding("cinnamon.toggle_nofall", GLFW.GLFW_KEY_N)
        
        selectedKeybinding = null
        isListening = false
    }
    
    private fun saveKeybindings() {
        // Here you would typically save to a config file
        // For now, just close the listening state
        selectedKeybinding = null
        isListening = false
    }
    
    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (isListening) {
            // Cancel listening
            isListening = false
            selectedKeybinding = null
            return true
        }
        
        val contentX = getContentX()
        val contentY = getContentY()
        val contentWidth = getContentWidth()
        val listY = contentY + 60
        val listHeight = getKeybindingListHeight() - 10
        
        if (mouseX >= contentX + 10 && mouseX < contentX + contentWidth - 10 &&
            mouseY >= listY && mouseY < listY + listHeight) {
            
            val entries = getKeybindingEntries()
            entries.forEachIndexed { index, entry ->
                val entryY = listY - scrollOffset + index * (keybindingHeight + keybindingSpacing)
                
                if (mouseY >= entryY && mouseY < entryY + keybindingHeight) { // Fixed: Added missing condition and return
                    selectedKeybinding = entry.name
                    isListening = true
                    return true
                }
            }
        }
        
        return super.mouseClicked(mouseX, mouseY, button)
    }
    
    // Additional methods that might be missing - you can add these if needed
    override fun mouseScrolled(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double): Boolean {
        val contentX = getContentX()
        val contentY = getContentY() + 60
        val contentWidth = getContentWidth()
        val contentHeight = getContentHeight() - 70
        
        if (mouseX >= contentX && mouseX < contentX + contentWidth &&
            mouseY >= contentY && mouseY < contentY + contentHeight) {
            
            val scrollAmount = (verticalAmount * 20).toInt()
            scrollOffset = max(0, min(maxScrollOffset, scrollOffset - scrollAmount))
            return true
        }
        
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)
    }
    
    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if (isListening && selectedKeybinding != null) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                // Cancel listening
                isListening = false
                selectedKeybinding = null
            } else {
                // Set the new keybinding
                KeybindingManager.updateKeybinding(selectedKeybinding!!, keyCode)
                isListening = false
                selectedKeybinding = null
            }
            return true
        }
        
        return super.keyPressed(keyCode, scanCode, modifiers)
    }
}