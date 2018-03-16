package com.harvestasm.apm.repository.model;

/**
 * Created by yangfeng on 2018/3/16.
 */

public class ApmMeasurementItem {
    private int count;
    private double total;
    private double min;
    private double max;
    private double sum_of_squares;
    private String name;
    private String scope;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getSum_of_squares() {
        return sum_of_squares;
    }

    public void setSum_of_squares(double sum_of_squares) {
        this.sum_of_squares = sum_of_squares;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
