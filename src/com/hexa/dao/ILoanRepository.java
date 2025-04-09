package com.hexa.dao;

import com.hexa.entity.Loan;
import com.hexa.exception.InvalidLoanException;

import java.util.List;

public interface ILoanRepository {

    boolean applyLoan(Loan loan);

    double calculateInterest(int loanId) throws InvalidLoanException;
    double calculateInterest(double principal, double interestRate, int term);

    String loanStatus(int loanId) throws InvalidLoanException;

    double calculateEMI(int loanId) throws InvalidLoanException;
    double calculateEMI(double principal, double rate, int term);

    String loanRepayment(int loanId, double amount) throws InvalidLoanException;

    List<Loan> getAllLoan();

    Loan getLoanById(int loanId) throws InvalidLoanException;
}

