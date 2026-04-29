package com.green.bank.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.green.bank.database.DatabaseOperations;

public class ReturnLoanFX extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("Green Bank - Return Loan");

        VBox root = new VBox(15);
        root.setPadding(new Insets(25));
        root.setAlignment(Pos.CENTER);

        Label title = new Label("Loan Repayment");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField amountField = new TextField();
        amountField.setPromptText("Enter Amount to Repay");

        Button payBtn = new Button("Pay Now");
        payBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        payBtn.setMaxWidth(Double.MAX_VALUE);

        payBtn.setOnAction(e -> {
            try {
                double repayAmt = Double.parseDouble(amountField.getText());

                if (repayAmt <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Amount", "Please enter a positive value.");
                    return;
                }

                DatabaseOperations ops = new DatabaseOperations();
                // 1. Logic: Call the backend to deduct from balance and update loan status
                boolean success = ops.repayLoan(LoginFX.currentUser.getAccount_no(), repayAmt);

                if (success) {
                    Platform.runLater(() -> {
                        // 2. Refresh the Dashboard balance and loan labels instantly
                        DashboardFX.refresh();
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Loan repayment successful!");
                        stage.close();
                    });
                } else {
                    showAlert(Alert.AlertType.ERROR, "Payment Failed", "Insufficient funds in your account.");
                }

            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter a valid numeric amount.");
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "System Error", "An error occurred while processing repayment.");
            }
        });

        root.getChildren().addAll(title, new Label("Amount to Repay:"), amountField, payBtn);

        Scene scene = new Scene(root, 300, 300);
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
