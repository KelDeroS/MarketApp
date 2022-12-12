package com.example.marketapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;

import static com.example.marketapp.MarketAppMain.*;

public class AllProductsController {

    @FXML
    private TableColumn<AllProducts, Integer> allProductsAmountInWarehouse;

    @FXML
    private TableColumn<AllProducts, BigDecimal> allProductsBuyPrice;

    @FXML
    private TableColumn<AllProducts, Integer> allProductsMaxSold;

    @FXML
    private TableColumn<AllProducts, Integer> allProductsMinSold;

    @FXML
    private TableColumn<AllProducts, String> allProductsName;

    @FXML
    private TableColumn<AllProducts, Integer> allProductsAmountInStore;

    @FXML
    private TableColumn<AllProducts, BigDecimal> allProductsSellPrice;

    @FXML
    private TableView<AllProducts> allProductsTable;

    @FXML
    void initialize() {
        initColumns();
        fillTable();
    }

    private void initColumns(){
        allProductsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        allProductsAmountInWarehouse.setCellValueFactory(new PropertyValueFactory<>("amountInWarehouse"));
        allProductsAmountInStore.setCellValueFactory(new PropertyValueFactory<>("amountInStore"));
        allProductsMinSold.setCellValueFactory(new PropertyValueFactory<>("minSold"));
        allProductsMaxSold.setCellValueFactory(new PropertyValueFactory<>("maxSold"));
        allProductsBuyPrice.setCellValueFactory(new PropertyValueFactory<>("buyPrice"));
        allProductsSellPrice.setCellValueFactory(new PropertyValueFactory<>("sellPrice"));
    }

    public void fillTable()
    {
        ObservableList<AllProducts> packedData = loadAllProductsData();

        for (int i = 0; i < packedData.size(); i++)
        {
            allProductsTable.getItems().add(packedData.get(i));
        }
    }


}
