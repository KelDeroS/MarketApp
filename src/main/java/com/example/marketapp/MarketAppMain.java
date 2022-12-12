package com.example.marketapp;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;

public class MarketAppMain extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MarketAppMain.class.getResource("ProductsInWarehouseView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setResizable(false);
        stage.setTitle("Products in warehouse");
        stage.setScene(scene);
        stage.show();
    }

    public static int dayCount = 0;
    public static BigDecimal estimation = BigDecimal.valueOf(0);
    private static ObservableList<ProductsInWarehouse> savedWarehouseData = FXCollections.observableArrayList();
    private static ObservableList<ProductsInStore> savedStoreData = FXCollections.observableArrayList();
    private static ObservableList<AllProducts> savedAllProductsData = FXCollections.observableArrayList();
    private static ObservableList<Stats> savedLastIterationData = FXCollections.observableArrayList();
    private static ObservableList<Stats> savedLastPeriodData = FXCollections.observableArrayList();
    private static ObservableList<Stats> savedStatsData = FXCollections.observableArrayList();

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public static void saveWarehouseData(ObservableList<ProductsInWarehouse> data)
    {
        savedWarehouseData = data;
    }
    public static ObservableList<ProductsInWarehouse> loadWarehouseData()
    {
        return savedWarehouseData;
    }

    public static void saveStoreData(ObservableList<ProductsInStore> data)
    {
        savedStoreData = data;
    }
    public static ObservableList<ProductsInStore> loadStoreData()
    {
        return savedStoreData;
    }

    public static ObservableList<AllProducts> loadAllProductsData()
    {
        return savedAllProductsData;
    }
    public static void createAllProductsData()
    {
        savedAllProductsData.clear();
        for (int i = 0; i < savedWarehouseData.size(); i++)
        {
            AllProducts tmp = new AllProducts();
            tmp.setName(savedWarehouseData.get(i).getName());
            tmp.setAmountInWarehouse(savedWarehouseData.get(i).getAmount());
            tmp.setBuyPrice(savedWarehouseData.get(i).getPrice());
            tmp.setAmountInStore(savedStoreData.get(i).getQuantity());
            tmp.setMinSold(savedStoreData.get(i).getMinSold());
            tmp.setMaxSold(savedStoreData.get(i).getMaxSold());
            tmp.setSellPrice(savedStoreData.get(i).getPrice());
            savedAllProductsData.add(tmp);
        }
    }

    public static void saveLastIterationData(ObservableList<Stats> data)
    {
        savedLastIterationData = data;
    }
    public static ObservableList<Stats> loadLastIterationData()
    {
        return savedLastIterationData;
    }

    public static void saveLastPeriodData(ObservableList<Stats> data)
    {
        savedLastPeriodData = data;
    }
    public static ObservableList<Stats> loadLastPeriodData()
    {
        return savedLastPeriodData;
    }

    public static void saveStatsData(ObservableList<Stats> data)
    {
        savedStatsData = data;
    }

    public static ObservableList<Stats> loadStatsData()
    {
        return savedStatsData;
    }
    public static void createStatsData()
    {
        savedStatsData.clear();
        for (int i = 0; i < savedWarehouseData.size(); i++)
        {
            Stats tmp = new Stats();
            tmp.setName(savedWarehouseData.get(i).getName());
            tmp.setSoldLast(0);
            tmp.setRevenueLast(BigDecimal.valueOf(0));
            tmp.setSold(0);
            tmp.setSpent(BigDecimal.valueOf(0));
            tmp.setRevenue(BigDecimal.valueOf(0));
            tmp.setGained(BigDecimal.valueOf(0));
            tmp.setLeftInStore(0);
            tmp.setLeftInWarehouse(savedWarehouseData.get(i).getAmount());
            savedStatsData.add(tmp);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}