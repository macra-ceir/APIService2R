package com.gl.ceir.config.request.model.HighCharts;


public class PieSeriesData {
    String name;
    int y;

    public PieSeriesData() {
    }

    public PieSeriesData(String name, int y) {
        this.y = y;
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return " { name:'" + name + "', y: " + y + " }";
    }

}
