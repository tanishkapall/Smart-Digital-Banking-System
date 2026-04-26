package com.green.bank.database;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.green.bank.model.AccountModel;
import com.green.bank.model.DepositSchemeModel;
import com.green.bank.model.LoanModel;

public class DatabaseOperations {

    Connection conn;
    
    // ✅ INSERT ACCOUNT
    public boolean insertAccountDetails(AccountModel model) throws Exception {
        JDBC_Connect connect = new JDBC_Connect();
        conn = connect.getConnection();

        try {
            conn.setAutoCommit(false);

            PreparedStatement ps1 = conn.prepareStatement(
                    "INSERT INTO account VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );

            ps1.setString(1, model.getAccount_no());
            ps1.setString(2, model.getFirst_name());
            ps1.setString(3, model.getLast_name());
            ps1.setString(4, model.getAddress());
            ps1.setString(5, model.getCity());
            ps1.setString(6, model.getBranch());
            ps1.setString(7, model.getZip());
            ps1.setString(8, model.getUsername());
            ps1.setString(9, model.getPassword());
            ps1.setString(10, model.getPhone_number());
            ps1.setString(11, model.getEmail());
            ps1.setString(12, model.getAccount_type());
            ps1.setString(13, model.getReg_date());

            int count1 = ps1.executeUpdate();

            PreparedStatement ps2 = conn.prepareStatement(
                    "INSERT INTO amount VALUES (?, ?)"
            );
            ps2.setString(1, model.getAccount_no());
            ps2.setDouble(2, model.getAmount());

            int count2 = ps2.executeUpdate();

            conn.commit();
            conn.close();

            return (count1 > 0 && count2 > 0);

        } catch (Exception e) {
            conn.rollback();
            conn.close();
            throw e;
        }
    }

    // ✅ INSERT LOAN
    public boolean insertLoanDetails(LoanModel model) throws Exception {
        JDBC_Connect connect = new JDBC_Connect();
        conn = connect.getConnection();

        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO loan (id, amount, status, first_name, last_name, address, email) VALUES (?, ?, ?, ?, ?, ?, ?)"
        );

        ps.setString(1, model.getAccount_no());
        ps.setDouble(2, model.getLoan_amount());
        ps.setString(3, model.getStatus());
        ps.setString(4, model.getFirst_name());
        ps.setString(5, model.getLast_name());
        ps.setString(6, model.getAddress());
        ps.setString(7, model.getEmail());

        int count = ps.executeUpdate();
        conn.close();

        return count > 0;
    }

    // ✅ GET ACCOUNT DETAILS
    public AccountModel getAccountDetails(Connection conn, String account_no) throws Exception {
        AccountModel am = null;
        PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM account WHERE id=?"
        );
        ps.setString(1, account_no);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            am = new AccountModel();
            am.setAccount_no(rs.getString(1));
            am.setFirst_name(rs.getString(2));
            am.setLast_name(rs.getString(3));
            am.setAddress(rs.getString(4));
            am.setCity(rs.getString(5));
            am.setBranch(rs.getString(6));
            am.setZip(rs.getString(7));
            am.setUsername(rs.getString(8));
            am.setPassword(rs.getString(9));
            am.setPhone_number(rs.getString(10));
            am.setEmail(rs.getString(11));
            am.setAccount_type(rs.getString(12));
            am.setReg_date(rs.getString(13));

