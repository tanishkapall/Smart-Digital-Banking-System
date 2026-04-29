package com.green.bank.model;

public class DepositSchemeModel {

    // 🔒 Clean internal naming
    private String accountNo;
    private String depositDate;
    private String value;

    private int year;
    private int interestRate;
    private double amount;   // ✅ better than int

    // =========================
    // ✅ DEFAULT CONSTRUCTOR
    // =========================
    public DepositSchemeModel() {}

    // =========================
    // ✅ OPTIONAL CONSTRUCTOR
    // =========================
    public DepositSchemeModel(String accountNo, int year, int interestRate, double amount) {
        this.accountNo = accountNo;
        this.year = year;
        this.interestRate = interestRate;
        this.amount = amount;
    }

    // =========================
    // 🔁 BACKWARD COMPATIBILITY
    // =========================

    public String getAccount_no() {
        return accountNo;
    }

    public void setAccount_no(String account_no) {
        this.accountNo = account_no;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getInterest_rate() {
        return interestRate;
    }

    public void setInterest_rate(int interest_rate) {
        this.interestRate = interest_rate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDeposit_date() {
        return depositDate;
    }

    public void setDeposit_date(String deposit_date) {
        this.depositDate = deposit_date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    // =========================
    // 🆕 CLEAN MODERN METHODS (optional)
    // =========================

    public String getAccountNo() {
        return accountNo;
    }

    public int getInterestRate() {
        return interestRate;
    }

    public String getDepositDate() {
        return depositDate;
    }

}