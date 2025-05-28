package code.cinnamon.gui.screens

import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text
import code.cinnamon.gui.CinnamonScreen
import code.cinnamon.gui.CinnamonGuiManager
import code.cinnamon.gui.components.CinnamonButton
import code.cinnamon.gui.theme.CinnamonTheme

class SettingsScreen : CinnamonScreen(Text.literal("Settings")) {
    
    override fun initializeComponents() {
        val centerX = guiX + guiWidth / 2
        val contentY = getContentY()
        val buttonWidth = 160
        val buttonHeight = CinnamonTheme.BUTTON_HEIGHT
        val spacing = 35
        
        // Calculate starting Y position for settings sections
        val totalButtonsHeight = (buttonHeight * 5) + (spacing * 4)
        val startY = contentY + 80 // Leave space for title area
        
        // Settings category buttons
        addButton(CinnamonButton(
            centerX - buttonWidth / 2,
            startY,
            buttonWidth,
            buttonHeight,
            Text.literal("General"),
            { _, _ -> /* TODO: Open general settings */ },
            true
        ))
        
        addButton(CinnamonButton(
            centerX - buttonWidth / 2,
            startY + spacing,
            buttonWidth,
            buttonHeight,
            Text.literal("Performance"),
            { _, _ -> /* TODO: Open performance settings */ }
        ))
        
        addButton(CinnamonButton(
            centerX - buttonWidth / 2,
            startY + spacing * 2,
            buttonWidth,
            buttonHeight,
            Text.literal("Interface"),
            { _, _ -> /* TODO: Open interface settings */ }
        ))
        
        addButton(CinnamonButton(
            centerX - buttonWidth / 2,
            startY + spacing * 3,
            buttonWidth,
            buttonHeight,
            Text.literal("Advanced"),
            { _, _ -> /* TODO: Open advanced settings */ }
        ))
        
        // Back button at bottom
        addButton(CinnamonButton(
            centerX - buttonWidth / 2,
            startY + spacing * 4,
            buttonWidth,
            buttonHeight,
            Text.literal("Back"),
            { _, _ -> CinnamonGuiManager.closeCurrentScreen() }
        ))
    }
    
    override fun renderContent(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        val centerX = guiX + guiWidth / 2
        val contentY = getContentY()
        
        // Draw title area
        val titleText = Text.literal("SETTINGS")
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
        val subtitleText = Text.literal("Configure Client Preferences")
        val subtitleWidth = textRenderer.getWidth(subtitleText)
        context.drawText(
            textRenderer,
            subtitleText,
            centerX - subtitleWidth / 2,
            contentY + 40,
            CinnamonTheme.secondaryTextColor,
            false
        )
        
        // Add a subtle glow effect around the title
        val glowColor = 0x20ffffff
        context.fill(
            centerX - titleWidth / 2 - 2,
            contentY + 18,
            centerX + titleWidth / 2 + 2,
            contentY + 32,
            glowColor
        )
        
        // Draw settings info text
        val infoText = Text.literal("Select a category to configure")
        val infoWidth = textRenderer.getWidth(infoText)
        context.drawText(
            textRenderer,
            infoText,
            centerX - infoWidth / 2,
            contentY + 65,
            CinnamonTheme.secondaryTextColor,
            false
        )
    }
    
    override fun renderFooter(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        super.renderFooter(context, mouseX, mouseY, delta)
        
        // Draw configuration status in footer area
        val configText = Text.literal("Configuration Status")
        context.drawText(
            textRenderer,
            configText,
            guiX + PADDING,
            getFooterY() + (FOOTER_HEIGHT - textRenderer.fontHeight) / 2,
            CinnamonTheme.infoColor,
            false
        )
        
        // Draw save status indicator
        val saveText = Text.literal("Auto-Save: Enabled")
        val saveWidth = textRenderer.getWidth(saveText)
        context.drawText(
            textRenderer,
            saveText,
            guiX + guiWidth - saveWidth - PADDING,
            getFooterY() + (FOOTER_HEIGHT - textRenderer.fontHeight) / 2,
            CinnamonTheme.successColor,
            false
        )
    }
}