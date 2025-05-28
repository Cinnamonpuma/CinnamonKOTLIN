# Cinnamon GUI Documentation

## Core Files

1. **CinnamonScreen.kt**
   - Base class for all GUI screens
   - Handles common functionality like:
     - Background rendering
     - Screen dimensions and positioning
     - Header/footer rendering
     - Basic input handling

2. **CinnamonGuiManager.kt**
   - Manages screen transitions and navigation
   - Keeps track of current/open screens
   - Provides methods like `openMainMenu()`, `openModulesScreen()`, etc.

3. **CinnamonTheme.kt**
   - Defines colors, sizes, and visual styles
   - Centralized theming system for consistent look
   - Contains color constants for backgrounds, text, buttons, etc.

## Components

4. **CinnamonButton.kt**
   - Custom button implementation
   - Handles hover/pressed states
   - Supports theming from CinnamonTheme

## Screen Implementations

5. **MainMenuScreen.kt**
   - The main entry screen for the GUI
   - Contains navigation buttons to other screens
   - Displays mod name/version

6. **ModulesScreen.kt**
   - Shows available modules
   - Allows enabling/disabling modules
   - May include module configuration

7. **KeybindingsScreen.kt**
   - Displays and allows editing keybindings
   - Shows current key assignments
   - Handles key rebinding

8. **SettingsScreen.kt**
   - Contains various mod settings
   - May include visual/performance options
   - Handles preference saving/loading