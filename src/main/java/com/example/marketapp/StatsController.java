package com.example.marketapp;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;

import static com.example.marketapp.MarketAppMain.*;

public class StatsController {
    @FXML
    private TextField sumRevenueField;

    @FXML
    private TableColumn<Stats, Double> statsRevenue;

    @FXML
    private TextField bestRevenueField;

    @FXML
    private TextField dayCounterField;

    @FXML
    private TextField bestSellField;

    @FXML
    private TextField estimationField;

    @FXML
    private TextField sumGainedField;

    @FXML
    private TextField sumSpentField;

    @FXML
    private TableColumn<Stats, String> statsEstimationName;

    @FXML
    private TableView<Stats> estimationStatsTable;

    @FXML
    private TableColumn<Stats, String> statsEstimationReal;

    @FXML
    private TableColumn<Stats, String> statsEstimationTheoretical;

    @FXML
    private TableColumn<Stats, BigDecimal> statsGained;

    @FXML
    private TableColumn<Stats, Integer> statsLeftInStore;

    @FXML
    private TableColumn<Stats, Integer> statsLeftInWarehouse;

    @FXML
    private TableColumn<Stats, String> statsName;

    @FXML
    private TableColumn<Stats, BigDecimal> statsRevenueLast;

    @FXML
    private TableColumn<Stats, Integer> statsSold;

    @FXML
    private TableColumn<Stats, Integer> statsSoldLast;

    @FXML
    private TableColumn<Stats, BigDecimal> statsSpent;

    @FXML
    private TableView<Stats> statsTable;

    @FXML
    void initialize() {
        initColumns();
        fillTable();
        estimator();
        ObservableList<Stats> stats = loadStatsData();
        ObservableList<AllProducts> inputs = loadAllProductsData();

        int mostSold = stats.get(0).getSold(), indexSell = 0, indexRev = 0;
        BigDecimal bestRevenue = BigDecimal.valueOf(stats.get(0).getSold()).multiply(inputs.get(0).getSellPrice().subtract(inputs.get(0).getBuyPrice()));
        for (int i = 1; i < stats.size(); i++) {
            if (stats.get(i).getSold() > mostSold)
            {
                mostSold = stats.get(i).getSold();
                indexSell = i;
            }

            if (Double.parseDouble(String.valueOf(BigDecimal.valueOf(stats.get(i).getSold()).multiply(inputs.get(i).getSellPrice().subtract(inputs.get(i).getBuyPrice())))) > Double.parseDouble(String.valueOf(bestRevenue)))
            {
                bestRevenue = BigDecimal.valueOf(stats.get(i).getSold()).multiply(inputs.get(i).getSellPrice().subtract(inputs.get(i).getBuyPrice()));
                indexRev = i;
            }

        }
        bestSellField.setText(stats.get(indexSell).getName());
        bestRevenueField.setText(stats.get(indexRev).getName());

        BigDecimal sumGained = BigDecimal.valueOf(0), sumSpent = BigDecimal.valueOf(0), sumRevenue = BigDecimal.valueOf(0);
        for (int i = 0; i < stats.size(); i++)
        {
            sumSpent = sumSpent.add(stats.get(i).getSpent());
            sumRevenue = sumRevenue.add(stats.get(i).getRevenue());
            sumGained = sumGained.add(stats.get(i).getGained());
        }
        sumSpentField.setText(String.valueOf(sumSpent));
        sumRevenueField.setText(String.valueOf(sumRevenue));
        sumGainedField.setText(String.valueOf(sumGained));

        if (Double.parseDouble(String.valueOf(estimation)) > 0)
        {
            estimationField.setText("+" + estimation);
        }
        else
        {
            estimationField.setText(String.valueOf(estimation));
        }

        dayCounterField.setText(String.valueOf(dayCount));
    }

