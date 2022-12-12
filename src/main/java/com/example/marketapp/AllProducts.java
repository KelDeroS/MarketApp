package com.example.marketapp;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AllProducts {

    private String name;
    private int amountInWarehouse, amountInStore, minSold, maxSold;
    private BigDecimal buyPrice, sellPrice;

    public AllProducts(){}

    public AllProducts(String name, int amountInWarehouse, int amountInStore, int minSold, int maxSold, BigDecimal buyPrice, BigDecimal sellPrice) {
        this.name = name;
        this.amountInWarehouse = amountInWarehouse;
        this.amountInStore = amountInStore;
        this.minSold = minSold;
        this.maxSold = maxSold;
        this.buyPrice = buyPrice.setScale(2, RoundingMode.HALF_UP);
        this.sellPrice = sellPrice.setScale(2, RoundingMode.HALF_UP);
    }

    public String getName() {
        return name;
    }

    public int getAmountInWarehouse() {
        return amountInWarehouse;
    }

    public int getAmountInStore() {
        return amountInStore;
    }

    public int getMinSold() {
        return minSold;
    }

    public int getMaxSold() {
        return maxSold;
    }

    public BigDecimal getBuyPrice() {
        return buyPrice;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmountInWarehouse(int amountInWarehouse) {
        this.amountInWarehouse = amountInWarehouse;
    }

    public void setAmountInStore(int amountInStore) {
        this.amountInStore = amountInStore;
    }

    public void setMinSold(int minSold) {
        this.minSold = minSold;
    }

    public void setMaxSold(int maxSold) {
        this.maxSold = maxSold;
    }

    public void setBuyPrice(BigDecimal buyPrice) {
        this.buyPrice = buyPrice;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }
}
