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
import java.util.Currency;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.autovend.BarcodedUnit;
import com.autovend.PayController;
import com.autovend.PrintReceiptController;
import com.autovend.SelfCheckoutLogic;
import com.autovend.SellableUnit;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.products.BarcodedProduct;
import com.autovend.Bill;
 

public class PrintReceiptTest extends Tests {
	private PrintReceiptController controller;
	private PayController payController;
	private SelfCheckoutStation selfCheckoutStation;
	private SelfCheckoutLogic selfCheckoutLogic;
	private BarcodedUnit apple;
	private BarcodedUnit orange;
	private BarcodedProduct appleProduct;
	private BarcodedProduct orangeProduct;
	private Currency c;
	private Bill bill;

	@Before
	public void setup() throws OverloadException {
		super.generalSetup();
		
		selfCheckoutStation = new SelfCheckoutStation(currency, new int[] {5,10}, new BigDecimal[] {BigDecimal.valueOf(0.05), BigDecimal.valueOf(0.10)}, 100, 1);
		selfCheckoutLogic = new SelfCheckoutLogic(scs);
		controller = new PrintReceiptController(selfCheckoutStation, selfCheckoutLogic);
		payController = new PayController(selfCheckoutStation, selfCheckoutLogic);
		apple = new BarcodedUnit(barcode1, 100);
		orange =new BarcodedUnit(barcode2, 150);
		appleProduct = new BarcodedProduct(barcode1, "apple", BigDecimal.valueOf(2.00), 100);
		orangeProduct = new BarcodedProduct(barcode2, "orange", BigDecimal.valueOf(1.50), 150);
		c = Currency.getInstance(Locale.CANADA);
		bill = new Bill(5, c);
	}
	
	
	@Test
	public void printReceiptTest() {
		scl.selfCheckoutStation.mainScanner.scan(apple);
		scl.selfCheckoutStation.mainScanner.scan(orange);
		String receipt = null;
		try {
			receipt = scl.printReceiptController.printReceipt();
		}
		catch(Exception e) {
			System.out.println("Failed to print receipt!");
		}
		String expected_receipt = "ITEMS:\napple: $2.00\norange: $1.50\nTOTAL: $3.50";
		System.out.println(receipt);
		assertEquals(expected_receipt, receipt);
	}
	
	@Test
	public void printReceiptTestsclDisabled() {
		boolean b = false;
		scl.selfCheckoutStation.mainScanner.scan(apple);
		scl.disable();
		String receipt = null;
		try {
			receipt = scl.printReceiptController.printReceipt();
		}
		catch(Exception e) {
			b = true;
		}
		assertTrue(b);
	}
	
	@Test
	public void outofPaper() {
		
	}

}