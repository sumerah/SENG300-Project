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

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.PayController;
import com.autovend.PrintReceiptController;
import com.autovend.SelfCheckoutLogic;
import com.autovend.SellableUnit;
import com.autovend.devices.OverloadException;
import com.autovend.devices.ReceiptPrinter;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.Bill;
import com.autovend.Numeral;
 

public class PrintReceiptTest {
	private PrintReceiptController controller;
	private PayController payController;
	private SelfCheckoutStation selfCheckoutStation;
	private SelfCheckoutLogic selfCheckoutLogic;
	private BarcodedUnit apple;
	private BarcodedUnit orange;
	private BarcodedProduct appleProduct;
	private BarcodedProduct orangeProduct;
	private Currency c;

	@Before
	public void setup() throws OverloadException {
		c = Currency.getInstance(Locale.CANADA);
		selfCheckoutStation = new SelfCheckoutStation(c, new int[] {5,10}, new BigDecimal[] {BigDecimal.valueOf(0.05), BigDecimal.valueOf(0.10)}, 100, 1);
		selfCheckoutLogic = new SelfCheckoutLogic(selfCheckoutStation);
		Barcode barcode1 = new Barcode(Numeral.valueOf((byte) 1));
		Barcode barcode2 = new Barcode(Numeral.valueOf((byte) 2));
		apple = new BarcodedUnit(barcode1, 100);
		orange =new BarcodedUnit(barcode2, 150);
		appleProduct = new BarcodedProduct(barcode1, "apple", BigDecimal.valueOf(2.00), 100);
		orangeProduct = new BarcodedProduct(barcode2, "orange", BigDecimal.valueOf(1.50), 150);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode1, appleProduct);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode2, orangeProduct);
		selfCheckoutLogic.selfCheckoutStation.printer.addInk(100);
		selfCheckoutLogic.selfCheckoutStation.printer.addPaper(20);
	
	}
	
	
	@Test
	public void printReceiptTest() {
		selfCheckoutLogic.selfCheckoutStation.mainScanner.scan(apple);
		selfCheckoutLogic.selfCheckoutStation.mainScanner.scan(orange);
		String receipt = null;
		try {
			receipt = selfCheckoutLogic.printReceiptController.printReceipt();
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
		selfCheckoutLogic.selfCheckoutStation.mainScanner.scan(apple);
		selfCheckoutLogic.disable();
		String receipt = null;
		try {
			receipt = selfCheckoutLogic.printReceiptController.printReceipt();
		}
		catch(Exception e) {
			b = true;
		}
		assertTrue(b);
	}
	
	@Test
	public void outofPaper() throws OverloadException {
		SelfCheckoutStation scs = new SelfCheckoutStation(c, new int[] {5,10}, new BigDecimal[] {BigDecimal.valueOf(0.05), BigDecimal.valueOf(0.10)}, 100, 1);
		scs.printer.addInk(100);
		SelfCheckoutLogic scl = new SelfCheckoutLogic(scs);
		scl.selfCheckoutStation.mainScanner.scan(apple);
		scl.selfCheckoutStation.mainScanner.scan(orange);
		String receipt = null;
		boolean b = false;
		try {
			receipt = scl.printReceiptController.printReceipt();
		}
		catch(Exception e) {
			b=true;
		}
		assertTrue(b);
		assertEquals(1, scl.attendant.stationNeedsMaintenance);
		assertTrue(scl.isDisabled());
	}

}