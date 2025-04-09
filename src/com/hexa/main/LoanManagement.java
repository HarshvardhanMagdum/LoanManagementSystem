package com.hexa.main;

import com.hexa.dao.*;
import com.hexa.entity.*;
import com.hexa.exception.*;
import java.util.List;
import java.util.Scanner;

public class LoanManagement {

    public static void main(String[] args) throws DbConnectionException {
        Scanner sc = new Scanner(System.in);
        ILoanRepository repo = new LoanRepositoryImpl();

        while (true) {
            System.out.println("\n========== Loan Management System ==========");
            System.out.println("1. Apply Loan");
            System.out.println("2. Get All Loans");
            System.out.println("3. Get Loan By ID");
            System.out.println("4. Calculate Interest");
            System.out.println("5. Calculate EMI");
            System.out.println("6. Update Loan Status");
            System.out.println("7. Loan Repayment");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");
            int ch = sc.nextInt();

            switch (ch) {
                case 1:
                    System.out.println("Choose Loan Type: 1. HomeLoan  2. CarLoan");
                    int type = sc.nextInt();
                    System.out.println("Enter customer ID, name, email, phone, address, credit score:");
                    int cid = sc.nextInt();
                    sc.nextLine();
                    String name = sc.nextLine();
                    String email = sc.nextLine();
                    String phone = sc.nextLine();
                    String addr = sc.nextLine();
                    int credit = sc.nextInt();
                    Customer cust = new Customer(cid, name, email, phone, addr, credit);

                    System.out.println("Enter loan ID, principal, interest rate, tenure (months):");
                    int lid = sc.nextInt();
                    double principal = sc.nextDouble();
                    double rate = sc.nextDouble();
                    int term = sc.nextInt();

                    if (type == 1) {
                        sc.nextLine();
                        System.out.println("Enter property address and value:");
                        String propAddr = sc.nextLine();
                        int propVal = sc.nextInt();
                        HomeLoan hl = new HomeLoan(lid, cust, principal, rate, term, "HomeLoan", "Pending", propAddr, propVal);
                        repo.applyLoan(hl);
                    } else {
                        sc.nextLine();
                        System.out.println("Enter car model and value:");
                        String model = sc.nextLine();
                        int carVal = sc.nextInt();
                        CarLoan cl = new CarLoan(lid, cust, principal, rate, term, "CarLoan", "Pending", model, carVal);
                        repo.applyLoan(cl);
                    }
                    break;

                case 2:
                    List<Loan> allLoans = repo.getAllLoan();
                    for (Loan l : allLoans) {
                        System.out.println(l);
                    }
                    break;

                case 3:
                    System.out.print("Enter Loan ID: ");
                    int loanId = sc.nextInt();
                    try {
                        Loan l = repo.getLoanById(loanId);
                        System.out.println(l);
                    } catch (InvalidLoanException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 4:
                    System.out.print("Enter Loan ID: ");
                    int id = sc.nextInt();
                    try {
                        double interest = repo.calculateInterest(id);
                        System.out.println("Total Interest: ₹" + interest);
                    } catch (InvalidLoanException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 5:
                    System.out.print("Enter Loan ID: ");
                    int emiId = sc.nextInt();
                    try {
                        double emi = repo.calculateEMI(emiId);
                        System.out.println("Monthly EMI: ₹" + emi);
                    } catch (InvalidLoanException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 6:
                    System.out.print("Enter Loan ID: ");
                    int statusId = sc.nextInt();
                    try {
                        repo.loanStatus(statusId);
                    } catch (InvalidLoanException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 7:
                    System.out.print("Enter Loan ID and repayment amount: ");
                    int repayId = sc.nextInt();
                    double amount = sc.nextDouble();
                    try {
                        repo.loanRepayment(repayId, amount);
                    } catch (InvalidLoanException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 8:
                    System.out.println("Exiting...");
                    sc.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

}
