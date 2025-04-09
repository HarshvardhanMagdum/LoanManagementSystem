package com.hexa.exception;

public class LoanNotFoundException extends Exception{

	public LoanNotFoundException() {
		super();
	}

	public LoanNotFoundException(String message) {
        super(message);
    }

	@Override
	public String toString() {
		return "Loan Not Found";
	}
	
	

}
