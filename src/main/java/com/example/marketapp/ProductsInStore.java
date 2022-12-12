package com.example.marketapp;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ProductsInStore {
    private String name;
    private int quantity, minSold, maxSold;
    private BigDecimal price;

    public ProductsInStore(){
    }

    public ProductsInStore(String name)
    {
        this.name = name;
        this.minSold = 0;
        this.maxSold = 1;
        this.quantity = 0;
        this.price = BigDecimal.valueOf(0);
    }

    public ProductsInStore(String name, int minSold, int maxSold, int quantity, BigDecimal price)
    {
        this.name = name;
        this.minSold = minSold;
        this.maxSold = maxSold;
        this.quantity = quantity;
        this.price = price.setScale(2, RoundingMode.HALF_UP);
    }

    public String getName()
    {
        return name;
    }

    public int getMinSold()
    {
        return minSold;
    }

    public int getMaxSold()
    {
        return maxSold;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public BigDecimal getPrice()
    {
        return price;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setMinSold(int minSold) {this.minSold = minSold;}

    public void setMaxSold(int maxSold) {this.maxSold = maxSold;}

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }
}
