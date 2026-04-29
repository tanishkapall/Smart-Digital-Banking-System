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

public class WithdrawFX extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Green Bank - Withdraw Cash");

        VBox root = new VBox(15);
        root.setPadding(new Insets(25));
        root.setAlignment(Pos.CENTER);

        Label title = new Label("Withdrawal Details");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField accField = new TextField();
        accField.setPromptText("Account No");

        TextField userField = new TextField();
        userField.setPromptText("Username");

        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");

        TextField amountField = new TextField();
        amountField.setPromptText("Amount (e.g. 50.00)");

        Button withdrawBtn = new Button("Withdraw Cash");
        withdrawBtn.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white; -fx-font-weight: bold;");
        withdrawBtn.setMaxWidth(Double.MAX_VALUE);

        withdrawBtn.setOnAction(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                if (amount <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Amount", "Please enter a positive value.");
                    return;
                }
                processWithdraw(accField.getText(), userField.getText(), passField.getText(), amount);
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid numeric amount.");
            }
        });

        root.getChildren().addAll(title, accField, userField, passField, amountField, withdrawBtn);

        Scene scene = new Scene(root, 350, 450);
        stage.setScene(scene);
        stage.show();
    }

    private void processWithdraw(String acc, String user, String pass, double amount) {
        Connection conn = null;
        try {
            JDBC_Connect connect = new JDBC_Connect();
            conn = connect.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // 1. Validate Credentials (The logic from your original Swing version)
            PreparedStatement psCheck = conn.prepareStatement(
                    "SELECT * FROM account WHERE id=? AND username=? AND password=?"
            );
            psCheck.setString(1, acc);
            psCheck.setString(2, user);
            psCheck.setString(3, pass);

            ResultSet rsUser = psCheck.executeQuery();
            if (!rsUser.next()) {
                showAlert(Alert.AlertType.ERROR, "Authentication Failed", "Invalid Account No, Username, or Password.");
                conn.close();
                return;
            }

            // 2. Check Balance
            PreparedStatement psBal = conn.prepareStatement("SELECT amount FROM amount WHERE id=?");
            psBal.setString(1, acc);
            ResultSet rsBal = psBal.executeQuery();

            if (rsBal.next()) {
                double currentBalance = rsBal.getDouble(1);

                if (currentBalance >= amount) {
                    // 3. Perform Withdrawal
                    double newBalance = currentBalance - amount;
                    PreparedStatement psUpdate = conn.prepareStatement("UPDATE amount SET amount=? WHERE id=?");
                    psUpdate.setDouble(1, newBalance);
                    psUpdate.setString(2, acc);
                    psUpdate.executeUpdate();

                    // 4. Commit and Refresh
                    conn.commit();

                    Platform.runLater(() -> {
                        DashboardFX.refresh(); // Update real-time balance on dashboard
                        showAlert(Alert.AlertType.INFORMATION, "Success",
                                String.format("Withdrawal Successful!\nRemaining Balance: $%.2f", newBalance));
                    });
                } else {
                    showAlert(Alert.AlertType.WARNING, "Insufficient Funds", "You do not have enough balance for this withdrawal.");
                }
            }

            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ignored) {}
            showAlert(Alert.AlertType.ERROR, "System Error", "Withdrawal failed due to a database error.");
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