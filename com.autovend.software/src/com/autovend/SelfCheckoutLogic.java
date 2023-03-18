package com.autovend;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.lang.Math;

import com.autovend.devices.ElectronicScale;
import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.products.*;

public class SelfCheckoutLogic {
	public CustomerStub customer;
	public AttendantStub attendant;
	public SelfCheckoutStation selfCheckoutStation;
	public AddItemController addItemController;
	public PrintReceiptController printReceiptController;
	public PayController payController;
	public ArrayList<BarcodedProduct> scannedItems = new ArrayList<>();
	public BigDecimal totalCost = BigDecimal.valueOf(0.00);
	public BigDecimal funds = BigDecimal.valueOf(0.00);
	public BigDecimal amountDue = BigDecimal.valueOf(0.00);
	public double baggingAreaExpectedWeight = 0;
	public double baggingAreaWeight = 0;
	public boolean systemDisabled = false;
	
	public SelfCheckoutLogic(SelfCheckoutStation scs){
		selfCheckoutStation = scs;
		customer = new CustomerStub();
		attendant = new AttendantStub();
		addItemController = new AddItemController(selfCheckoutStation, this);
		printReceiptController = new PrintReceiptController(selfCheckoutStation, this);
		payController = new PayController(selfCheckoutStation, this);
	}	
	
	public void enable() {systemDisabled = false;}
	
	public void disable() {systemDisabled = true;}
	
}
