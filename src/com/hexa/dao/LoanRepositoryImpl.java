package com.hexa.dao;

import com.hexa.entity.Customer;
import com.hexa.entity.Loan;
import com.hexa.exception.DbConnectionException;
import com.hexa.exception.InvalidLoanException;
import com.hexa.util.DBConnUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LoanRepositoryImpl implements ILoanRepository {
    private final Connection connection;

    public LoanRepositoryImpl() throws DbConnectionException {
        this.connection = DBConnUtil.getDbConnection();
    }

    @Override
    public boolean applyLoan(Loan loan) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Confirm to apply for loan? (Yes/No): ");
        String confirm = sc.nextLine();

        if (!confirm.equalsIgnoreCase("Yes")) {
            System.out.println("Loan application cancelled.");
            return false;
        }

        String insertSQL = "INSERT INTO loan (loan_id, customer_id, principal_amount, interest_rate, loan_term, loan_type, loan_status) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setInt(1, loan.getLoanId());
            pstmt.setInt(2, loan.getCustomer().getCustomerId());
            pstmt.setDouble(3, loan.getPrincipalAmount());
            pstmt.setDouble(4, loan.getInterestRate());
            pstmt.setInt(5, loan.getLoanTerm());
            pstmt.setString(6, loan.getLoanType());
            pstmt.setString(7, "Pending");

            pstmt.executeUpdate();
            System.out.println("Loan application submitted successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
		return false;
    }

    @Override
    public double calculateInterest(int loanId) throws InvalidLoanException {
        Loan loan = getLoanById(loanId);
        return calculateInterest(loan.getPrincipalAmount(), loan.getInterestRate(), loan.getLoanTerm());
    }

    @Override
    public double calculateInterest(double principal, double interestRate, int term) {
        return (principal * interestRate * term) / 1200;
    }

    @Override
    public String loanStatus(int loanId) throws InvalidLoanException {
        Loan loan = getLoanById(loanId);
        int creditScore = loan.getCustomer().getCreditScore();
        String status = (creditScore > 650) ? "Approved" : "Rejected";

        String updateSQL = "UPDATE loan SET loan_status = ? WHERE loan_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, loanId);
            pstmt.executeUpdate();
            System.out.println("Loan status updated to: " + status);
        } catch (SQLException e) {
            e.printStackTrace();
        }
		return updateSQL;
    }

    @Override
    public double calculateEMI(int loanId) throws InvalidLoanException {
        Loan loan = getLoanById(loanId);
        return calculateEMI(loan.getPrincipalAmount(), loan.getInterestRate(), loan.getLoanTerm());
    }

    @Override
    public double calculateEMI(double principal, double rate, int term) {
        double monthlyRate = rate / 12 / 100;
        return (principal * monthlyRate * Math.pow(1 + monthlyRate, term)) /
                (Math.pow(1 + monthlyRate, term) - 1);
    }

    @Override
    public String loanRepayment(int loanId, double amount) throws InvalidLoanException {
        double emi = calculateEMI(loanId);
        if (amount < emi) {
            System.out.println("Insufficient amount to pay even one EMI.");
            return null;
        }
        int paidEmiCount = (int)(amount / emi);
        System.out.println("Number of EMIs paid: " + paidEmiCount);
        // You can also update remaining term or paid amount in DB if required
		return null;
    }

    @Override
    public List<Loan> getAllLoan() {
        List<Loan> loanList = new ArrayList<>();
        String selectSQL = "SELECT * FROM loan l JOIN customer c ON l.customer_id = c.customer_id";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {

            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        rs.getString("address"),
                        rs.getInt("credit_score")
                );

                Loan loan = new Loan(
                        rs.getInt("loan_id"),
                        customer,
                        rs.getDouble("principal_amount"),
                        rs.getDouble("interest_rate"),
                        rs.getInt("loan_term"),
                        rs.getString("loan_type"),
                        rs.getString("loan_status")
                );

                loanList.add(loan);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return loanList;
    }

    @Override
    public Loan getLoanById(int loanId) throws InvalidLoanException {
        String selectSQL = "SELECT * FROM loan l JOIN customer c ON l.customer_id = c.customer_id WHERE loan_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {
            pstmt.setInt(1, loanId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        rs.getString("address"),
                        rs.getInt("credit_score")
                );

                return new Loan(
                        rs.getInt("loan_id"),
                        customer,
                        rs.getDouble("principal_amount"),
                        rs.getDouble("interest_rate"),
                        rs.getInt("loan_term"),
                        rs.getString("loan_type"),
                        rs.getString("loan_status")
                );
            } else {
                throw new InvalidLoanException("Loan with ID " + loanId + " not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new InvalidLoanException("Database error occurred.");
        }
    }
}

