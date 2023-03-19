//SENG300 Project
//Group 47
//Student Names:
//Sumerah Rowshan (UCID: 30160897)
//Justin Chu (UCID: 30162809)
//Jitaksha Batish (UCID: 30116450)
//Fairooz Shafin (UCID: 30149774)
//AAL Farhan Ali (UCID: 30148704)package com.autovend.software.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.Before;
import org.junit.Test;

import com.autovend.Bill;
import com.autovend.BillDenomination;
import com.autovend.BillDispenser;
import com.autovend.BillInput;
import com.autovend.BillOutput;
import com.autovend.BillSlot;
import com.autovend.BillStorage;
import com.autovend.BillValidator;
import com.autovend.DisabledException;
import com.autovend.SelfCheckoutLogic;
import com.autovend.SelfCheckoutStation;
import com.autovend.devices.observers.BillDispenserObserver;
import com.autovend.devices.observers.BillSlotObserver;
import com.autovend.devices.observers.BillStorageObserver;
import com.autovend.devices.observers.BillValidatorObserver;

/**
 * This class contains JUnit tests for the PayController class, which is responsible for handling
 * the payment process in a self-checkout system.
 */
public class PayWithCashTest {
    
    private SelfCheckoutStation scs;
    private SelfCheckoutLogic scl;
    private PayController payController;

    /**
     * Sets up the test by creating a mock self-checkout station and self-checkout logic object, 
     * and instantiating a PayController object with those objects.
     */
    @Before
    public void setUp() {
        scs = mock(SelfCheckoutStation.class);
        scl = mock(SelfCheckoutLogic.class);
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
     */
    @Test
    public void testReactToValidBillDetectedEvent() {
        Currency currency = Currency.getInstance("CAD");
        BillValidator validator = mock(BillValidator.class);

        payController.reactToValidBillDetectedEvent(validator, currency, 20);

        assertEquals(BigDecimal.valueOf(20), payController.funds);
        verify(scl.customer).notifyAmountDue(payController.amountDue);
    }

    /**
     * Tests the reactToValidBillDetectedEvent method of the PayController class when the system is 
     * disabled.
     */
    @Test(expected = DisabledException.class)
    public void testReactToValidBillDetectedEvent_whenSystemDisabled() {
        payController.selfCheckoutLogic.systemDisabled = true;
        Currency currency = Currency.getInstance("CAD");
        BillValidator validator = mock(BillValidator.class);

        payController.reactToValidBillDetectedEvent(validator, currency, 20);
    }

    /**
     * Tests the reactToValidBillDetectedEvent method of the PayController class when the amount due 
     * is zero.
     */
    @Test
    public void testReactToValidBillDetectedEvent_whenAmountDueZero() {
        Currency currency = Currency.getInstance("CAD");
        BillValidator validator = mock(BillValidator.class);

        payController.amountDue = BigDecimal.ZERO;
        payController.reactToValidBillDetectedEvent(validator, currency, 20);

        assertEquals(BigDecimal.valueOf(20), payController.funds);
    }
