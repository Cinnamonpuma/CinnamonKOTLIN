package code.cinnamon.keybindings

import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import org.lwjgl.glfw.GLFW

object KeybindingManager {
    private val keybindings = mutableMapOf<String, KeyBinding>()
    
    fun registerKeybinding(name: String, key: Int, category: String = "Cinnamon"): KeyBinding {
        val keyBinding = KeyBindingHelper.registerKeyBinding(
            KeyBinding(
                name,
                InputUtil.Type.KEYSYM,
                key,
                category
            )
        )
        keybindings[name] = keyBinding
        return keyBinding
    }
    
    fun getKeybinding(name: String): KeyBinding? {
        return keybindings[name]
    }
    
    fun getAllKeybindings(): Map<String, KeyBinding> = keybindings.toMap()
    
    fun isPressed(name: String): Boolean {
        return keybindings[name]?.isPressed ?: false
    }
    
    fun wasPressed(name: String): Boolean {
        return keybindings[name]?.wasPressed() ?: false
    }
    
    fun initialize() {
        // Register default keybindings here
        registerKeybinding("cinnamon.open_gui", GLFW.GLFW_KEY_RIGHT_SHIFT)
        registerKeybinding("cinnamon.toggle_speed", GLFW.GLFW_KEY_V)
        registerKeybinding("cinnamon.toggle_flight", GLFW.GLFW_KEY_F)
        registerKeybinding("cinnamon.toggle_nofall", GLFW.GLFW_KEY_N)
    }
}