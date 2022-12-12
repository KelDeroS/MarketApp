package com.example.marketapp;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.BigDecimalStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.example.marketapp.MarketAppMain.*;

public class ProductsInStoreController {

    @FXML
    private TableColumn<ProductsInStore, Integer> productsInStoreMaxSold;

    @FXML
    private TableColumn<ProductsInStore, Integer> productsInStoreMinSold;

    @FXML
    private TableColumn<ProductsInStore, String> productsInStoreName;

    @FXML
    private TableColumn<ProductsInStore, BigDecimal> productsInStorePrice;

    @FXML
    private TableColumn<ProductsInStore, Integer> productsInStoreQuantity;

    @FXML
    private TableView<ProductsInStore> productsInStoreTable;

    @FXML
    private Button storeNextWindowButton;

    @FXML
    private Button storePreviousWindowButton;

    @FXML
    void initialize() {
        initTable();
        fillTable();
        packData();
    }

    private void initTable()
    {
        initColumns();
        storePreviousWindowButton.setOnAction(e->storePreviousWindowButtonClicked());
        storeNextWindowButton.setOnAction(e->storeNextWindowButtonClicked());
    }

    private void initColumns()
    {
        productsInStoreName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productsInStoreMinSold.setCellValueFactory(new PropertyValueFactory<>("minSold"));
        productsInStoreMaxSold.setCellValueFactory(new PropertyValueFactory<>("maxSold"));
        productsInStoreQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        productsInStorePrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        editColumns();
    }

    private void editColumns()
    {
        productsInStoreMinSold.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        productsInStoreMinSold.setOnEditCommit(e->{
            if (e.getNewValue() <= e.getTableView().getItems().get(e.getTablePosition().getRow()).getMaxSold())
            {
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setMinSold(e.getNewValue());
            }
            productsInStoreTable.refresh();
        });

        productsInStoreMaxSold.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        productsInStoreMaxSold.setOnEditCommit(e->{
            if (e.getTableView().getItems().get(e.getTablePosition().getRow()).getMinSold() <= e.getNewValue())
            {
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setMaxSold(e.getNewValue());
            }
            productsInStoreTable.refresh();
        });

        productsInStoreQuantity.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        productsInStoreQuantity.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setQuantity(e.getNewValue());
            productsInStoreTable.refresh();
        });

        productsInStorePrice.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));

        productsInStorePrice.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setPrice(e.getNewValue().setScale(2, RoundingMode.HALF_UP));
            productsInStoreTable.refresh();
        });

        productsInStoreTable.setEditable(true);
    }

    public void storePreviousWindowButtonClicked()
    {
        packData();
        storePreviousWindowButton.getScene().getWindow().hide();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ProductsInWarehouseView.fxml"));

        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setTitle("Товары на складе");
        stage.show();
    }

    public void storeNextWindowButtonClicked()
    {
        packData();
        createAllProductsData();
        createStatsData();
        storePreviousWindowButton.getScene().getWindow().hide();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ControlPanelView.fxml"));

        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setTitle("Панель контроля");
        stage.show();
    }

    public void packData()
    {
        ObservableList<ProductsInStore> packedData = FXCollections.observableArrayList();
        int i = 0;

        while (i < productsInStoreTable.getItems().size())
        {
            ProductsInStore tmp = new ProductsInStore();
            tmp.setName(productsInStoreTable.getItems().get(i).getName());
            tmp.setMinSold(productsInStoreTable.getItems().get(i).getMinSold());
            tmp.setMaxSold(productsInStoreTable.getItems().get(i).getMaxSold());
            tmp.setQuantity(productsInStoreTable.getItems().get(i).getQuantity());
            tmp.setPrice(productsInStoreTable.getItems().get(i).getPrice());
            packedData.add(tmp);
            i++;
        }
        saveStoreData(packedData);
    }

    public void fillTable()
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MarketAppMain.class.getResource("ProductsInWarehouseView.fxml"));

        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        ProductsInWarehouseController controller = loader.getController();
        ObservableList<ProductsInWarehouse> content = controller.getContent();

        for (int i = 0; i < content.size(); i++)
        {
            ProductsInStore tmp = new ProductsInStore(content.get(i).getName());
            productsInStoreTable.getItems().add(tmp);
        }

        ObservableList<ProductsInStore> packedData = loadStoreData();
        for (int i = 0; i < productsInStoreTable.getItems().size(); i++)
        {
            for (int j = 0; j < packedData.size(); j++)
            {
                if (productsInStoreTable.getItems().get(i).getName().equals(packedData.get(j).getName()))
                {
                    productsInStoreTable.getItems().get(i).setMinSold(packedData.get(j).getMinSold());
                    productsInStoreTable.getItems().get(i).setMaxSold(packedData.get(j).getMaxSold());
                    productsInStoreTable.getItems().get(i).setQuantity(packedData.get(j).getQuantity());
                    productsInStoreTable.getItems().get(i).setPrice(packedData.get(j).getPrice());
                }
            }
        }
    }
}
