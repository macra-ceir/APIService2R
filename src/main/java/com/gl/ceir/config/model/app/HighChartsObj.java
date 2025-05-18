package com.gl.ceir.config.model.app;

import java.util.List;

public class HighChartsObj {

    String title;
    String subtitle;
    String chartType;
    String yAxis;
    String legend;
    List catogery;
    String pointStart;
    List<SeriesData> seriesData;

    public HighChartsObj() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getChartType() {
        return chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }


    public String getyAxis() {
        return yAxis;
    }

    public void setyAxis(String yAxis) {
        this.yAxis = yAxis;
    }

    public String getLegend() {
        return legend;
    }

    public void setLegend(String legend) {
        this.legend = legend;
    }

    public List getCatogery() {
        return catogery;
    }

    public void setCatogery(List catogery) {
        this.catogery = catogery;
    }

    public String getPointStart() {
        return pointStart;
    }

    public void setPointStart(String pointStart) {
        this.pointStart = pointStart;
    }

    public List<SeriesData> getSeriesData() {
        return seriesData;
    }

    public void setSeriesData(List<SeriesData> seriesData) {
        this.seriesData = seriesData;
    }

    @Override
    public String toString() {
        return "HighChartsObj{" +
                "title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", chartType='" + chartType + '\'' +
                ", yAxis='" + yAxis + '\'' +
                ", legend='" + legend + '\'' +
                ", catogery=" + catogery +
                ", pointStart='" + pointStart + '\'' +
                ", seriesData=" + seriesData +
                '}';
    }
}


