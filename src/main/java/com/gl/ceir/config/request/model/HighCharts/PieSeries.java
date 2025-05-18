package com.gl.ceir.config.request.model.HighCharts;

import java.util.List;

public class PieSeries {
    private String name;
    List<PieSeriesData> data;
    public PieSeries(){}

    public PieSeries(String name, List<PieSeriesData> data) {
        this.data = data;
        this.name = name;
    }

    // Getters and Setters

    @Override
    public String toString() {
        return "{ data:" + data + ", name: '" + name + "' }";
    }
}
