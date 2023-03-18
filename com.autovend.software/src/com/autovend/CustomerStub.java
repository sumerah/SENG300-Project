//SENG300 Project
//Group 47
//Student Names:
//Sumerah Rowshan (UCID: 30160897)
//Justin Chu (UCID: 30162809)
//Jitaksha Batish (UCID: 30116450)
//Fairooz Shafin (UCID: 30149774)
//AAL Farhan Ali (UCID: 30148704)


package com.autovend;

import java.math.BigDecimal;

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

