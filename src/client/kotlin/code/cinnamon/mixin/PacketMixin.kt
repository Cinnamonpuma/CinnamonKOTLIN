// PacketMixin.kt - Mixin to modify outgoing packets
package code.cinnamon.mixin

import code.cinnamon.modules.ModuleManager
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(PlayerMoveC2SPacket::class)
class PacketMixin {
    
    @Shadow
    protected var onGround: Boolean = false
    
    /**
     * Modify outgoing movement packets to always report onGround = true
     * This is the most reliable method for most servers
     */
    @Inject(method = ["<init>(DDDFFZZ)V"], at = [At("TAIL")])
    private fun modifyMovementPacket(x: Double, y: Double, z: Double, yaw: Float, pitch: Float, onGround: Boolean, horizontalCollision: Boolean, ci: CallbackInfo) {
        val noFallModule = ModuleManager.getModule("NoFall")
        
        if (noFallModule != null && noFallModule.isEnabled) {
            // Directly modify the onGround field using Shadow
            this.onGround = true
            println("NoFall: Modified packet onGround to true")
        }
    }
}