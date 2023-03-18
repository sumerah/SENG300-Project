
package com.autovend;

import com.autovend.*;
import com.autovend.devices.*;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.ElectronicScaleObserver;
import com.autovend.devices.observers.ReceiptPrinterObserver;



public class PrintReceipt implements ElectronicScaleObserver, ReceiptPrinterObserver {
	private static final long serialVersionUID = 2485932101191989634L;
	private int charactersOfInkRemaining = 0;
	
	public static final int MAXIMUM_INK = 1 << 20;

	public void ScanReceipt(SelfCheckoutLogic scl) {
		scannedItems = scl.scannedItems;
	}
	
	public void print() {
		Bill bill = new Bill(value, currency);
		int value = bill.getValue();
		Currency currency = bill.getCurrency;
		for (Bill bill : scannedItems) {
			value = bill.getValue();
			String itemName = bill.getDesccription();
		}
	
	}
	
	
	// Exception thrown if the printer runs out of ink
	public void addInk(int quantity) throws OverloadException {
		if(quantity < 0)
			throw new SimulationException("Are you trying to remove ink?");

		if(charactersOfInkRemaining + quantity > MAXIMUM_INK)
			throw new OverloadException("You spilled a bunch of ink!");

		if(quantity > 0) {
			charactersOfInkRemaining += quantity;

			for(ReceiptPrinterObserver observer : observers)
				observer.reactToInkAddedEvent(this);
		}
	}
}
