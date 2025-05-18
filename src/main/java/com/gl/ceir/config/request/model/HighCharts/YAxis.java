package com.gl.ceir.config.request.model.HighCharts;

public class YAxis {
    private Title title;

    // Getters and Setters

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

//    @Override
//    public String toString() {
//      //  return "{text: '" + text + "'}";
//        return "yAxis:{ " + title + "}";
//    }


    @Override
    public String toString() {
        return "{title:" + title + '}';
    }
}
