package code.cinnamon.modules.impl

import code.cinnamon.modules.Module
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.random.Random

class SpeedModule : Module("Speed", "Increases player movement speed") {
    override fun onEnable() {
        // Add speed effect logic here
        println("Speed module enabled!")
    }
    
    override fun onDisable() {
        // Remove speed effect logic here
        println("Speed module disabled!")
    }
}

class FlightModule : Module("Flight", "Allows player to fly") {
    override fun onEnable() {
        // Add flight logic here
        println("Flight module enabled!")
    }
    
    override fun onDisable() {
        // Remove flight logic here
        println("Flight module disabled!")
    }
}

/**
 * NoFall Module - Prevents fall damage by manipulating player state
 * 
 * This module works by detecting when the player is falling and would take damage,
 * then sends packets to the server to indicate the player is on ground, preventing
 * fall damage calculation.
 */
class NoFallModule : Module("NoFall", "Prevents fall damage using mixins") {

    // Configuration options with proper setters
    var mode: NoFallMode = NoFallMode.MIXIN
        set(value) {
            field = value
            logDebug("NoFall mode changed to: $value")
        }
    
    var fallDistanceThreshold: Float = 3.0f
        set(value) {
            if (value > 0) {
                field = value
                logDebug("Fall distance threshold set to: $value")
            }
        }
    
    var packetSpoofing: Boolean = true
        set(value) {
            field = value
            logDebug("Packet spoofing: ${if (value) "enabled" else "disabled"}")
        }
    
    var resetFallDistance: Boolean = true
        set(value) {
            field = value
            logDebug("Fall distance reset: ${if (value) "enabled" else "disabled"}")
        }
    
    var debugOutput: Boolean = true
    
    // Statistics
    private var damagePreventedCount: Int = 0
    private var maxFallDistancePrevented: Float = 0.0f
    
    enum class NoFallMode {
        MIXIN,          // Use mixin to intercept fall damage (most effective)
        PACKET,         // Use packet spoofing (moderate effectiveness)
        HYBRID          // Use both methods (maximum compatibility)
    }
    
    override fun onEnable() {
        try {
            resetStatistics()
            logDebug("NoFall module enabled with mode: $mode")
            
            when (mode) {
                NoFallMode.PACKET -> {
                    logDebug("Warning: Packet-only mode may not work on all servers")
                }
                NoFallMode.MIXIN -> {
                    logDebug("Using mixin-based fall damage prevention")
                }
                NoFallMode.HYBRID -> {
                    logDebug("Using hybrid approach for maximum compatibility")
                }
            }
            
        } catch (e: Exception) {
            logDebug("Error enabling NoFall module: ${e.message}")
            disable()
        }
    }
    
    override fun onDisable() {
        try {
            logDebug("NoFall module disabled")
            logDebug("Statistics: Prevented $damagePreventedCount falls, max distance: $maxFallDistancePrevented")
        } catch (e: Exception) {
            logDebug("Error disabling NoFall module: ${e.message}")
        }
    }
    
    /**
     * Called by mixin when fall damage would occur
     * Returns true if damage should be prevented
     */
    fun shouldPreventFallDamage(fallDistance: Float): Boolean {
        if (!isEnabled) return false
        
        val shouldPrevent = fallDistance >= fallDistanceThreshold
        
        if (shouldPrevent) {
            damagePreventedCount++
            maxFallDistancePrevented = maxOf(maxFallDistancePrevented, fallDistance)
            logDebug("Preventing fall damage: ${fallDistance} blocks")
        }
        
        return shouldPrevent
    }
    
    /**
     * Called by mixin to check if fall distance should be reset
     */
    fun shouldResetFallDistance(currentFallDistance: Float): Boolean {
        if (!isEnabled || !resetFallDistance) return false
        
        return currentFallDistance >= fallDistanceThreshold
    }
    
    /**
     * Called by packet mixin to check if onGround should be forced
     */
    fun shouldForceOnGround(): Boolean {
        if (!isEnabled) return false
        
        return when (mode) {
            NoFallMode.PACKET, NoFallMode.HYBRID -> packetSpoofing
            else -> false
        }
    }
    
    /**
     * Manual packet spoofing method (backup for when mixins aren't available)
     */
    fun sendSpoofedPacket() {
        if (!isEnabled || mode == NoFallMode.MIXIN) return
        
        try {
            val client = MinecraftClient.getInstance()
            val player = client.player ?: return
            val networkHandler = client.networkHandler ?: return
            
            if (player.fallDistance >= fallDistanceThreshold) {
                val packet = PlayerMoveC2SPacket.Full(
                    player.x,
                    player.y,
                    player.z,
                    player.yaw,
                    player.pitch,
                    true, // Force onGround = true
                    false
                )
                networkHandler.sendPacket(packet)
                logDebug("Sent spoofed onGround packet")
            }
        } catch (e: Exception) {
            logDebug("Error sending spoofed packet: ${e.message}")
        }
    }
    
    /**
     * Get current status and statistics
     */
    fun getDetailedStatus(): String {
        val client = MinecraftClient.getInstance()
        val player = client.player
        
        return buildString {
            appendLine("=== NoFall Module Status ===")
            appendLine("Enabled: $isEnabled")
            appendLine("Mode: $mode")
            appendLine("Fall Threshold: $fallDistanceThreshold blocks")
            appendLine("Packet Spoofing: $packetSpoofing")
            appendLine("Reset Fall Distance: $resetFallDistance")
            appendLine()
            appendLine("=== Current State ===")
            if (player != null) {
                appendLine("Current Fall Distance: ${String.format("%.2f", player.fallDistance)}")
                appendLine("On Ground: ${player.isOnGround}")
                appendLine("In Water: ${player.isTouchingWater}")
                appendLine("Flying: ${player.abilities.flying}")
            } else {
                appendLine("Player: null")
            }
            appendLine()
            appendLine("=== Statistics ===")
            appendLine("Falls Prevented: $damagePreventedCount")
            appendLine("Max Fall Distance Prevented: ${String.format("%.2f", maxFallDistancePrevented)} blocks")
        }
    }
    
    fun getStatus(): String {
        val client = MinecraftClient.getInstance()
        val player = client.player
        
        return "NoFall: ${if (isEnabled) "Enabled" else "Disabled"} | " +
               "Mode: $mode | " +
               "Falls Prevented: $damagePreventedCount | " +
               if (player != null) "Fall Distance: ${String.format("%.2f", player.fallDistance)}" 
               else "Player: null"
    }
    
    private fun resetStatistics() {
        damagePreventedCount = 0
        maxFallDistancePrevented = 0.0f
    }
    
    private fun logDebug(message: String) {
        if (debugOutput) {
            println("NoFall: $message")
        }
    }
}