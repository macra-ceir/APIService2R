package com.gl.ceir.config.request.model.HighCharts;


import java.util.List;


public class HighChartOutput {

    private Chart chart;
    private Title title;
    private XAxis xAxis;
    private YAxis yAxis;
    private List<Series> series;

    public Chart getChart() {
        return chart;
    }

    public void setChart(Chart chart) {
        this.chart = chart;
    }

    public List<Series> getSeries() {
        return series;
    }

    public void setSeries(List<Series> series) {
        this.series = series;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public XAxis getxAxis() {
        return xAxis;
    }

    public void setxAxis(XAxis xAxis) {
        this.xAxis = xAxis;
    }

    public YAxis getyAxis() {
        return yAxis;
    }

    public void setyAxis(YAxis yAxis) {
        this.yAxis = yAxis;
    }

    @Override
    public String toString() {
        return "{ chart:" + chart + ", title:" + title + ", xAxis:" + xAxis + ", yAxis:" + yAxis + ", series:" + series + '}';
    }
}

