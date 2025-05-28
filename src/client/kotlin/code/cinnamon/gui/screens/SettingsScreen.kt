package code.cinnamon.gui.screens

import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text
import code.cinnamon.gui.CinnamonScreen
import code.cinnamon.gui.components.CinnamonButton

class SettingsScreen : CinnamonScreen(Text.literal("Settings")) {
    
    override fun initializeComponents() {
        // Back button
        addButton(CinnamonButton(
            PADDING,
            height - FOOTER_HEIGHT + 10,
            80,
            20,
            Text.literal("Back"),
            { _, _ -> close() }
        ))
        
        // Save button
        addButton(CinnamonButton(
            width - PADDING - 80,
            height - FOOTER_HEIGHT + 10,
            80,
            20,
            Text.literal("Save"),
            { _, _ -> /* TODO: Save settings */ }
        ))
    }
    
    override fun renderContent(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        val contentY = HEADER_HEIGHT + PADDING
        val contentHeight = height - HEADER_HEIGHT - FOOTER_HEIGHT - (PADDING * 2)
        
        // Settings panel background
        context.fill(
            PADDING,
            contentY,
            width - PADDING,
            contentY + contentHeight,
            theme.contentBackground
        )
        
        // Title
        context.drawText(
            textRenderer,
            Text.literal("Client Settings"),
            PADDING + 10,
            contentY + 10,
            theme.primaryTextColor,
            false
        )
        
        // TODO: Render settings controls here
        // This would include toggles, sliders, dropdowns, etc.
    }
}