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

public class AddItemTest {
	SelfCheckoutStation scs;
	private Currency currency;
	SelfCheckoutLogic scl;
	private Barcode barcode1;
	private Barcode barcode2;
	
	
	@Before
	public void generalSetup() throws OverloadException {
		currency = Currency.getInstance(Locale.CANADA);
		scs = new SelfCheckoutStation(currency, new int[] {5,10}, new BigDecimal[] {BigDecimal.valueOf(0.05), BigDecimal.valueOf(0.10)}, 100, 1);
		scl = new SelfCheckoutLogic(scs);
		Numeral number = Numeral.valueOf((byte) 1);
		Numeral number2 = Numeral.valueOf((byte) 2);
		barcode1 = new Barcode(number);
		barcode2 = new Barcode(number2);
		BarcodedProduct bp1 = new BarcodedProduct(barcode1, "apple", BigDecimal.valueOf(2.00), 100);
		BarcodedProduct bp2 = new BarcodedProduct(barcode2, "orange", BigDecimal.valueOf(1.50), 150);
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
public void testAddMultipleItems() {
    // scan two items
    BarcodedUnit item1 = new BarcodedUnit(barcode1, 100);
    BarcodedUnit item2 = new BarcodedUnit(barcode2, 150);
    scs.mainScanner.scan(item1);
    scs.mainScanner.scan(item2);
    
    // assert the total cost is correct
    assertEquals(BigDecimal.valueOf(3.50).setScale(2, RoundingMode.HALF_UP), scl.totalCost);
}

@Test(expected = DisabledException.class)
public void testStationDisabled() throws DisabledException {
    // disable the station
    selfCheckoutLogic.disable();
    
    // scan an item (should throw DisabledException)
    BarcodedUnit item = new BarcodedUnit(barcode1, 100);
    scs.mainScanner.scan(item);
}
	
@Test(expected = EmptyException.class)
public void testBaggingAreaEmpty() throws EmptyException {
    // scan an item without placing it in the bagging area (should throw EmptyException)
    BarcodedUnit item = new BarcodedUnit(barcode1, 100);
    scs.mainScanner.scan(item);
    selfCheckoutLogic.enable();
}
	
@Test(expected = OverloadException.class)
public void testStationOverloaded() throws OverloadException {
    // scan an item that exceeds the maximum weight capacity of the bagging area (should throw OverloadException)
    BarcodedUnit item = new BarcodedUnit(barcode1, 101);
    scs.mainScanner.scan(item);
}

@Test
public void testAddingItemUpdatesExpectedWeight() throws EmptyException, OverloadException {
    BarcodedProduct product = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode1);
    double expectedWeight = product.getExpectedWeight();
    scs.handheldScanner.scan(barcode1);
    assertEquals(expectedWeight, scl.baggingAreaExpectedWeight, 0.01);
}

@Test
public void testAddingItemUpdatesTotalCost() throws EmptyException, OverloadException {
    BarcodedProduct product = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode1);
    BigDecimal price = product.getPrice().setScale(2, RoundingMode.HALF_UP);
    scs.handheldScanner.scan(barcode1);
    assertEquals(price, scl.totalCost);
}

@Test
public void testAddingMultipleItemsUpdatesExpectedWeightAndTotalCost() throws EmptyException, OverloadException {
    BarcodedProduct product1 = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode1);
    BarcodedProduct product2 = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode2);
    double expectedWeight = product1.getExpectedWeight() + product2.getExpectedWeight();
    BigDecimal price1 = product1.getPrice().setScale(2, RoundingMode.HALF_UP);
    BigDecimal price2 = product2.getPrice().setScale(2, RoundingMode.HALF_UP);
    BigDecimal expectedTotalCost = price1.add(price2);
    scs.handheldScanner.scan(barcode1);
    scs.handheldScanner.scan(barcode2);
    assertEquals(expectedWeight, scl.baggingAreaExpectedWeight, 0.01);
    assertEquals(expectedTotalCost, scl.totalCost);
}

	
}
	
