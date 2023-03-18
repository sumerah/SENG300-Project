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

public class PayController implements BillSlotObserver, BillDispenserObserver, BillValidatorObserver, BillStorageObserver{
	private SelfCheckoutStation selfCheckoutStation;
	private SelfCheckoutLogic selfCheckoutLogic;
	private BigDecimal totalCost;
	private BigDecimal amountDue;
	private BigDecimal funds; 
	
	public PayController(SelfCheckoutStation scs, SelfCheckoutLogic scl) {
		selfCheckoutStation = scs;
		selfCheckoutLogic = scl;
		totalCost = scl.totalCost;
		amountDue = scl.amountDue;
		funds = scl.funds;
		
		scs.billInput.register(this);
		scs.billValidator.register(this);
		scs.billStorage.register(this);
		scs.billOutput.register(this);
		
		for(int i = 0; i < scs.billDenominations.length; i++) {
			scs.billDispensers.get(scs.billDenominations[i]).register(this);
		}
		
		//Notify the customer the amountDue
		selfCheckoutLogic.customer.notifyAmountDue(amountDue);
		
		//System is ready for insertion of bills
		scs.billInput.enable();
	}
	
	@Override
	public void reactToValidBillDetectedEvent(BillValidator validator, Currency currency, int value) {
		//Only run if station is enabled
		if (selfCheckoutLogic.systemDisabled) {
			throw new DisabledException();
		}
		
		funds = funds.add(BigDecimal.valueOf(value));
		amountDue = totalCost.subtract(funds);
		if (amountDue.compareTo(new BigDecimal(0.00)) <= 0) {
			//TODO Dispense Change function after coins are added to system 
		} else {
			selfCheckoutLogic.customer.notifyAmountDue(amountDue);
		}
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
	public void reactToInvalidBillDetectedEvent(BillValidator validator) {
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
