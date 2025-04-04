package com.restaurant.domain;

public class SystemSettings {
    private String[] operatingHours;
    private String tableConfiguration;
    private String confirmationSettings;
    private String[] userPermissions;

    public SystemSettings() {
        this.operatingHours = new String[]{"09:00-22:00"};
        this.tableConfiguration = "50 tables";
        this.confirmationSettings = "Email";
        this.userPermissions = new String[]{"Staff", "Manager"};
    }

    public boolean update(String settings) {
        this.tableConfiguration = settings;
        return true;
    }

    public boolean reset() {
        this.operatingHours = new String[]{"09:00-22:00"};
        this.tableConfiguration = "50 tables";
        this.confirmationSettings = "Email";
        return true;
    }

    public boolean validate() {
        return true;
    }
}