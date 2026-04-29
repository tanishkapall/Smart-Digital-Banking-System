package com.green.bank.model;

public class AccountModel {

    // Data Members
    private String accountNo;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String branch;
    private String zip;
    private String username;
    private String password;
    private String phoneNumber;
    private String email;
    private String accountType;

    private double amount;
    private String regDate;


    //  DEFAULT CONSTRUCTOR

    public AccountModel() {}


    //  OPTIONAL CONSTRUCTOR

    public AccountModel(String accountNo, String firstName, String lastName,
                        String username, String password, double amount) {
        this.accountNo = accountNo;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.amount = amount;
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone_number() {
        return phoneNumber;
    }

    public void setPhone_number(String phone_number) {
        this.phoneNumber = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccount_type() {
        return accountType;
    }

    public void setAccount_type(String account_type) {
        this.accountType = account_type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getReg_date() {
        return regDate;
    }

    public void setReg_date(String reg_date) {
        this.regDate = reg_date;
    }


    public String getAccountNo() {
        return accountNo;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getRegDate() {
        return regDate;
    }

}