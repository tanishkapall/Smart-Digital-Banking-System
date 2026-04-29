package com.green.bank.model;
public class LoanModel {

    // 🔒 Clean internal naming
    private String accountNo;
    private String status;
    private String firstName;
    private String lastName;
    private String email;
    private String address;

    private double loanAmount;   // ✅ better than int

    // =========================
    // ✅ DEFAULT CONSTRUCTOR
    // =========================
    public LoanModel() {}

    // =========================
    // ✅ OPTIONAL CONSTRUCTOR
    // =========================
    public LoanModel(String accountNo, double loanAmount, String status) {
        this.accountNo = accountNo;
        this.loanAmount = loanAmount;
        this.status = status;
    }

    // =========================
    // 🔁 BACKWARD COMPATIBILITY
    // (OLD METHOD NAMES)
    // =========================

    public String getAccount_no() {
        return accountNo;
    }

    public void setAccount_no(String account_no) {
        this.accountNo = account_no;
    }

    public String getFirst_name() {
        return firstName;
    }

    public void setFirst_name(String first_name) {
        this.firstName = first_name;
    }

    public String getLast_name() {
        return lastName;
    }

    public void setLast_name(String last_name) {
        this.lastName = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getLoan_amount() {
        return loanAmount;
    }

    public void setLoan_amount(double loan_amount) {
        this.loanAmount = loan_amount;
    }

    // =========================
    // 🆕 CLEAN MODERN METHODS
    // =========================

    public String getAccountNo() {
        return accountNo;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}