package com.autovend;

import com.autovend.*;
import com.autovend.devices.*;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.ElectronicScaleObserver;
import com.autovend.devices.observers.ReceiptPrinterObserver;



public class PrintReceipt implements ElectronicScaleObserver, ReceiptPrinterObserver {
	private static final long serialVersionUID = 2485932101191989634L;
	private int charactersOfInkRemaining = 0;
	

	public void print() {
		Bill bill = new Bill(value, currency);
	}
	
}

