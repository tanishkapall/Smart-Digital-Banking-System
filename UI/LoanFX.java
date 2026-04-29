package com.green.bank.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import com.green.bank.model.LoanModel;
import com.green.bank.database.DatabaseOperations;

public class LoanFX extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Green Bank - Apply for Loan");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(25));
        grid.setHgap(10);
        grid.setVgap(10);

        Label title = new Label("Instant Loan Application");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Fields (Auto-filled from session where possible for better UX)
        TextField accNoField = new TextField(LoginFX.currentUser.getAccount_no());
        accNoField.setEditable(false); // Prevents user from changing their own ID

        TextField fNameField = new TextField(LoginFX.currentUser.getFirst_name());
        fNameField.setEditable(false);

        TextField amountField = new TextField();
        amountField.setPromptText("Enter Loan Amount");

        Button submitBtn = new Button("Apply & Get Instant Cash");
        submitBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");
        submitBtn.setMaxWidth(Double.MAX_VALUE);

        // Add to grid
        grid.add(title, 0, 0, 2, 1);
        grid.add(new Label("Account No:"), 0, 1);
        grid.add(accNoField, 1, 1);
        grid.add(new Label("First Name:"), 0, 2);
        grid.add(fNameField, 1, 2);
        grid.add(new Label("Loan Amount:"), 0, 3);
        grid.add(amountField, 1, 3);
        grid.add(submitBtn, 1, 4);

        submitBtn.setOnAction(e -> {
            try {
                double loanAmount = Double.parseDouble(amountField.getText());

                if (loanAmount <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Amount", "Please enter a positive loan amount.");
                    return;
                }

                applyInstantLoan(loanAmount, stage);

            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter a valid numeric amount.");
            }
        });

        stage.setScene(new Scene(grid, 400, 350));
        stage.show();
    }

    private void applyInstantLoan(double amount, Stage stage) {
        try {
            // 1. Create Model with 'success' status for instant approval
            LoanModel lm = new LoanModel();
            lm.setAccount_no(LoginFX.currentUser.getAccount_no());
            lm.setFirst_name(LoginFX.currentUser.getFirst_name());
            lm.setLast_name(LoginFX.currentUser.getLast_name());
            lm.setAddress(LoginFX.currentUser.getAddress());
            lm.setEmail(LoginFX.currentUser.getEmail());
            lm.setLoan_amount(amount);
            lm.setStatus("success"); // Instant approval logic

            DatabaseOperations ops = new DatabaseOperations();

            // 2. Insert record into loan table
            boolean recordSaved = ops.insertLoanDetails(lm);

            if (recordSaved) {
                // 3. Update the 'amount' table to add the cash to balance
                // Note: Casting to int if your DatabaseOperations.updateAmount still uses int
                ops.updateAmount(lm.getAccount_no(), amount);

                // 4. Update UI and Dashboard
                Platform.runLater(() -> {
                    DashboardFX.refresh(); // Balance will jump up immediately
                    showAlert(Alert.AlertType.INFORMATION, "Loan Granted",
                            String.format("Successfully credited $%.2f to your account!", amount));
                    stage.close();
                });
            } else {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Could not process loan at this time.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "System Error", "An error occurred while processing your loan.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}