    private void initColumns()
    {
        statsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        statsSoldLast.setCellValueFactory(new PropertyValueFactory<>("soldLast"));
        statsRevenueLast.setCellValueFactory(new PropertyValueFactory<>("revenueLast"));
        statsSold.setCellValueFactory(new PropertyValueFactory<>("sold"));
        statsSpent.setCellValueFactory(new PropertyValueFactory<>("spent"));
        statsRevenue.setCellValueFactory(new PropertyValueFactory<>("revenue"));
        statsGained.setCellValueFactory(new PropertyValueFactory<>("gained"));
        statsLeftInStore.setCellValueFactory(new PropertyValueFactory<>("leftInStore"));
        statsLeftInWarehouse.setCellValueFactory(new PropertyValueFactory<>("leftInWarehouse"));

        statsEstimationReal.setCellValueFactory(new PropertyValueFactory<>("estimationReal"));
        statsEstimationTheoretical.setCellValueFactory(new PropertyValueFactory<>("estimationTheoretical"));
        statsEstimationName.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    public void estimator()
    {
        ObservableList<AllProducts> inputs = loadAllProductsData();
        ObservableList<Stats> stats = loadStatsData();

        for (int i = 0; i < inputs.size(); i++)
        {
            int days = 0;
            BigDecimal soldDaily = BigDecimal.valueOf(Double.valueOf(inputs.get(i).getMaxSold() + inputs.get(i).getMinSold()) / 2);
            BigDecimal amountInStore;
            BigDecimal amountInWarehouse;
            BigDecimal revenue = inputs.get(i).getBuyPrice().multiply(BigDecimal.valueOf(inputs.get(i).getAmountInWarehouse())).negate();



            if (inputs.get(i).getAmountInStore() > inputs.get(i).getAmountInWarehouse())
            {
                amountInWarehouse = BigDecimal.valueOf(0);
                amountInStore = BigDecimal.valueOf(inputs.get(i).getAmountInWarehouse());
            }
            else
            {
                amountInStore = BigDecimal.valueOf(inputs.get(i).getAmountInStore());
                amountInWarehouse = BigDecimal.valueOf(inputs.get(i).getAmountInWarehouse()).subtract(amountInStore);
            }

            //System.out.print(amountInWarehouse + " " + amountInStore + " ");
            //System.out.println("");
            for (int j = 0; j < 30; j++) {
                if (days == 7)
                {
                    if (Double.parseDouble(String.valueOf(amountInWarehouse)) >= inputs.get(i).getAmountInStore())
                    {
                        amountInWarehouse = amountInWarehouse.subtract(BigDecimal.valueOf(inputs.get(i).getAmountInStore()));
                        amountInStore = BigDecimal.valueOf(inputs.get(i).getAmountInStore());
                    }
                    else
                    {
                        if (Double.parseDouble(String.valueOf(amountInWarehouse)) + Double.parseDouble(String.valueOf(amountInStore)) >= inputs.get(i).getAmountInStore())
                        {
                            amountInWarehouse = BigDecimal.valueOf(inputs.get(i).getAmountInStore() - Double.parseDouble(String.valueOf(amountInStore)));
                            amountInStore = BigDecimal.valueOf(inputs.get(i).getAmountInStore());
                        }
                        else
                        {
                            amountInStore = amountInStore.add(amountInWarehouse);
                            amountInWarehouse = BigDecimal.valueOf(0);
                        }
                    }
                }
                days++;
                //System.out.print(amountInWarehouse + " " + amountInStore + " ");
                //System.out.println("");

                if (Double.parseDouble(String.valueOf(soldDaily)) < Double.parseDouble(String.valueOf(amountInStore)))
                {
                    amountInStore = amountInStore.subtract(soldDaily);
                    revenue = revenue.add(inputs.get(i).getSellPrice().multiply(soldDaily)).setScale(2, RoundingMode.HALF_UP);
                }
                else
                {
                    revenue = revenue.add(inputs.get(i).getSellPrice().multiply(amountInStore)).setScale(2, RoundingMode.HALF_UP);
                    if (Double.parseDouble(String.valueOf(amountInWarehouse)) >= inputs.get(i).getAmountInStore())
                    {
                        amountInWarehouse = amountInWarehouse.subtract(BigDecimal.valueOf(inputs.get(i).getAmountInStore()));
                        amountInStore = BigDecimal.valueOf(inputs.get(i).getAmountInStore());
                    }
                    else
                    {
                        if (Double.parseDouble(String.valueOf(amountInWarehouse)) + Double.parseDouble(String.valueOf(amountInStore)) >= inputs.get(i).getAmountInStore())
                        {
                            amountInWarehouse = BigDecimal.valueOf(inputs.get(i).getAmountInStore() - Double.parseDouble(String.valueOf(amountInStore)));
                            amountInStore = BigDecimal.valueOf(inputs.get(i).getAmountInStore());
                        }
                        else
                        {
                            amountInStore = amountInWarehouse;
                            amountInWarehouse = BigDecimal.valueOf(0);
                        }
                    }
                }
                //System.out.print(amountInWarehouse + " " + amountInStore + " ");
                //System.out.println("");
                //System.out.println(revenue);
                if ((amountInStore.equals(BigDecimal.valueOf(0))) && (amountInWarehouse.equals(BigDecimal.valueOf(0))))
                {
                    break;
                }
            }

            if ((amountInStore.equals(BigDecimal.valueOf(0))) && (amountInWarehouse.equals(BigDecimal.valueOf(0))))
            {
                stats.get(i).setEstimationTheoretical("Товар закончился на " + days + " день. Полученная прибыль: " + revenue.setScale(2, RoundingMode.HALF_UP));
                continue;
            }
            stats.get(i).setEstimationTheoretical("На складе осталось " + amountInWarehouse.setScale(0, RoundingMode.HALF_UP) + " товаров, в магазине осталось " + amountInStore.setScale(0, RoundingMode.HALF_UP) + " товаров. Полученная прибыль: " + revenue.setScale(2, RoundingMode.HALF_UP));
        }
        saveStatsData(stats);
    }

    public void fillTable()
    {
        ObservableList<Stats> stats = loadStatsData();

        for (int i = 0; i < stats.size(); i++)
        {
            statsTable.getItems().add(stats.get(i));
            estimationStatsTable.getItems().add(stats.get(i));
        }
    }
}
