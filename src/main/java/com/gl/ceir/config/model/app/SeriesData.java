package com.gl.ceir.config.model.app;

import com.gl.ceir.config.request.model.HighCharts.PieSeries;

import java.util.List;

public class SeriesData {
    String name;
    String type;
    List<String> data;


    public SeriesData(String name, List<String> data) {
        this.name = name;
        this.data = data;
    }

    public SeriesData(String name, String type, List<String> data) {
        this.name = name;
        this.type = type;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List  getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "SeriesData{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", data=" + data +
                '}';
    }
}
