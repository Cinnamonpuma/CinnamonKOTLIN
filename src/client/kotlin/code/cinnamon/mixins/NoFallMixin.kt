// NoFallMixin.kt - Mixin to intercept fall damage
package code.cinnamon.mixins

import code.cinnamon.modules.ModuleManager
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

@Mixin(PlayerEntity::class)
class NoFallMixin {

    /**
     * Alternative approach: Intercept the fall distance update
     * This prevents fall distance from accumulating
     */
    @Inject(method = ["updateFallState"], at = [At("HEAD")], cancellable = true)
    private fun onUpdateFallState(y: Double, onGround: Boolean, state: BlockState, pos: BlockPos, ci: CallbackInfo) {
        val noFallModule = ModuleManager.getModule("NoFall")
        
        if (noFallModule != null && noFallModule.isEnabled) {
            // Force onGround to be true to prevent fall distance accumulation
            val player = this as PlayerEntity
            if (player.fallDistance > 2.0) {
                // Cancel the original method and reset fall distance
                ci.cancel()
                player.fallDistance = 0.0
                println("NoFall: Reset fall distance")
            }
        }
    }
}