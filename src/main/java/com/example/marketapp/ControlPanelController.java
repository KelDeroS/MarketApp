package com.example.marketapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

import static com.example.marketapp.MarketAppMain.*;

public class ControlPanelController {
    @FXML
    private Label dayCounterField;

    @FXML
    private Button controlPanel1DayButton;

    @FXML
    private Button controlPanel30DayButton;

    @FXML
    private Button controlPanel7DayButton;

    @FXML
    private Button controlPanelAllProductsButton;

    @FXML
    private Button controlPanelBackButton;

    @FXML
    private Button controlPanelStatsButton;

    @FXML
    void initialize()
    {
        controlPanelAllProductsButton.setOnAction(e->allProductsButtonClicked());
        controlPanelBackButton.setOnAction(e->backButtonClicked());
        controlPanelStatsButton.setOnAction(e->statsButtonClicked());
        controlPanel1DayButton.setOnAction(e->dayButtonClicked());
        controlPanel7DayButton.setOnAction(e->weekButtonClicked());
        controlPanel30DayButton.setOnAction(e->monthButtonClicked());
    }

    public void allProductsButtonClicked()
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AllProductsView.fxml"));

        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setTitle("Входные данные");
        stage.show();
    }

    public void backButtonClicked()
    {
        dayCount = 0;
        controlPanelBackButton.getScene().getWindow().hide();
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

    public void statsButtonClicked()
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("StatsView.fxml"));

        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setTitle("Статистика");
        stage.show();
    }

    public int randomSold(int i)
    {
        Random rand = new Random();
        ObservableList<AllProducts> inputs = loadAllProductsData();

        int max = inputs.get(i).getMaxSold();
        int min = inputs.get(i).getMinSold();
        if (max == min)
        {
            return max;
        }
        else {
            return rand.nextInt(max - min) + min;
        }
    }

    public void iteration()
    {
        ObservableList<AllProducts> inputs = loadAllProductsData();
        ObservableList<Stats> stats = loadStatsData();

        if (dayCount % 30 == 0)
        {
            refillWarehouse();
        }
        if (dayCount % 7 == 0)
        {
            for (int i = 0; i < inputs.size(); i++)
            {
                refillStore(i);
            }
        }
        dayCount++;
        dayCounterField.setText(String.valueOf(dayCount));
        ObservableList<Stats> lastIteration = FXCollections.observableArrayList();
        estimation = BigDecimal.valueOf(0);

        for (int i = 0; i < inputs.size(); i++)
        {
            Stats tmp = new Stats();
            int trySell, oldTrySell;

            oldTrySell = randomSold(i); //120
            trySell = checkStore(i, oldTrySell); //53

            if (trySell == oldTrySell)
            {
                stats.get(i).setLeftInStore(stats.get(i).getLeftInStore() - trySell);
            }

            stats.get(i).setSold(stats.get(i).getSold() + trySell);
            stats.get(i).setRevenue(inputs.get(i).getSellPrice().multiply(BigDecimal.valueOf(stats.get(i).getSold())));
            stats.get(i).setGained(stats.get(i).getGained().add(inputs.get(i).getSellPrice().multiply(BigDecimal.valueOf(trySell))).setScale(2, RoundingMode.HALF_UP));
            if (stats.get(i).getLeftInWarehouse() == 0 && stats.get(i).getLeftInStore() == 0)
            {
                stats.get(i).setEstimationReal("Товар кончился, в этом месяце получено: " + stats.get(i).getGained());
            }
            else
            {
                BigDecimal income = inputs.get(i).getSellPrice(). multiply(BigDecimal.valueOf(stats.get(i).getSold())). multiply(BigDecimal.valueOf(Double.valueOf(((dayCount / 30) + 1) * 30) / dayCount)).subtract(stats.get(i).getSpent()).setScale(2, RoundingMode.HALF_UP);
                BigDecimal maxPossible = inputs.get(i).getSellPrice().multiply(BigDecimal.valueOf(inputs.get(i).getAmountInWarehouse())).subtract(stats.get(i).getSpent());
                if (Double.parseDouble(String.valueOf(maxPossible)) < Double.parseDouble(String.valueOf(income)))
                {
                    stats.get(i).setEstimationReal("К концу месяца ожидается прибыль в размере:" + maxPossible);
                    estimation = estimation.add(maxPossible);
                }
                else {
                    stats.get(i).setEstimationReal("К концу месяца ожидается прибыль в размере:" + income);
                    estimation = estimation.add(income);
                }
            }
            tmp.setName(inputs.get(i).getName());
            tmp.setSoldLast(trySell);
            tmp.setRevenueLast(inputs.get(i).getSellPrice().multiply(BigDecimal.valueOf(trySell)).setScale(2, RoundingMode.HALF_UP));
            lastIteration.add(tmp);
            saveStatsData(stats);
        }
        saveLastIterationData(lastIteration);
    }

    public void refillWarehouse()
    {
        ObservableList<AllProducts> products = loadAllProductsData();
        ObservableList<Stats> stats = loadStatsData();

        for (int i = 0; i < stats.size(); i++)
        {
            stats.get(i).setLeftInWarehouse(products.get(i).getAmountInWarehouse());
            stats.get(i).setSpent(stats.get(i).getSpent().add(products.get(i).getBuyPrice().multiply(BigDecimal.valueOf(products.get(i).getAmountInWarehouse()))));
            stats.get(i).setGained(stats.get(i).getGained().subtract(products.get(i).getBuyPrice().multiply(BigDecimal.valueOf(products.get(i).getAmountInWarehouse()))));
        }

        saveStatsData(stats);
    }

    public void refillStore(int i)
    {
        ObservableList<AllProducts> products = loadAllProductsData();
        ObservableList<Stats> stats = loadStatsData();

        int tryRefill = products.get(i).getAmountInStore() - stats.get(i).getLeftInStore();
        //System.out.println(tryRefill + " " + stats.get(i).getLeftInWarehouse() + " " + stats.get(i).getLeftInStore());
        if (stats.get(i).getLeftInWarehouse() >= tryRefill)
        {
            stats.get(i).setLeftInWarehouse(stats.get(i).getLeftInWarehouse() - tryRefill);
            stats.get(i).setLeftInStore(products.get(i).getAmountInStore());
        }
        else
        {
            stats.get(i).setLeftInStore(stats.get(i).getLeftInStore() + stats.get(i).getLeftInWarehouse());
            stats.get(i).setLeftInWarehouse(0);
        }

        saveStatsData(stats);
    }

    public int checkStore(int i, int trySell)
    {
        ObservableList<Stats> stats = loadStatsData();
        int left = stats.get(i).getLeftInStore();
        //System.out.println(left + " " + trySell);

        if (left >= trySell)
        {
            return trySell;
        }
        else
        {
            stats.get(i).setLeftInStore(0);
            refillStore(i);
            saveStatsData(stats);
            return left;
        }
    }

    public void dayButtonClicked()
    {
        iteration();
        saveLastPeriodData(loadLastIterationData());
        fillLastPeriodData();
    }

    public void weekButtonClicked()
    {
        ObservableList<Stats> lastPeriod = FXCollections.observableArrayList();

        iteration();
        ObservableList<Stats> lastIteration = loadLastIterationData();
        for (int i = 0; i < lastIteration.size(); i++)
        {
            Stats tmp = new Stats();
            tmp.setName(lastIteration.get(i).getName());
            tmp.setSoldLast(lastIteration.get(i).getSoldLast());
            tmp.setRevenueLast(lastIteration.get(i).getRevenueLast());
            lastPeriod.add(tmp);
        }

        for (int i = 1; i < 7; i++)
        {
            iteration();
            lastIteration = loadLastIterationData();
            for (int j = 0; j < lastIteration.size(); j++)
            {
                lastPeriod.get(j).setSoldLast(lastPeriod.get(j).getSoldLast() + lastIteration.get(j).getSoldLast());
                lastPeriod.get(j).setRevenueLast(lastPeriod.get(j).getRevenueLast().add(lastIteration.get(j).getRevenueLast()));
            }
        }


        saveLastPeriodData(lastPeriod);
        fillLastPeriodData();
    }

    public void monthButtonClicked()
    {
        ObservableList<Stats> lastPeriod = FXCollections.observableArrayList();

        iteration();
        ObservableList<Stats> lastIteration = loadLastIterationData();
        for (int i = 0; i < lastIteration.size(); i++)
        {
            Stats tmp = new Stats();
            tmp.setName(lastIteration.get(i).getName());
            tmp.setSoldLast(lastIteration.get(i).getSoldLast());
            tmp.setRevenueLast(lastIteration.get(i).getRevenueLast());
            lastPeriod.add(tmp);
        }

        for (int i = 1; i < 30; i++)
        {
            iteration();
            lastIteration = loadLastIterationData();
            for (int j = 0; j < lastIteration.size(); j++)
            {
                lastPeriod.get(0).setSoldLast(lastPeriod.get(0).getSoldLast() + lastIteration.get(j).getSoldLast());
                lastPeriod.get(0).setRevenueLast(lastPeriod.get(0).getRevenueLast().add(lastIteration.get(j).getRevenueLast()));
            }
        }

        saveLastPeriodData(lastPeriod);
        fillLastPeriodData();
    }

    public void fillLastPeriodData()
    {
        ObservableList<Stats> lastPeriod = loadLastPeriodData();
        ObservableList<Stats> stats = loadStatsData();
        for (int i = 0; i < lastPeriod.size(); i++)
        {
            stats.get(i).setSoldLast(lastPeriod.get(i).getSoldLast());
            stats.get(i).setRevenueLast(lastPeriod.get(i).getRevenueLast());
        }
        saveStatsData(stats);
    }

    public void estimate(int i)
    {

    }
}
