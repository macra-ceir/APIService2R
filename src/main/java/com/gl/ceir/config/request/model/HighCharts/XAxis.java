package com.gl.ceir.config.request.model.HighCharts;

import java.util.List;

public class XAxis {
    private List<String> categories;

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "{ categories: " + categories + " }";
    }
}
