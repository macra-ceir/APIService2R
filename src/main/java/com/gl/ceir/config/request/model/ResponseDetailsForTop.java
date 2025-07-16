package com.gl.ceir.config.request.model;

public class ResponseDetailsForTop {
    String dateValue;
    String typeValue;
    String countValue;


    public String getDateValue() {
        return dateValue;
    }

    public void setDateValue(String dateValue) {
        this.dateValue = dateValue;
    }

    public String getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(String typeValue) {
        this.typeValue = typeValue;
    }

    public String getCountValue() {
        return countValue;
    }

    public void setCountValue(String countValue) {
        this.countValue = countValue;
    }

    public ResponseDetailsForTop(String dateValue, String typeValue, String countValue) {
        this.dateValue = dateValue;
        this.typeValue = typeValue;
        this.countValue = countValue;
    }
}
