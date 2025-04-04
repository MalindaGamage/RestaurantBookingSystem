package com.restaurant.controller;

import com.restaurant.domain.SystemSettings;

public class SystemSettingsManager {
    private SystemSettings settings;

    public SystemSettingsManager() {
        this.settings = new SystemSettings();
    }

    public SystemSettings getSystemSettings() {
        return settings;
    }

    public boolean updateSystemSettings(String settings) {
        return this.settings.update(settings);
    }

    public void manageSystemSettings() {
        System.out.println("Managing system settings...");
    }

    public boolean configureTableLayout() {
        return true;
    }

    public boolean setOperatingHours(String hours) {
        return true;
    }
}