package com.autovend;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.lang.Math;

import com.autovend.devices.ElectronicScale;
import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.products.*;

/**
 * Represents the logic of the self-checkout station (software)
 * @author Justin Chu, 30162809
 * @author Jitaksha Batish, 30116450
 * @author Sumerah Rowshan, 30160897
 * @author Fairooz Shafin, 30149774
 * @author AAL Farhan Ali, 30148704
 */
public class SelfCheckoutLogic {
	//Customer using the station
	public CustomerStub customer;
	//Attendant related to the station
	public AttendantStub attendant;
	//The station itself
	public SelfCheckoutStation selfCheckoutStation;
	//The controller that handles the added items 
	public AddItemController addItemController;
	//The controller that handles the receipt
	public PrintReceiptController printReceiptController;
	//The controller that handles the payment
	public PayController payController;
	//ArrayList that stores all added items
	public ArrayList<BarcodedProduct> scannedItems = new ArrayList<>();
	//BigDecimal that stores the total cost of added items
	public BigDecimal totalCost = BigDecimal.valueOf(0.00);
	//BigDecimal that stores the value of the cash inputed by customer
	public BigDecimal funds = BigDecimal.valueOf(0.00);
	//BigDecimal that stores how much the customer still needs to pay (can be negative)
	public BigDecimal amountDue = BigDecimal.valueOf(0.00);
	//The expected weight of the baggingArea
	public double baggingAreaExpectedWeight = 0;
	//The actual weight of the bagging area
	public double baggingAreaWeight = 0;
	//Boolean for checking whether the station has been disabled
	public boolean systemDisabled = false;
	
	/**
	 * Basic constructor.
	 * 
	 * @param scs
	 * 				The self-checkout station that the logic is installed on
	 */
	public SelfCheckoutLogic(SelfCheckoutStation scs){
		selfCheckoutStation = scs;
		customer = new CustomerStub();
		attendant = new AttendantStub();
		addItemController = new AddItemController(selfCheckoutStation, this);
		printReceiptController = new PrintReceiptController(selfCheckoutStation, this);
		payController = new PayController(selfCheckoutStation, this);
	}	
	
	/**
	 * Method to enable the station
	 */
	public void enable() {systemDisabled = false;}
	
	/**
	 * Method to disable the station
	 */
	public void disable() {systemDisabled = true;}
	
	/**
	 * returns true if disabled 
	 */
	public boolean isDisabled() {
		return systemDisabled;
	}
}
