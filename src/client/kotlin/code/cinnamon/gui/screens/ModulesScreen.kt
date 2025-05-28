package code.cinnamon.gui.screens

import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text
import code.cinnamon.gui.CinnamonScreen
import code.cinnamon.gui.components.CinnamonButton

class ModulesScreen : CinnamonScreen(Text.literal("Modules")) {
    
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
        
        // Add more module-specific buttons here
    }
    
    override fun renderContent(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        val contentY = HEADER_HEIGHT + PADDING
        val contentHeight = height - HEADER_HEIGHT - FOOTER_HEIGHT - (PADDING * 2)
        
        // Module list background
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
            Text.literal("Available Modules"),
            PADDING + 10,
            contentY + 10,
            theme.primaryTextColor,
            false
        )
        
        // TODO: Render module list here
        // This would typically iterate through ModuleManager.modules
    }
}