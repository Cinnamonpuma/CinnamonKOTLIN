package code.cinnamon.gui.screens

import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text
import code.cinnamon.gui.CinnamonScreen
import code.cinnamon.gui.CinnamonGuiManager
import code.cinnamon.gui.components.CinnamonButton
import code.cinnamon.gui.theme.CinnamonTheme

class MainMenuScreen : CinnamonScreen(Text.literal("Cinnamon Client")) {
    
    override fun initializeComponents() {
        val centerX = guiX + guiWidth / 2
        val contentY = getContentY()
        val buttonWidth = 180
        val buttonHeight = CinnamonTheme.BUTTON_HEIGHT_LARGE
        val spacing = 45
        
        // Calculate starting Y position to center buttons vertically (now with 5 buttons)
        val totalButtonsHeight = (buttonHeight * 5) + (spacing * 4)
        val startY = contentY + (getContentHeight() - totalButtonsHeight) / 2
        
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
        
        // New Theme Manager button
        addButton(CinnamonButton(
            centerX - buttonWidth / 2,
            startY + spacing * 3,
            buttonWidth,
            buttonHeight,
            Text.literal("Theme Manager"),
            { _, _ -> CinnamonGuiManager.openThemeManagerScreen() }
        ))
        
        addButton(CinnamonButton(
            centerX - buttonWidth / 2,
            startY + spacing * 4,
            buttonWidth,
            buttonHeight,
            Text.literal("Close"),
            { _, _ -> CinnamonGuiManager.closeCurrentScreen() }
        ))
    }
    
    override fun renderContent(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        val centerX = guiX + guiWidth / 2
        val contentY = getContentY()
        
        // Draw logo/title area
        val logoText = Text.literal("CINNAMON")
        val logoWidth = textRenderer.getWidth(logoText)
        context.drawText(
            textRenderer,
            logoText,
            centerX - logoWidth / 2,
            contentY + 20,
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
            contentY + 40,
            CinnamonTheme.secondaryTextColor,
            false
        )
        
        // Add a subtle glow effect around the logo
        val glowColor = 0x20ffffff
        context.fill(
            centerX - logoWidth / 2 - 2,
            contentY + 18,
            centerX + logoWidth / 2 + 2,
            contentY + 32,
            glowColor
        )
    }
    
    override fun renderFooter(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        super.renderFooter(context, mouseX, mouseY, delta)
        
        // Draw version info in footer area
        val versionText = Text.literal("v1.0.0 - Minecraft 1.21.5")
        val versionWidth = textRenderer.getWidth(versionText)
        context.drawText(
            textRenderer,
            versionText,
            guiX + guiWidth - versionWidth - PADDING,
            getFooterY() + (FOOTER_HEIGHT - textRenderer.fontHeight) / 2,
            CinnamonTheme.secondaryTextColor,
            false
        )
        
        // Draw status indicator
        val statusText = Text.literal("Ready")
        context.drawText(
            textRenderer,
            statusText,
            guiX + PADDING,
            getFooterY() + (FOOTER_HEIGHT - textRenderer.fontHeight) / 2,
            CinnamonTheme.successColor,
            false
        )
    }
}