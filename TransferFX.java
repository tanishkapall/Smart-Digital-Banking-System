package com.green.bank.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.*;
import com.green.bank.database.JDBC_Connect;

public class TransferFX extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Green Bank - Transfer Money");
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(25));
        layout.setAlignment(Pos.CENTER);

        Label title = new Label("Secure Money Transfer");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField targetAcc = new TextField();
        targetAcc.setPromptText("Target Account Number");

        TextField amountField = new TextField();
        amountField.setPromptText("Amount (e.g., 100.50)");

        PasswordField passField = new PasswordField();
        passField.setPromptText("Your Password");

        Button transferBtn = new Button("Transfer Now");
        transferBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");
        transferBtn.setMaxWidth(Double.MAX_VALUE);

        transferBtn.setOnAction(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                if (amount <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Amount", "Please enter a positive value.");
                    return;
                }
                processTransfer(targetAcc.getText(), amount, passField.getText(), stage);
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid numeric amount.");
            }
        });

        layout.getChildren().addAll(title, new Label("Recipient Account:"), targetAcc,
                new Label("Amount:"), amountField,
                new Label("Confirm Password:"), passField, transferBtn);

        Scene scene = new Scene(layout, 350, 450);
        stage.setScene(scene);
        stage.show();
    }

    private void processTransfer(String targetAcc, double amount, String pswd, Stage stage) {
        Connection conn = null;
        try {
            JDBC_Connect connect = new JDBC_Connect();
            conn = connect.getConnection();
            conn.setAutoCommit(false); // Start transaction for safety

            String senderAcc = LoginFX.currentUser.getAccount_no();

            // 1. Verify Sender's Password
            if (!pswd.equals(LoginFX.currentUser.getPassword())) {
                showAlert(Alert.AlertType.ERROR, "Authentication Failed", "Incorrect password.");
                conn.close();
                return;
            }

            // 2. Check if Recipient Exists
            PreparedStatement psTarget = conn.prepareStatement("SELECT * FROM account WHERE id=?");
            psTarget.setString(1, targetAcc);
            if (!psTarget.executeQuery().next()) {
                showAlert(Alert.AlertType.ERROR, "Recipient Not Found", "The target account number does not exist.");
                conn.close();
                return;
            }

            // 3. Check Sender Balance
            PreparedStatement psGetBal = conn.prepareStatement("SELECT amount FROM amount WHERE id=?");
            psGetBal.setString(1, senderAcc);
            ResultSet rsBal = psGetBal.executeQuery();
            double currentBal = 0;
            if (rsBal.next()) currentBal = rsBal.getDouble(1);

            if (currentBal < amount) {
                showAlert(Alert.AlertType.WARNING, "Insufficient Funds", "Your balance is too low for this transfer.");
                conn.close();
                return;
            }

            // 4. Deduct from Sender
            PreparedStatement psDeduct = conn.prepareStatement("UPDATE amount SET amount=amount-? WHERE id=?");
            psDeduct.setDouble(1, amount);
            psDeduct.setString(2, senderAcc);
            psDeduct.executeUpdate();

            // 5. Add to Recipient
            PreparedStatement psAdd = conn.prepareStatement("UPDATE amount SET amount=amount+? WHERE id=?");
            psAdd.setDouble(1, amount);
            psAdd.setString(2, targetAcc);
            psAdd.executeUpdate();

            // 6. Commit Transaction
            conn.commit();

            Platform.runLater(() -> {
                DashboardFX.refresh(); // Update dashboard balance instantly
                showAlert(Alert.AlertType.INFORMATION, "Success", "Transfer of $" + amount + " successful!");
                stage.close();
            });

        } catch (Exception ex) {
            ex.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ignored) {}
            showAlert(Alert.AlertType.ERROR, "Transfer Failed", "An error occurred during the transaction.");
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException ignored) {}
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