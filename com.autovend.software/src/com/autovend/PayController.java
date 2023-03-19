package com.autovend;
import java.math.BigDecimal;
import java.util.Currency;

import com.autovend.*;
import com.autovend.devices.*;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BillDispenserObserver;
import com.autovend.devices.observers.BillSlotObserver;
import com.autovend.devices.observers.BillStorageObserver;
import com.autovend.devices.observers.BillValidatorObserver;

/**
 * Controls the logic when customer is ready to pay
 * @author Justin Chu, 30162809
 * @author Jitaksha Batish, 30116450
 * @author Sumerah Rowshan, 30160897
 * @author Fairooz Shafin, 30149774
 * @author AAL Farhan Ali, 30148704
 */
public class PayController implements BillSlotObserver, BillDispenserObserver, BillValidatorObserver, BillStorageObserver{
	private SelfCheckoutStation selfCheckoutStation;
	private SelfCheckoutLogic selfCheckoutLogic;
	private BigDecimal totalCost;
	private BigDecimal amountDue;
	private BigDecimal funds; 
	
	/**
	 * Basic Constructor
	 * 
	 * @param scs
	 * 				The self-checkout station
	 * @param scl
	 * 				The self-checkout logic
	 */
	public PayController(SelfCheckoutStation scs, SelfCheckoutLogic scl) {
		selfCheckoutStation = scs;
		selfCheckoutLogic = scl;
		totalCost = scl.totalCost;
		amountDue = scl.amountDue;
		funds = scl.funds;
		
		//Register all bill related hardware and listen for events
		scs.billInput.register(this);
		scs.billValidator.register(this);
		scs.billStorage.register(this);
		scs.billOutput.register(this);
		
		//Register all dispenser instances
		for(int i = 0; i < scs.billDenominations.length; i++) {
			scs.billDispensers.get(scs.billDenominations[i]).register(this);
		}
		
		//Notify the customer the amountDue
		selfCheckoutLogic.customer.notifyAmountDue(amountDue);
		
		//System is ready for insertion of bills
		scs.billInput.enable();
	}
	
	/**
	 * Reaction for when a valid bill is inserted
	 * 1. add inserted bill to funds
	 * 2. update the amountDue (amountDue = totalCost - funds)
	 * 3. if amountDue >= 0 notify the customer about the amountDue and wait for further bills
	 * 4. if amountDue <= 0 notify the customer about the amountDue and dispense change (not yet implemented because of coins)
	 * 5. the printing of the receipt would follow (not done here)
	 */
	@Override
	public void reactToValidBillDetectedEvent(BillValidator validator, Currency currency, int value) {
		//Only run if station is enabled
		if (selfCheckoutLogic.systemDisabled) {
			throw new DisabledException();
		}
		BigDecimal v = new BigDecimal(value);
		funds = funds.add(v);
		System.out.println(funds);
		amountDue = totalCost.subtract(funds);
		System.out.println(amountDue);
		if (amountDue.compareTo(new BigDecimal(0.00)) <= 0) {
			//TODO Dispense Change function after coins are added to system 
		} else {
			selfCheckoutLogic.customer.amountDue = amountDue;
		}
	}
	
	@Override
	public void reactToInvalidBillDetectedEvent(BillValidator validator) {
		selfCheckoutLogic.customer.notifyAmountDue(totalCost);
	}

	@Override
	public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToBillInsertedEvent(BillSlot slot) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToBillEjectedEvent(BillSlot slot) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToBillRemovedEvent(BillSlot slot) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToBillsFullEvent(BillStorage unit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToBillAddedEvent(BillStorage unit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToBillsLoadedEvent(BillStorage unit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToBillsUnloadedEvent(BillStorage unit) {
		// TODO Auto-generated method stub
		
	}


	

	@Override
	public void reactToBillsFullEvent(BillDispenser dispenser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToBillsEmptyEvent(BillDispenser dispenser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToBillAddedEvent(BillDispenser dispenser, Bill bill) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToBillRemovedEvent(BillDispenser dispenser, Bill bill) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToBillsLoadedEvent(BillDispenser dispenser, Bill... bills) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToBillsUnloadedEvent(BillDispenser dispenser, Bill... bills) {
		// TODO Auto-generated method stub
		
	}

}
