package com.green.bank.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.sql.*;

import com.green.bank.database.JDBC_Connect;
import com.green.bank.model.AccountModel;

public class LoginFX extends Application {

    public static AccountModel currentUser = null;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Green Bank - Login"); //Set Title

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        Button loginBtn = new Button("Login");
        Button createBtn = new Button("Create Account");

        // Styling
        loginBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);

        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().addAll(createBtn, loginBtn);
        grid.add(hbBtn, 1, 2);

        loginBtn.setOnAction(e -> handleLogin(usernameField.getText(), passwordField.getText(), primaryStage));
        createBtn.setOnAction(e -> new CreateAccountFX().start(new Stage()));

        Scene scene = new Scene(grid, 350, 250);
        primaryStage.setScene(scene); //Pass the scene to stage 
        primaryStage.show(); //Method to show primary stage
    }

    private void handleLogin(String username, String password, Stage stage) {
        try {
            JDBC_Connect connect = new JDBC_Connect();
            Connection conn = connect.getConnection();

            // 1. Fetch user credentials and personal info
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM account WHERE username=? AND password=?");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password!");
                conn.close();
                return;
            }

            // 2. Map Database columns to the AccountModel
            AccountModel am = new AccountModel();
            am.setAccount_no(rs.getString(1));   // id
            am.setFirst_name(rs.getString(2));   // first_name
            am.setLast_name(rs.getString(3));    // last_name
            am.setAddress(rs.getString(4));      // address
            am.setCity(rs.getString(5));         // city

            am.setBranch(rs.getString(6));       // branch
            am.setZip(rs.getString(7));          // zip
            am.setUsername(rs.getString(8));     // username
            am.setPassword(rs.getString(9));     // password
            am.setPhone_number(rs.getString(10));// phone
            am.setEmail(rs.getString(11));       // email
            am.setAccount_type(rs.getString(12));// account_type
            am.setReg_date(rs.getString(13));    // reg_date

            // 3. Fetch Balance from the 'amount' table
            PreparedStatement ps2 = conn.prepareStatement("SELECT amount FROM amount WHERE id=?");
            ps2.setString(1, am.getAccount_no());
            ResultSet rs2 = ps2.executeQuery();
            if (rs2.next()) {
                am.setAmount(rs2.getDouble(1));
            }

            // 4. Set "Session" and switch screens
            currentUser = am;
            conn.close();

            // Open Dashboard
            new DashboardFX().start(new Stage());
            stage.close(); // Close login window

        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not connect to the banking server.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) { launch(args); }
}