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
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.Numeral;
import com.autovend.SelfCheckoutLogic;
import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;

public class Tests {
	SelfCheckoutStation scs;
	private Currency currency;
	SelfCheckoutLogic scl;
	Barcode barcode1;
	Barcode barcode2;
	BarcodedProduct bp1;
	BarcodedProduct bp2;
	
	
	@Before
	public void generalSetup() throws OverloadException {
		currency = Currency.getInstance(Locale.CANADA);
		scs = new SelfCheckoutStation(currency, new int[] {5,10}, new BigDecimal[] {BigDecimal.valueOf(0.05), BigDecimal.valueOf(0.10)}, 100, 1);
		scl = new SelfCheckoutLogic(scs);
		Numeral number = Numeral.valueOf((byte) 1);
		Numeral number2 = Numeral.valueOf((byte) 2);
		barcode1 = new Barcode(number);
		barcode2 = new Barcode(number2);
		bp1 = new BarcodedProduct(barcode1, "apple", BigDecimal.valueOf(2.00), 100);
		bp2 = new BarcodedProduct(barcode2, "orange", BigDecimal.valueOf(1.50), 150);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode1, bp1);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode2, bp2);
		scs.printer.addInk(100);
		scs.printer.addPaper(20);
	}

	@Test
	public void test() {
		BarcodedUnit item = new BarcodedUnit(barcode1, 100);
		scs.mainScanner.scan(item);
		System.out.println(scl.totalCost);
		assertEquals(BigDecimal.valueOf(2.00).setScale(2, RoundingMode.HALF_UP), scl.totalCost);
	}
	
	@Test
	public void test2() throws OverloadException, EmptyException {
		BarcodedUnit item = new BarcodedUnit(barcode1, 100);
		scs.mainScanner.scan(item);
		String receipt = scl.printReceiptController.printReceipt();
		System.out.println(receipt);
		String expected = "ITEMS:\napple: $2.00\nTOTAL: $2.00";
		assertEquals(expected, receipt);
	}
}