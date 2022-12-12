package com.example.marketapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.BigDecimalStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.example.marketapp.MarketAppMain.*;

public class ProductsInWarehouseController {

    @FXML
    private TextField addAmount;

    @FXML
    private Button warehouseNextWindowButton;

    @FXML
    private Button addButton;

    @FXML
    private TextField addName;

    @FXML
    private TextField addPrice;

    @FXML
    private Button deleteButton;

    @FXML
    private TableColumn<ProductsInWarehouse, Integer> productsInWarehouseAmount;

    @FXML
    private TableColumn<ProductsInWarehouse, String> productsInWarehouseName;

    @FXML
    private TableColumn<ProductsInWarehouse, BigDecimal> productsInWarehousePrice;

    @FXML
    private  TableView<ProductsInWarehouse> productsInWarehouseTable;



    @FXML
    void initialize() {
        initTable();
        fillTable();
        packData();
    }

    private void initTable()
    {
        initColumns();
        addButton.setOnAction(e->addButtonClicked());
        deleteButton.setOnAction(e->deleteButtonClicked());
        warehouseNextWindowButton.setOnAction(e->warehouseNextWindowButtonClicked());
    }

    private void initColumns()
    {
        productsInWarehouseName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productsInWarehouseAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        productsInWarehousePrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        editColumns();
    }

    private void editColumns()
    {
        productsInWarehouseName.setCellFactory(TextFieldTableCell.forTableColumn());

        productsInWarehouseName.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setName(e.getNewValue());
            productsInWarehouseTable.refresh();
        });

        productsInWarehouseAmount.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));


        productsInWarehouseAmount.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setAmount(e.getNewValue());
            productsInWarehouseTable.refresh();
        });

        productsInWarehousePrice.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));

        productsInWarehousePrice.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setPrice(e.getNewValue().setScale(2, RoundingMode.HALF_UP));
            productsInWarehouseTable.refresh();
        });

        productsInWarehouseTable.setEditable(true);
    }

    public void addButtonClicked()
    {
        ProductsInWarehouse product = new ProductsInWarehouse();
        product.setName(addName.getText());
        if (isNumeric(addAmount.getText()))
        {
            product.setAmount(Integer.parseInt(addAmount.getText()));
        }
        else
        {
            addAmount.clear();
            if (!isNumeric(addPrice.getText()))
            {
                addPrice.clear();
            }
            return;
        }
        if (isNumeric(addPrice.getText()))
        {
            product.setPrice(BigDecimal.valueOf(Double.parseDouble(addPrice.getText())).setScale(2, RoundingMode.HALF_UP));
        }
        else
        {
            addPrice.clear();
            return;
        }
        productsInWarehouseTable.getItems().add(product);
        addName.clear();
        addAmount.clear();
        addPrice.clear();
        fixTable();
        packData();
    }

    public void deleteButtonClicked()
    {
        ObservableList<ProductsInWarehouse> allProducts, selectedProducts;
        allProducts = productsInWarehouseTable.getItems();
        selectedProducts = productsInWarehouseTable.getSelectionModel().getSelectedItems();
        selectedProducts.forEach(allProducts::remove);
        packData();
    }

    public void fixTable()
    {
        productsInWarehouseTable.getSortOrder().add(productsInWarehouseName);
        int i = 0;
        while (i < productsInWarehouseTable.getItems().size() - 1)
        {
            if (productsInWarehouseTable.getItems().get(i).getName().equals(productsInWarehouseTable.getItems().get(i + 1).getName()))
            {
                productsInWarehouseTable.getItems().remove(i + 1);
                i--;
            }
            i++;
        }
    }

    public void warehouseNextWindowButtonClicked()
    {
        packData();
        warehouseNextWindowButton.getScene().getWindow().hide();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ProductsInStoreView.fxml"));

        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setTitle("Товары в магазине");
        stage.show();
    }

    public void packData()
    {
        ObservableList<ProductsInWarehouse> packedData = FXCollections.observableArrayList();
        int i = 0;

        while (i < productsInWarehouseTable.getItems().size())
        {
            //System.out.println(productsInWarehouseTable.getItems().get(i).getName());
            ProductsInWarehouse tmp = new ProductsInWarehouse();
            tmp.setName(productsInWarehouseTable.getItems().get(i).getName());
            tmp.setAmount(productsInWarehouseTable.getItems().get(i).getAmount());
            tmp.setPrice(productsInWarehouseTable.getItems().get(i).getPrice());
            packedData.add(tmp);
            i++;
        }
        saveWarehouseData(packedData);
    }
    public  ObservableList<ProductsInWarehouse> getContent() {
        return loadWarehouseData();
    }

    public void fillTable()
    {
        ObservableList<ProductsInWarehouse> packedData = loadWarehouseData();
        int i = 0;
        while (i < packedData.size())
        {
            ProductsInWarehouse tmp = new ProductsInWarehouse(packedData.get(i).getName(), packedData.get(i).getAmount(), packedData.get(i).getPrice());
            productsInWarehouseTable.getItems().add(tmp);
            i++;
        }
    }
}
