// NoFallMixin.kt - Simple mixin to prevent fall damage for MC 1.21.5
package code.cinnamon.mixins

import code.cinnamon.modules.ModuleManager
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.damage.DamageSource
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

@Mixin(PlayerEntity::class)
class NoFallMixin {

    /**
     * Most reliable approach for 1.21.5: Directly prevent fall damage
     */
    @Inject(method = ["damage"], at = [At("HEAD")], cancellable = true)
    private fun onDamage(source: DamageSource, amount: Float, cir: CallbackInfoReturnable<Boolean>) {
        val noFallModule = ModuleManager.getModule("NoFall")
        
        if (noFallModule != null && noFallModule.isEnabled) {
            // Check if this is fall damage and cancel it
            if (source.isOf(net.minecraft.entity.damage.DamageTypes.FALL)) {
                cir.setReturnValue(false)
                return
            }
        }
    }
}