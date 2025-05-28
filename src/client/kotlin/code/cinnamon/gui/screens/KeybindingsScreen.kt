package code.cinnamon.gui.screens

import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text
import code.cinnamon.gui.CinnamonScreen
import code.cinnamon.gui.components.CinnamonButton

class KeybindingsScreen : CinnamonScreen(Text.literal("Keybindings")) {
    
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
        
        // Reset button
        addButton(CinnamonButton(
            width - PADDING - 80,
            height - FOOTER_HEIGHT + 10,
            80,
            20,
            Text.literal("Reset"),
            { _, _ -> /* TODO: Reset keybindings to default */ }
        ))
    }
    
    override fun renderContent(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        val contentY = HEADER_HEIGHT + PADDING
        val contentHeight = height - HEADER_HEIGHT - FOOTER_HEIGHT - (PADDING * 2)
        
        // Keybindings list background
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
            Text.literal("Key Bindings"),
            PADDING + 10,
            contentY + 10,
            theme.primaryTextColor,
            false
        )
        
        // TODO: Render keybinding list here
        // This would typically iterate through KeybindingManager.keybindings
    }
}