package com.autovend;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.autovend.devices.SelfCheckoutStation;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;

public class AddItemTest {
	private SelfCheckoutStation scs;
	private Currency currency;
	private SelfCheckoutLogic scl;
	private Barcode barcode1;
	private Barcode barcode2;
	
	
	@Before
	public void gneralSetup() {
		currency = Currency.getInstance(Locale.CANADA);
		scs = new SelfCheckoutStation(currency, new int[] {5,10}, new BigDecimal[] {BigDecimal.valueOf(0.05), BigDecimal.valueOf(0.10)}, 100, 1);
		scl = new SelfCheckoutLogic(scs);
		Numeral number = Numeral.valueOf((byte) 1);
		Numeral number2 = Numeral.valueOf((byte) 2);
		barcode1 = new Barcode(number);
		barcode2 = new Barcode(number2);
		BarcodedProduct bp1 = new BarcodedProduct(barcode1, "apple", BigDecimal.valueOf(2.01), 100);
		BarcodedProduct bp2 = new BarcodedProduct(barcode2, "orange", BigDecimal.valueOf(1.50), 150);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode1, bp1);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode2, bp2);
	}

	@Test
	public void test() {
		BarcodedUnit item = new BarcodedUnit(barcode1, 100);
		scs.mainScanner.scan(item);
		System.out.println(scl.totalCost);
		assertEquals(BigDecimal.valueOf(2.00), scl.totalCost);
	}
}
