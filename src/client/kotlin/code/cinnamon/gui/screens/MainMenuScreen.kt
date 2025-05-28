package code.cinnamon.gui.screens

import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text
import code.cinnamon.gui.CinnamonScreen
import code.cinnamon.gui.CinnamonGuiManager
import code.cinnamon.gui.components.CinnamonButton
import code.cinnamon.gui.theme.CinnamonTheme

class MainMenuScreen : CinnamonScreen(Text.literal("Cinnamon Client")) {
    
    override fun initializeComponents() {
        val centerX = width / 2
        val startY = HEADER_HEIGHT + 60
        val buttonWidth = 200
        val buttonHeight = CinnamonTheme.BUTTON_HEIGHT_LARGE
        val spacing = 50
        
        // Main navigation buttons
        addButton(CinnamonButton(
            centerX - buttonWidth / 2,
            startY,
            buttonWidth,
            buttonHeight,
            Text.literal("Modules"),
            { _, _ -> CinnamonGuiManager.openModulesScreen() },
            true
        ))
        
        addButton(CinnamonButton(
            centerX - buttonWidth / 2,
            startY + spacing,
            buttonWidth,
            buttonHeight,
            Text.literal("Keybindings"),
            { _, _ -> CinnamonGuiManager.openKeybindingsScreen() }
        ))
        
        addButton(CinnamonButton(
            centerX - buttonWidth / 2,
            startY + spacing * 2,
            buttonWidth,
            buttonHeight,
            Text.literal("Settings"),
            { _, _ -> CinnamonGuiManager.openSettingsScreen() }
        ))
        
        addButton(CinnamonButton(
            centerX - buttonWidth / 2,
            startY + spacing * 3,
            buttonWidth,
            buttonHeight,
            Text.literal("Close"),
            { _, _ -> CinnamonGuiManager.closeCurrentScreen() }
        ))
    }
    
    override fun renderContent(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        val centerX = width / 2
        val logoY = HEADER_HEIGHT + 20
        
        // Draw logo/title area
        val logoText = Text.literal("CINNAMON")
        val logoWidth = textRenderer.getWidth(logoText)
        context.drawText(
            textRenderer,
            logoText,
            centerX - logoWidth / 2,
            logoY,
            CinnamonTheme.accentColor,
            true
        )
        
        // Draw subtitle
        val subtitleText = Text.literal("Advanced Minecraft Client")
        val subtitleWidth = textRenderer.getWidth(subtitleText)
        context.drawText(
            textRenderer,
            subtitleText,
            centerX - subtitleWidth / 2,
            logoY + 20,
            CinnamonTheme.secondaryTextColor,
            false
        )
        
        // Draw version info in footer area
        val versionText = Text.literal("v1.0.0 - Minecraft 1.21.5")
        val versionWidth = textRenderer.getWidth(versionText)
        context.drawText(
            textRenderer,
            versionText,
            width - versionWidth - PADDING,
            height - FOOTER_HEIGHT + 15,
            CinnamonTheme.secondaryTextColor,
            false
        )
    }
    
    override fun shouldCloseOnEsc(): Boolean {
        return true
    }
}