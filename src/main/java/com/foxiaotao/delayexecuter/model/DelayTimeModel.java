package com.foxiaotao.delayexecuter.model;


import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class DelayTimeModel implements Serializable {

    private TimeUnit unit;

    private Integer value;

    public TimeUnit getUnit() {
        return unit;
    }

    public void setUnit(TimeUnit unit) {
        this.unit = unit;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
