package com.green.bank.ui;
#importing required libraries
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.sql.*;
import com.green.bank.database.JDBC_Connect;

public class DepositFX extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Deposit Funds");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20));
        grid.setHgap(10); grid.setVgap(10);

        TextField accField = new TextField();
        TextField userField = new TextField();
        PasswordField passField = new PasswordField();
        TextField amountField = new TextField();
        Button depositBtn = new Button("Deposit");
        depositBtn.setStyle("-fx-background-color: #2e7d32; -fx-text-fill: white;");

        grid.add(new Label("Account No:"), 0, 0); grid.add(accField, 1, 0);
        grid.add(new Label("Username:"), 0, 1); grid.add(userField, 1, 1);
        grid.add(new Label("Password:"), 0, 2); grid.add(passField, 1, 2);
        grid.add(new Label("Amount:"), 0, 3); grid.add(amountField, 1, 3);
        grid.add(depositBtn, 1, 4);

        depositBtn.setOnAction(e -> {
            try {
                // Support for decimal values
                double amount = Double.parseDouble(amountField.getText());
                if (amount <= 0) {
                    showAlert("Error", "Please enter an amount greater than zero.");
                    return;
                }
                handleDeposit(accField.getText(), userField.getText(), passField.getText(), amount);
            } catch (NumberFormatException ex) {
                showAlert("Error", "Please enter a valid decimal number (e.g., 100.50).");
            }
        });

        stage.setScene(new Scene(grid, 350, 300));
        stage.show();
    }

    private void handleDeposit(String acc, String user, String pass, double amount) {
        Connection conn = null;
        try {
            JDBC_Connect connect = new JDBC_Connect();
            conn = connect.getConnection();

            // Start transaction for data integrity
            conn.setAutoCommit(false);

            // 1. Validate Credentials
            PreparedStatement check = conn.prepareStatement(
                    "SELECT * FROM account WHERE id=? AND username=? AND password=?"
            );
            check.setString(1, acc);
            check.setString(2, user);
            check.setString(3, pass);

            ResultSet rs = check.executeQuery();
            if (!rs.next()) {
                showAlert("Error", "Invalid credentials! Please check your Account No, Username, and Password.");
                conn.close();
                return;
            }

            // 2. Get Current Balance (using getDouble for decimals)
            PreparedStatement getBal = conn.prepareStatement("SELECT amount FROM amount WHERE id=?");
            getBal.setString(1, acc);
            ResultSet rsBal = getBal.executeQuery();

            double currentBalance = 0;
            if (rsBal.next()) {
                currentBalance = rsBal.getDouble(1);
            }

            // 3. Update Balance
            double newBalance = currentBalance + amount;
            PreparedStatement update = conn.prepareStatement("UPDATE amount SET amount=? WHERE id=?");
            update.setDouble(1, newBalance);
            update.setString(2, acc);

            int rowsUpdated = update.executeUpdate();

            if (rowsUpdated > 0) {
                // 4. Commit transaction
                conn.commit();

                // 5. Update UI in real-time
                javafx.application.Platform.runLater(() -> {
                    DashboardFX.refresh(); // This updates the balance on the Dashboard instantly
                    showAlert("Success", String.format("Deposit Successful! New Balance: $%.2f", newBalance));
                });
            } else {
                conn.rollback();
                showAlert("Error", "Transaction failed. Please try again.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException se) {
                se.printStackTrace();
            }
            showAlert("Error", "A database error occurred: " + ex.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
