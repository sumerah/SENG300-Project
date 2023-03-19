//SENG300 Project
//Group 47
//Student Names:
//Sumerah Rowshan (UCID: 30160897)
//Justin Chu (UCID: 30162809)
//Jitaksha Batish (UCID: 30116450)
//Fairooz Shafin (UCID: 30149774)
//AAL Farhan Ali (UCID: 30148704)



package com.autovend.software.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.autovend.BarcodedUnit;
import com.autovend.PrintReceiptController;
import com.autovend.SelfCheckoutLogic;
import com.autovend.SellableUnit;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.products.BarcodedProduct;
 

public class PrintReceiptTest extends Tests {
	private PrintReceiptController controller;
	private SelfCheckoutStation selfCheckoutStation;
	private SelfCheckoutLogic selfCheckoutLogic;
	private BarcodedUnit apple;
	private BarcodedUnit orange;
	

	@Before
	public void setup() throws OverloadException {
		super.generalSetup();
		
		selfCheckoutStation = scs;
		selfCheckoutLogic = scl;
		controller = new PrintReceiptController(selfCheckoutStation, selfCheckoutLogic);
		
		apple = new BarcodedUnit(barcode1, 2.0);
		orange =new BarcodedUnit(barcode2, 1.5);
	}
	
	
	@Test
	public void printReceiptTest() {
		
		scs.handheldScanner.scan((SellableUnit)apple);
		
		
		
	}

}