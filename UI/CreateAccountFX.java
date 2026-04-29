package com.green.bank.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.green.bank.model.AccountModel;
import com.green.bank.database.DatabaseOperations;

public class CreateAccountFX extends Application {

    // Define fields as class members for access in handleRegistration
    private TextField firstNameField, lastNameField, addressField, cityField, branchField;
    private TextField zipField, usernameField, phoneField, emailField, amountField;
    private PasswordField passwordField, rePasswordField;
    private ComboBox<String> accountTypeBox; //DropDown Menu

    @Override
    public void start(Stage stage) {
        stage.setTitle("Green Bank - Open New Account");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(25));
        grid.setHgap(10);
        grid.setVgap(10);

        // Initialize Fields
        firstNameField = new TextField();
        lastNameField = new TextField();
        addressField = new TextField();
        cityField = new TextField();
        branchField = new TextField();
        zipField = new TextField();
        usernameField = new TextField();
        passwordField = new PasswordField();
        rePasswordField = new PasswordField();
        phoneField = new TextField();
        emailField = new TextField();
        amountField = new TextField();

        accountTypeBox = new ComboBox<>();
        accountTypeBox.getItems().addAll("Savings", "Current", "Student");
        accountTypeBox.getSelectionModel().selectFirst();

        // Add UI Elements to Grid
        addRow(grid, "First Name:", firstNameField, 0);
        addRow(grid, "Last Name:", lastNameField, 1);
        addRow(grid, "Address:", addressField, 2);
        addRow(grid, "City:", cityField, 3);
        addRow(grid, "Branch:", branchField, 4);
        addRow(grid, "Zip Code:", zipField, 5);
        addRow(grid, "Username:", usernameField, 6);
        addRow(grid, "Password:", passwordField, 7);
        addRow(grid, "Confirm Password:", rePasswordField, 8);
        addRow(grid, "Phone:", phoneField, 9);
        addRow(grid, "Email:", emailField, 10);
        addRow(grid, "Account Type:", accountTypeBox, 11);
        addRow(grid, "Initial Amount:", amountField, 12);

        Button submitBtn = new Button("Register Account");
        submitBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        submitBtn.setMaxWidth(Double.MAX_VALUE);

        grid.add(submitBtn, 1, 13);

        submitBtn.setOnAction(e -> handleRegistration(stage));

        Scene scene = new Scene(grid, 450, 650);
        stage.setScene(scene);
        stage.show();
    }

    private void addRow(GridPane grid, String labelText, javafx.scene.Node field, int row) {
        grid.add(new Label(labelText), 0, row);
        grid.add(field, 1, row);
    }

    private void handleRegistration(Stage stage) {
        try {
            // Validate Basic Password Logic
            String password = passwordField.getText();
            String rePassword = rePasswordField.getText();

            if (!password.equals(rePassword)) {
                showAlert(Alert.AlertType.ERROR, "Error", "Passwords do not match!");
                return;
            }

            // Generate account number
            Random rand = new Random();
            int randomNum = 100000 + rand.nextInt(900000);
            String accountNo = firstNameField.getText().substring(0, 2) +
                    lastNameField.getText().substring(0, 2) + randomNum;

            // Prepare Date
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String regDate = df.format(new Date());

            // Set Data into Model
            AccountModel am = new AccountModel();
            am.setAccount_no(accountNo);
            am.setFirst_name(firstNameField.getText());
            am.setLast_name(lastNameField.getText());
            am.setAddress(addressField.getText());
            am.setCity(cityField.getText());
            am.setBranch(branchField.getText());
            am.setZip(zipField.getText());
            am.setUsername(usernameField.getText());
            am.setPassword(password);
            am.setPhone_number(phoneField.getText());
            am.setEmail(emailField.getText());
            am.setAccount_type(accountTypeBox.getValue());
            am.setAmount(Double.parseDouble(amountField.getText()));
            am.setReg_date(regDate);

            // Save to DB via backend logic
            DatabaseOperations db = new DatabaseOperations();
            boolean success = db.insertAccountDetails(am);

            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Account Created! No: " + accountNo);
                stage.close();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to create account in database.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "System Error", "Check input fields and database connection.");
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