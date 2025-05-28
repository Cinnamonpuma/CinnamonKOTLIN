package code.cinnamon.modules.impl

import code.cinnamon.modules.Module

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

class NoFallModule : Module("NoFall", "Prevents fall damage") {
    override fun onEnable() {
        // Add no fall damage logic here
        println("NoFall module enabled!")
    }
    
    override fun onDisable() {
        // Remove no fall damage logic here
        println("NoFall module disabled!")
    }
}