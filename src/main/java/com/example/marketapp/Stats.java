package com.example.marketapp;

import java.math.BigDecimal;

public class Stats {
    private String name, estimationReal, estimationTheoretical;
    private int soldLast, sold, leftInStore, leftInWarehouse;
    private BigDecimal revenueLast, spent, gained, revenue;

    public Stats(){}

    public Stats(String name, int soldLast, int sold, int leftInStore, int leftInWarehouse, BigDecimal revenueLast, BigDecimal spent, BigDecimal revenue, BigDecimal gained, String estimationReal, String estimationTheoretical) {
        this.name = name;
        this.soldLast = soldLast;
        this.sold = sold;
        this.leftInStore = leftInStore;
        this.leftInWarehouse = leftInWarehouse;
        this.revenueLast = revenueLast;
        this.spent = spent;
        this.revenue = revenue;
        this.gained = gained;
        this.estimationReal = estimationReal;
        this.estimationTheoretical = estimationTheoretical;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSoldLast(int soldLast) {
        this.soldLast = soldLast;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public void setLeftInStore(int leftInStore) {
        this.leftInStore = leftInStore;
    }

    public void setLeftInWarehouse(int leftInWarehouse) {
        this.leftInWarehouse = leftInWarehouse;
    }

    public void setRevenueLast(BigDecimal revenueLast) {
        this.revenueLast = revenueLast;
    }

    public void setSpent(BigDecimal spent) {
        this.spent = spent;
    }

    public void setRevenue(BigDecimal revenue){
        this.revenue = revenue;
    }

    public void setGained(BigDecimal gained) {
        this.gained = gained;
    }

    public void setEstimationReal(String estimationReal) {
        this.estimationReal = estimationReal;
    }

    public void setEstimationTheoretical(String estimationTheoretical) {
        this.estimationTheoretical = estimationTheoretical;
    }

    public String getName() {
        return name;
    }

    public int getSoldLast() {
        return soldLast;
    }

    public int getSold() {
        return sold;
    }

    public int getLeftInStore() {
        return leftInStore;
    }

    public int getLeftInWarehouse() {
        return leftInWarehouse;
    }

    public BigDecimal getRevenueLast() {
        return revenueLast;
    }

    public BigDecimal getSpent() {
        return spent;
    }

    public BigDecimal getRevenue(){
        return revenue;
    }

    public BigDecimal getGained() {
        return gained;
    }

    public String getEstimationReal() {
        return estimationReal;
    }

    public String getEstimationTheoretical() {
        return estimationTheoretical;
    }

}
