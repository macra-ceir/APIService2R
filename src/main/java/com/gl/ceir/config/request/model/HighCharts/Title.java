package com.gl.ceir.config.request.model.HighCharts;

public class Title {
    private String text;

    // Getters and Setters

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "{text: '" + text + "'}";
    }
}
