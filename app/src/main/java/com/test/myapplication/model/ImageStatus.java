package com.test.myapplication.model;

public class ImageStatus {

    private boolean selected;

    public ImageStatus(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
