//SENG300 Project
//Group 47
//Student Names:
//Sumerah Rowshan (UCID: 30160897)
//Justin Chu (UCID: 30162809)
//Jitaksha Batish (UCID: 30116450)
//Fairooz Shafin (UCID: 30149774)
//AAL Farhan Ali (UCID: 30148704)

package com.autovend.software.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.Bill;
import com.autovend.Numeral;
import com.autovend.devices.BillDispenser;
import com.autovend.devices.BillSlot;
import com.autovend.devices.BillStorage;
import com.autovend.devices.BillValidator;
import com.autovend.devices.DisabledException;
import com.autovend.devices.OverloadException;
import com.autovend.PayController;
import com.autovend.SelfCheckoutLogic;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.BillDispenserObserver;
import com.autovend.devices.observers.BillSlotObserver;
import com.autovend.devices.observers.BillStorageObserver;
import com.autovend.devices.observers.BillValidatorObserver;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;

/**
 * This class contains JUnit tests for the PayController class, which is responsible for handling
 * the payment process in a self-checkout system.
 */
public class PayWithCashTest {
    
    private SelfCheckoutStation scs;
    private SelfCheckoutLogic scl;
    private PayController payController;
    private Currency c;
    private BarcodedUnit apple;
	private BarcodedUnit orange;
	private BarcodedProduct appleProduct;
	private BarcodedProduct orangeProduct;
    

    /**
     * Sets up the test by creating a mock self-checkout station and self-checkout logic object, 
     * and instantiating a PayController object with those objects.
     */
    @Before
    public void setUp() {
    	c = Currency.getInstance(Locale.CANADA);
        scs = new SelfCheckoutStation(c, new int[] {5,10}, new BigDecimal[] {BigDecimal.valueOf(0.05), BigDecimal.valueOf(0.10)}, 100, 1);
        scl = new SelfCheckoutLogic(scs);
        Barcode barcode1 = new Barcode(Numeral.valueOf((byte) 1));
		Barcode barcode2 = new Barcode(Numeral.valueOf((byte) 2));
		apple = new BarcodedUnit(barcode1, 100);
		orange =new BarcodedUnit(barcode2, 150);
		appleProduct = new BarcodedProduct(barcode1, "apple", BigDecimal.valueOf(10.00), 100);
		orangeProduct = new BarcodedProduct(barcode2, "orange", BigDecimal.valueOf(1.50), 150);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode1, appleProduct);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode2, orangeProduct);
		payController = new PayController(scs, scl);
    }

    /**
     * Tests the constructor of the PayController class.
     */
    @Test
    public void testConstructor() {
        assertNotNull(payController);
    }

    /**
     * Tests the reactToValidBillDetectedEvent method of the PayController class when a valid bill
     * is detected.
     * @throws OverloadException 
     * @throws DisabledException 
     */
    @Test
    public void testReactToValidBillDetectedEvent() throws DisabledException, OverloadException {
        scl.selfCheckoutStation.mainScanner.scan(apple);
        Bill bill = new Bill(5,c);
        scl.selfCheckoutStation.billValidator.accept(bill);
        

        assertEquals(5, scl.customer.amountDue);
    }
    
    /**
     * Tests the reactToValidBillDetectedEvent method of the PayController class when the system is 
     * disabled.
     */
    @Test(expected = DisabledException.class)
    public void testReactToValidBillDetectedEvent_whenSystemDisabled() {
        scl.systemDisabled = true;
        scl.selfCheckoutStation.billValidator.accept(new Bill(5, c));
    }
    
    @Test(expected = DisabledException.class)
    public void testwhenValidatordisabled() {
    	scl.selfCheckoutStation.billValidator.disable();
    	scl.selfCheckoutStation.billValidator.accept(new Bill(5, c));
    }

    @Test(expected = NullPointerException.class)
    public void testwhenNullBill() {
    	scl.selfCheckoutStation.billValidator.accept(null);
    }
    
    
}
