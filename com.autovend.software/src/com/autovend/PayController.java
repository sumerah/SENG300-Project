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

public class PayControllerTest {
    private SelfCheckoutStation scs;
    private SelfCheckoutLogic scl;
    private PayController payController;

    @Before
    public void setUp() {
        scs = mock(SelfCheckoutStation.class);
        scl = mock(SelfCheckoutLogic.class);
        payController = new PayController(scs, scl);
    }

    @Test
    public void testConstructor() {
        assertNotNull(payController);
    }

    @Test
    public void testReactToValidBillDetectedEvent() {
        Currency currency = Currency.getInstance("CAD");
        BillValidator validator = mock(BillValidator.class);

        payController.reactToValidBillDetectedEvent(validator, currency, 20);

        assertEquals(BigDecimal.valueOf(20), payController.funds);
        verify(scl.customer).notifyAmountDue(payController.amountDue);
    }

    @Test(expected = DisabledException.class)
    public void testReactToValidBillDetectedEvent_whenSystemDisabled() {
        payController.selfCheckoutLogic.systemDisabled = true;
        Currency currency = Currency.getInstance("CAD");
        BillValidator validator = mock(BillValidator.class);

        payController.reactToValidBillDetectedEvent(validator, currency, 20);
    }

    @Test
    public void testReactToValidBillDetectedEvent_whenAmountDueZero() {
        Currency currency = Currency.getInstance("CAD");
        BillValidator validator = mock(BillValidator.class);

        payController.amountDue = BigDecimal.ZERO;
        payController.reactToValidBillDetectedEvent(validator, currency, 20);

        assertEquals(BigDecimal.valueOf(20), payController.funds);
    }

    @Test
    public void testReactToValidBillDetectedEvent_whenAmountDueNegative() {
        Currency currency = Currency.getInstance("CAD");
        BillValidator validator = mock(BillValidator.class);
        BillDispenser dispenser = mock(BillDispenser.class);
        BillDenomination denomination = BillDenomination.TWENTY;

        payController.amountDue = BigDecimal.valueOf(-10);
        payController.totalCost = BigDecimal.valueOf(10);
        payController.scs.billDispensers.put(denomination, dispenser);
        payController.scs.billDenominations = new BillDenomination[] { denomination };
        payController.scs.billOutput = mock(BillOutput.class);

        payController.reactToValidBillDetectedEvent(validator, currency, 20);

        assertEquals(BigDecimal.valueOf(20), payController.funds);
        verify(scl.customer).notifyAmountDue(payController.amountDue);
        verify(dispenser).dispenseBills(new Bill[] { new Bill(denomination) });
    }

    @Test
    public void test