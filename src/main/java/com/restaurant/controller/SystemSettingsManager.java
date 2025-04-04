package com.restaurant.controller;

import com.restaurant.domain.SystemSettings;

import java.util.HashMap;
import java.util.Map;

public class SystemSettingsManager {
    private SystemSettings settings; // Reference to SystemSettings domain object
    private Map<String, Object> currentSettings; // Internal storage for settings

    public SystemSettingsManager() {
        this.settings = new SystemSettings();
        this.currentSettings = new HashMap<>();
    }

    // Get the SystemSettings object (if needed elsewhere)
    public SystemSettings getSystemSettings() {
        return settings;
    }

    // Update settings using a Map (primary method for UC11)
    public boolean updateSettings(Map<String, Object> settings) {
        // Validation
        if (settings.containsKey("operatingHours")) {
            if (!(settings.get("operatingHours") instanceof String)) {
                return false;
            }
            String hours = (String) settings.get("operatingHours");
            if (!hours.matches("\\d{2}:\\d{2}-\\d{2}:\\d{2}")) { // e.g., "09:00-22:00"
                return false;
            }
            this.settings.update(hours); // Delegate to SystemSettings if it handles hours
        }
        if (settings.containsKey("tableCount")) {
            if (!(settings.get("tableCount") instanceof Integer)) {
                return false;
            }
            int count = (Integer) settings.get("tableCount");
            if (count < 0) {
                return false;
            }
        }
        // Apply settings if valid
        this.currentSettings.putAll(settings);
        return true;
    }

    // Legacy method for string-based updates (optional, can be removed if not needed)
    public boolean updateSystemSettings(String settingsString) {
        return this.settings.update(settingsString); // Delegate to SystemSettings
    }

    // Display or manage settings (for UC11 Step 2, if needed)
    public void manageSystemSettings() {
        System.out.println("Current system settings: " + currentSettings);
    }

    // Getter for current settings (optional utility)
    public Map<String, Object> getCurrentSettings() {
        return new HashMap<>(currentSettings); // Return a copy to prevent external modification
    }
}