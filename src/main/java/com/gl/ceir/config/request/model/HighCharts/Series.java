package com.gl.ceir.config.request.model.HighCharts;

import java.util.List;

public class Series {
    private String name;
    private List<Integer> data;
    public Series(){}
    public Series(String name, List<Integer> data) {
        this.data = data;
        this.name = name;
    }

    // Getters and Setters

    @Override
    public String toString() {
        return "{ data:" + data + ", name: '" + name + "' }";
    }
}
