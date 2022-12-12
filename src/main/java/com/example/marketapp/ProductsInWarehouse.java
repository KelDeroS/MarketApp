package com.example.marketapp;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ProductsInWarehouse {
    private String name;
    private int amount;
    private BigDecimal price;


    public ProductsInWarehouse(){
    }

    public ProductsInWarehouse(String name, int amount, BigDecimal price)
    {
        this.name = name;
        this.amount = amount;
        this.price = price.setScale(2, RoundingMode.HALF_UP);
    }

    public String getName()
    {
        return name;
    }

    public int getAmount()
    {
        return amount;
    }

    public BigDecimal getPrice()
    {
        return price;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setAmount(int amount)
    {
        this.amount = amount;
    }

    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }
}
