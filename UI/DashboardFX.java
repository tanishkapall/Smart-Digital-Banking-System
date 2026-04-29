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
import com.green.bank.database.JDBC_Connect;
import com.green.bank.model.LoanModel;
import java.sql.Connection;

public class DashboardFX extends Application {
    private static DashboardFX instance;
    private Label balanceLabel, loanLabel, branchLabel, typeLabel, accNoLabel;

    // This allows other windows (Deposit/Withdraw) to trigger a UI refresh
    public static void refresh() {
        if (instance != null) {
            instance.updateUI();
        }
    }

    @Override
    public void start(Stage stage) {
        instance = this;
        stage.setTitle("Green Bank Dashboard");

        Label welcomeLabel = new Label("Welcome, " + LoginFX.currentUser.getFirst_name());
        welcomeLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        // Initialize dynamic labels
        branchLabel = new Label("Branch: " + LoginFX.currentUser.getBranch());
        typeLabel = new Label("Account Type: " + LoginFX.currentUser.getAccount_type());
        accNoLabel = new Label("Account No: " + LoginFX.currentUser.getAccount_no());
        balanceLabel = new Label();
        loanLabel = new Label();

        // Style dynamic labels
        balanceLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #2e7d32; -fx-font-weight: bold;");
        loanLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #d32f2f;");

        // Create Buttons
        Button depositBtn = new Button("Deposit");
        Button withdrawBtn = new Button("Withdraw");
        Button transferBtn = new Button("Transfer");
        Button loanBtn = new Button("Apply Loan");
        Button returnLoanBtn = new Button("Return Loan");

        // Set all buttons to full width for a cleaner look
        depositBtn.setMaxWidth(Double.MAX_VALUE);
        withdrawBtn.setMaxWidth(Double.MAX_VALUE);
        transferBtn.setMaxWidth(Double.MAX_VALUE);
        loanBtn.setMaxWidth(Double.MAX_VALUE);
        returnLoanBtn.setMaxWidth(Double.MAX_VALUE);

        VBox layout = new VBox(12);
        layout.setPadding(new Insets(25));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(
                welcomeLabel, branchLabel, typeLabel, accNoLabel,
                new Separator(),
                balanceLabel, loanLabel,
                new Separator(),
                depositBtn, withdrawBtn, transferBtn, loanBtn, returnLoanBtn
        );

        // Action Handlers
        depositBtn.setOnAction(e -> launchWindow(new DepositFX()));
        withdrawBtn.setOnAction(e -> launchWindow(new WithdrawFX()));
        transferBtn.setOnAction(e -> launchWindow(new TransferFX()));
        loanBtn.setOnAction(e -> launchWindow(new LoanFX()));
        returnLoanBtn.setOnAction(e -> launchWindow(new ReturnLoanFX()));

        updateUI(); // Initial data load

        Scene scene = new Scene(layout, 400, 550);
        stage.setScene(scene);
        stage.show();
    }

    private void launchWindow(Application app) {
        try {
            app.start(new Stage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // This pulls fresh data from the DB to update the screen
    public void updateUI() {
        Platform.runLater(() -> {
            try {
                JDBC_Connect connect = new JDBC_Connect();
                Connection conn = connect.getConnection();
                DatabaseOperations ops = new DatabaseOperations();

                // 1. Refresh global user data (Balance, etc.)
                LoginFX.currentUser = ops.getAccountDetails(conn, LoginFX.currentUser.getAccount_no());

                // 2. Update Balance Display
                balanceLabel.setText("Current Balance: $" + String.format("%.2f", LoginFX.currentUser.getAmount()));

                // 3. Update Loan Status Display
                LoanModel lm = ops.getLoanStatus(LoginFX.currentUser.getAccount_no());
                if (lm != null && lm.getStatus().equals("success")) {
                    loanLabel.setText("Active Loan: $" + String.format("%.2f", lm.getLoan_amount()));
                } else {
                    loanLabel.setText("No Active Loans");
                }

                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}