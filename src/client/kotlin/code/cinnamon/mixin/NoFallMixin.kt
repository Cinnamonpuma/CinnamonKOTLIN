// NoFallMixin.kt - Ultra-safe approach for MC 1.21.5
package code.cinnamon.mixin

import code.cinnamon.modules.ModuleManager
import net.minecraft.entity.player.PlayerEntity
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(PlayerEntity::class)
class NoFallMixin {

    /**
     * Ultra-safe approach: Just reset fall distance every tick
     * This prevents any fall damage from accumulating
     */
    @Inject(method = ["tick"], at = [At("HEAD")])
    private fun onTick(ci: CallbackInfo) {
        val noFallModule = ModuleManager.getModule("NoFall")
        
        if (noFallModule != null && noFallModule.isEnabled) {
            val player = this as PlayerEntity
            // Continuously reset fall distance to prevent damage
            player.fallDistance = 0.0
        }
    }
}