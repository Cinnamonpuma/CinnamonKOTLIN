// NoFallMixin.kt - Simple mixin to prevent fall damage
package code.cinnamon.mixins

import code.cinnamon.modules.ModuleManager
import net.minecraft.entity.player.PlayerEntity
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(PlayerEntity::class)
class NoFallMixin {

    /**
     * Simple approach: Reset fall distance every tick
     * This prevents any fall damage from accumulating
     */
    @Inject(method = ["tick"], at = [At("TAIL")])
    private fun onTick(ci: CallbackInfo) {
        val noFallModule = ModuleManager.getModule("NoFall")
        
        if (noFallModule != null && noFallModule.isEnabled) {
            val player = this as PlayerEntity
            // Reset fall distance to 0 if it's greater than 0
            if (player.fallDistance > 0.0) {
                player.fallDistance = 0.0
            }
        }
    }
}