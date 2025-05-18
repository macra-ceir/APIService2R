package com.gl.ceir.config.request.model.HighCharts;


public class Chart {
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
// Getters and Setters


    @Override
    public String toString() {
        return "{type:'" + type + "'}";
    }
}
