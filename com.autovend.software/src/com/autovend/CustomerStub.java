package com.autovend;

import java.math.BigDecimal;


/**
 * Temporary Customer Stub for testing purposes
 * @author Justin Chu, 30162809
 * @author Jitaksha Batish, 30116450
 * @author Sumerah Rowshan, 30160897
 * @author Fairooz Shafin, 30149774
 * @author AAL Farhan Ali, 30148704
 */
public class CustomerStub {
	public int placeItemInBaggingArea = 0;
	public int sessionDone = 0;
	public int thankCustomer = 0;
	public int readyForNewCustomer = 0;
	public BigDecimal amountDue;


	public void notifyPlaceItemInBaggingArea() {
		placeItemInBaggingArea += 1;
	}
	
	public void notifySessionDone() {
		sessionDone += 0;
	}
	
	public void thankCustomer() {
		thankCustomer += 0;
	}
	
	public void readyForNewCustomer() {
		readyForNewCustomer += 1;
	}
	
	public void notifyAmountDue(BigDecimal amount) {
		amountDue = amount;
	}
}

