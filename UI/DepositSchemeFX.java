package com.green.bank.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.green.bank.model.DepositSchemeModel;
import com.green.bank.database.DatabaseOperations;

public class DepositSchemeFX extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("Investment Schemes");
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        TextField accField = new TextField();
        accField.setPromptText("Account Number");

        ComboBox<String> yearBox = new ComboBox<>();
        yearBox.getItems().addAll("1", "2", "3", "5");

        ComboBox<String> amountBox = new ComboBox<>();
        amountBox.getItems().addAll("1,00,000", "3,00,000", "5,00,000");

        Button investBtn = new Button("Start Scheme");
        investBtn.setMaxWidth(Double.MAX_VALUE);

        investBtn.setOnAction(e -> {
            try {
                String selectedAmt = amountBox.getValue().replace(",", "");
                int amount = Integer.parseInt(selectedAmt);

                DepositSchemeModel ds = new DepositSchemeModel();
                ds.setAccount_no(accField.getText());
                ds.setYear(Integer.parseInt(yearBox.getValue()));
                ds.setAmount(amount);

                boolean success = new DatabaseOperations().insertDepositScheme(ds);
                if (success) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Scheme Activated!").show();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error activating scheme").show();
            }
        });

        layout.getChildren().addAll(new Label("Account:"), accField, new Label("Duration (Years):"), yearBox, new Label("Amount:"), amountBox, investBtn);
        stage.setScene(new Scene(layout, 350, 400));
        stage.show();
    }
}