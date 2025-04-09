package com.hexa.test;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import com.hexa.dao.ILoanRepository;
import com.hexa.dao.LoanRepositoryImpl;
import com.hexa.entity.Customer;
import com.hexa.entity.HomeLoan;
import com.hexa.entity.Loan;
import com.hexa.exception.DbConnectionException;
import com.hexa.exception.InvalidLoanException;

public class LoanRepositoryImplTest {
	
	private static ILoanRepository repo;

	@BeforeClass
	public static void setUp() {
	    try {
			repo = new LoanRepositoryImpl();
		} catch (DbConnectionException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetAllLoans() {
	    List<Loan> loans = repo.getAllLoan();
	    assertNotNull(loans);
	    assertTrue(loans.size() > 0);
	}

	@Test
	public void testGetLoanByIdValid() {
	    try {
	        Loan loan = repo.getLoanById(1);
	        assertNotNull(loan);
	        assertEquals(1, loan.getLoanId());
	    } catch (InvalidLoanException e) {
	        fail("Loan should exist: " + e.getMessage());
	    }
	}

	

	@Test
	public void testCalculateInterestValid() {
	    try {
	        double interest = repo.calculateInterest(1);
	        assertTrue(interest > 0);
	    } catch (InvalidLoanException e) {
	        fail("Exception: " + e.getMessage());
	    }
	}

	@Test
	public void testCalculateInterestInvalid() {
	    try {
	        repo.calculateInterest(9999);
	        fail("Expected InvalidLoanException");
	    } catch (InvalidLoanException e) {
	        // expected
	    }
	}

	@Test
	public void testCalculateEMIValid() {
	    try {
	        double emi = repo.calculateEMI(1);
	        assertTrue(emi > 0);
	    } catch (InvalidLoanException e) {
	        fail("Exception: " + e.getMessage());
	    }
	}

	@Test
	public void testCalculateEMIInvalid() {
	    try {
	        repo.calculateEMI(9999);
	        fail("Expected InvalidLoanException");
	    } catch (InvalidLoanException e) {
	        // expected
	    }
	}

	@Test
	public void testLoanStatusUpdate() {
	    try {
	        repo.loanStatus(1);
	        Loan loan = repo.getLoanById(1);
	        assertTrue(loan.getLoanStatus().equals("Approved") || loan.getLoanStatus().equals("Rejected"));
	    } catch (InvalidLoanException e) {
	        fail("Exception: " + e.getMessage());
	    }
	}

	@Test
	public void testLoanRepaymentValid() {
	    try {
	        repo.loanRepayment(1, 20000);
	    } catch (InvalidLoanException e) {
	        fail("Exception: " + e.getMessage());
	    }
	}

	@Test
	public void testLoanRepaymentInvalid() {
	    try {
	        repo.loanRepayment(9999, 10000);
	        fail("Expected InvalidLoanException");
	    } catch (InvalidLoanException e) {
	        // expected
	    }
	}

}
