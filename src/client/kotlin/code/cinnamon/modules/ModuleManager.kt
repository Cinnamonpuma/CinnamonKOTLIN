package code.cinnamon.modules

import code.cinnamon.modules.impl.*

object ModuleManager {
    private val modules = mutableListOf<Module>()
    
    fun registerModule(module: Module) {
        modules.add(module)
    }
    
    fun getModules(): List<Module> = modules.toList()
    
    fun getModule(name: String): Module? {
        return modules.find { it.name == name }
    }
    
    fun enableModule(name: String) {
        getModule(name)?.enable()
    }
    
    fun disableModule(name: String) {
        getModule(name)?.disable()
    }
    
    fun toggleModule(name: String) {
        getModule(name)?.toggle()
    }
    
    fun getEnabledModules(): List<Module> {
        return modules.filter { it.isEnabled }
    }
    
    fun initialize() {
        // Register your modules here
        registerModule(NoFallModule())
        registerModule(AutoclickerModule()) // Added
        // Add more modules as needed
    }
}

abstract class Module(val name: String, val description: String) {
    var isEnabled = false
        private set
    
    open fun enable() {
        if (!isEnabled) {
            isEnabled = true
            onEnable()
        }
    }
    
    open fun disable() {
        if (isEnabled) {
            isEnabled = false
            onDisable()
        }
    }
    
    fun toggle() {
        if (isEnabled) disable() else enable()
    }
    
    protected abstract fun onEnable()
    protected abstract fun onDisable()
}