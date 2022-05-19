package com.nikolaev.horeca.misc;

public class PagerLabel {
    boolean isAccessible;
    boolean isActive;
    String label;

    public PagerLabel(boolean isAccessible, boolean isActive, String label) {
        this.isAccessible = isAccessible;
        this.isActive = isActive;
        this.label = label;
    }

    public PagerLabel() {
        this.isAccessible = false;
        this.isActive = false;
        this.label = "1";
    }

    public boolean isAccessible() {
        return isAccessible;
    }

    public void setAccessible(boolean accessible) {
        isAccessible = accessible;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
