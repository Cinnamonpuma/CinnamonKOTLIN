package code.cinnamon.gui

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import code.cinnamon.gui.components.CinnamonButton
import code.cinnamon.gui.theme.CinnamonTheme

abstract class CinnamonScreen(title: Text) : Screen(title) {
    protected val theme = CinnamonTheme
    protected val buttons = mutableListOf<CinnamonButton>()
    
    companion object {
        const val HEADER_HEIGHT = 60
        const val FOOTER_HEIGHT = 40
        const val SIDEBAR_WIDTH = 200
        const val PADDING = 20
    }
    
    override fun init() {
        super.init()
        buttons.clear()
        initializeComponents()
    }
    
    abstract fun initializeComponents()
    
    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        // Background
        renderBackground(context, mouseX, mouseY, delta)
        
        // Header
        renderHeader(context, mouseX, mouseY, delta)
        
        // Main content area
        renderContent(context, mouseX, mouseY, delta)
        
        // Footer
        renderFooter(context, mouseX, mouseY, delta)
        
        // Render buttons
        buttons.forEach { button ->
            button.render(context, mouseX, mouseY, delta)
        }
        
        super.render(context, mouseX, mouseY, delta)
    }
    
    override fun renderBackground(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        // Dark gradient background similar to Lunar Client
        context.fillGradient(0, 0, width, height, theme.backgroundTop, theme.backgroundBottom)
        
        // Subtle pattern overlay
        renderPatternOverlay(context)
    }
    
    protected open fun renderHeader(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        // Header background
        context.fill(0, 0, width, HEADER_HEIGHT, theme.headerBackground)
        
        // Title
        val titleWidth = textRenderer.getWidth(title)
        context.drawText(
            textRenderer,
            title,
            (width - titleWidth) / 2,
            (HEADER_HEIGHT - textRenderer.fontHeight) / 2,
            theme.titleColor,
            true
        )
        
        // Header border
        context.fill(0, HEADER_HEIGHT - 1, width, HEADER_HEIGHT, theme.borderColor)
    }
    
    protected abstract fun renderContent(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float)
    
    protected open fun renderFooter(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        val footerY = height - FOOTER_HEIGHT
        
        // Footer background
        context.fill(0, footerY, width, height, theme.footerBackground)
        
        // Footer border
        context.fill(0, footerY, width, footerY + 1, theme.borderColor)
    }
    
    private fun renderPatternOverlay(context: DrawContext) {
        // Subtle dot pattern for texture
        for (x in 0 until width step 20) {
            for (y in 0 until height step 20) {
                context.fill(x, y, x + 1, y + 1, theme.patternColor)
            }
        }
    }
    
    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        buttons.forEach { btn ->
            if (btn.isMouseOver(mouseX, mouseY)) {
                btn.onClick(mouseX, mouseY)
                return true
            }
        }
        return super.mouseClicked(mouseX, mouseY, button)
    }
    
    override fun mouseMoved(mouseX: Double, mouseY: Double) {
        buttons.forEach { btn ->
            btn.setHovered(btn.isMouseOver(mouseX, mouseY))
        }
        super.mouseMoved(mouseX, mouseY)
    }
    
    protected fun addButton(button: CinnamonButton) {
        buttons.add(button)
    }
}