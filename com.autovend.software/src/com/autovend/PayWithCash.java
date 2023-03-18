package com.autovend;
import com.autovend.*;
import com.autovend.devices.*;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BillSlotObserver;

public class PayWithCash implements BillSlotObserver{

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

}
