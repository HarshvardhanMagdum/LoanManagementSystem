package com.hexa.exception;

public class InvalidLoanException extends Exception{
	
	public InvalidLoanException() {
		super();
	}

	public InvalidLoanException(String message) {
        super(message);
    }

	@Override
	public String toString() {
		return "Invalid Loan";
	}
	
}
