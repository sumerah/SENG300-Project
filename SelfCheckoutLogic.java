package com.autovend;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.lang.Math;

import com.autovend.devices.ElectronicScale;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.products.*;

public class SelfCheckoutLogic {
	public SelfCheckoutStation selfCheckoutStation;
	public ElectronicScale baggingArea;
	public AddItemController addItemController;
	public PrintReceipt printReceipt;
	public ArrayList<BarcodedProduct> scannedItems = new ArrayList<>();
	public BigDecimal totalCost = BigDecimal.valueOf(0.00);
	public double baggingAreaExpectedWeight = 0;
	public double baggingAreaWeight;
	
	public SelfCheckoutLogic(SelfCheckoutStation station){
		selfCheckoutStation = station;
		baggingArea = station.baggingArea;
		addItemController = new AddItemController(selfCheckoutStation, this);
		printReceipt = new PrintReceipt(this);
		
		
	}
}