            PreparedStatement ps2 = conn.prepareStatement(
                    "SELECT amount FROM amount WHERE id=?"
            );
            ps2.setString(1, account_no);
            ResultSet rs2 = ps2.executeQuery();
            if (rs2.next()) {
                am.setAmount(rs2.getDouble(1));
            }
        }
        return am;
    }

    // ✅ INSERT DEPOSIT
    public boolean insertDepositScheme(DepositSchemeModel model) throws Exception {
        JDBC_Connect connect = new JDBC_Connect();
        conn = connect.getConnection();
        String current_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO deposit VALUES (?, ?, ?, ?, ?)"
        );

        ps.setString(1, model.getAccount_no());
        ps.setInt(2, model.getYear());
        ps.setInt(3, model.getInterest_rate());
        ps.setDouble(4, model.getAmount());
        ps.setString(5, current_time);

        int count = ps.executeUpdate();
        conn.close();
        return count > 0;
    }

    // ✅ GET LOAN LIST
    public ArrayList<LoanModel> getLoanList(Connection conn) throws Exception {
        ArrayList<LoanModel> loanList = new ArrayList<>();
        PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM loan WHERE status='pending'"
        );
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            LoanModel loan = new LoanModel();
            loan.setAccount_no(rs.getString(1));
            loan.setLoan_amount(rs.getDouble(2));
            loan.setStatus(rs.getString(3));
            loan.setFirst_name(rs.getString(4));
            loan.setLast_name(rs.getString(5));
            loan.setAddress(rs.getString(6));
            loan.setEmail(rs.getString(7));
            loanList.add(loan);
        }
        return loanList;
    }

    // ✅ GET LOAN STATUS
    public LoanModel getLoanStatus(String account_no) throws Exception {
        LoanModel lm = null;
        JDBC_Connect connect = new JDBC_Connect();
        Connection conn = connect.getConnection();

        PreparedStatement ps = conn.prepareStatement(
                "SELECT SUM(amount) FROM loan WHERE id=? AND status='success'"
        );

        ps.setString(1, account_no);
        ResultSet rs = ps.executeQuery();

        if (rs.next() && rs.getDouble(1) > 0) {
            lm = new LoanModel();
            lm.setAccount_no(account_no);
            lm.setLoan_amount(rs.getDouble(1));  // total of all active loans
            lm.setStatus("success");
        }

        conn.close();
        return lm;
    }

    // ✅ UPDATE AMOUNT (LOAN APPROVAL)
    public void updateAmount(String account_no, double loan_amount) throws Exception {
        JDBC_Connect connect = new JDBC_Connect();
        Connection conn = connect.getConnection();
        conn.setAutoCommit(false);

        try {
            PreparedStatement ps1 = conn.prepareStatement(
                    "SELECT amount FROM amount WHERE id=?"
            );
            ps1.setString(1, account_no);
            ResultSet rs = ps1.executeQuery();

            double current_amount = 0;
            if (rs.next()) {
                current_amount = rs.getDouble(1);
            }

            current_amount += loan_amount;

            PreparedStatement ps2 = conn.prepareStatement(
                    "UPDATE amount SET amount=? WHERE id=?"
            );
            ps2.setDouble(1, current_amount);
            ps2.setString(2, account_no);
            ps2.executeUpdate();

            conn.commit();
            conn.close();
        } catch (Exception e) {
            conn.rollback();
            conn.close();
            throw e;
        }
    }


    // ✅ REPAY LOAN (The missing method)
    public boolean repayLoan(String account_no, double repayAmount) throws Exception {
        JDBC_Connect connect = new JDBC_Connect();
        Connection conn = connect.getConnection();

        try {
            // 1. Get current balance
            PreparedStatement ps1 = conn.prepareStatement("SELECT amount FROM amount WHERE id=?");
            ps1.setString(1, account_no);
            ResultSet rs = ps1.executeQuery();
            double currentBalance = 0;
            if (rs.next()) {
                currentBalance = rs.getDouble(1);
            }

            // 2. Check if user has enough money
            if (currentBalance < repayAmount) {
                conn.close();
                return false;
            }

            // 3. Deduct from balance
            PreparedStatement ps2 = conn.prepareStatement("UPDATE amount SET amount=? WHERE id=?");
            ps2.setDouble(1, currentBalance - repayAmount);
            ps2.setString(2, account_no);
            ps2.executeUpdate();

            // 4. Deduct repay amount across multiple loans
            double remaining = repayAmount;

            while (remaining > 0) {
                PreparedStatement ps3 = conn.prepareStatement(
                        "SELECT loan_id, amount FROM loan WHERE id=? AND status='success' AND amount > 0 ORDER BY loan_id ASC LIMIT 1"
                );
                ps3.setString(1, account_no);
                ResultSet rs2 = ps3.executeQuery();

                if (!rs2.next()) break;

                int loanId = rs2.getInt(1);
                double loanAmt = rs2.getDouble(2);

                if (remaining >= loanAmt) {
                    PreparedStatement ps4 = conn.prepareStatement(
                            "UPDATE loan SET status='returned' WHERE loan_id=?"
                    );
                    ps4.setInt(1, loanId);
                    ps4.executeUpdate();
                    remaining -= loanAmt;

                    // Verify
                    PreparedStatement verify = conn.prepareStatement("SELECT status FROM loan WHERE loan_id=?");
                    verify.setInt(1, loanId);
                    ResultSet vrs = verify.executeQuery();
                    if (vrs.next()) {
                        System.out.println("VERIFIED status in DB right now: " + vrs.getString(1));
                    }

                } else {
                    PreparedStatement ps4 = conn.prepareStatement(
                            "UPDATE loan SET amount=? WHERE loan_id=?"
                    );
                    ps4.setDouble(1, loanAmt - remaining);
                    ps4.setInt(2, loanId);
                    ps4.executeUpdate();
                    remaining = 0;
                }
            } // ← while loop ends here

            conn.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) conn.close();
            throw e;
        }
    }
    }
