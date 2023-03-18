//SENG300 Project
//Group 47
//Student Names:
//Sumerah Rowshan (UCID: 30160897)
//Justin Chu (UCID: 30162809)
//Jitaksha Batish (UCID: 30116450)
//Fairooz Shafin (UCID: 30149774)
//AAL Farhan Ali (UCID: 30148704)


package com.autovend;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import com.autovend.*;
import com.autovend.devices.*;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.ReceiptPrinterObserver;
import com.autovend.products.BarcodedProduct;



public class PrintReceiptController implements ReceiptPrinterObserver {
	private ReceiptPrinter printer;
	private ArrayList<BarcodedProduct> scannedItems;
	private SelfCheckoutStation selfCheckoutStation;
	private SelfCheckoutLogic selfCheckoutLogic;

	public PrintReceiptController(SelfCheckoutStation scs, SelfCheckoutLogic scl) {
		scannedItems = scl.scannedItems;
		selfCheckoutStation = scs;
		selfCheckoutLogic = scl;
		printer = scs.printer;
		
		printer.register(this);
	}
	
	public String printReceipt() throws OverloadException, EmptyException {
		//Only run if station is enabled
		if (selfCheckoutLogic.systemDisabled) {
			throw new DisabledException();
		}
		
		//Print "ITEMS:" title to receipt
		printer.print('I'); printer.print('T'); printer.print('E'); printer.print('M'); printer.print('S'); printer.print(':'); printer.print('\n');
		//For every item in the baggingArea
		for (BarcodedProduct item : scannedItems) {
			//Get item description
			String description = item.getDescription();
			
			//Print item description to receipt
			for (int i = 0; i < description.length(); i++) {
				printer.print(description.charAt(i));
			}
			printer.print(':'); printer.print(' ');
			
			//Get item price and convert to string
			BigDecimal bdPrice = item.getPrice();
			bdPrice = bdPrice.setScale(2, RoundingMode.HALF_UP);
			String sPrice = bdPrice.toString();
			
			printer.print('$');
			//Print item price to receipt
			for (int i = 0; i < sPrice.length(); i++) {
				printer.print(sPrice.charAt(i));
			}
			printer.print('\n');
		}
		
		//Print "TOTAL:" for total cost of items
		printer.print('T'); printer.print('O'); printer.print('T'); printer.print('A'); printer.print('L'); printer.print(':'); printer.print(' ');
		printer.print('$');
		
		//Get total cost of items and convert to string
		BigDecimal bdTotalCost = selfCheckoutLogic.totalCost;
		bdTotalCost = bdTotalCost.setScale(2, RoundingMode.HALF_UP);
		String sTotalCost = bdTotalCost.toString();
		
		//Print total cost to receipt
		for (int i = 0; i < sTotalCost.length(); i++) {
			printer.print(sTotalCost.charAt(i));
		}
		
		//Cut (end) receipt
		printer.cutPaper();
		String printedReceipt = printer.removeReceipt();
		
		//Notify customer
		selfCheckoutLogic.customer.notifySessionDone();
		selfCheckoutLogic.customer.thankCustomer();
		selfCheckoutLogic.customer.readyForNewCustomer();
		
		return printedReceipt;
	}

	@Override
	public void reactToOutOfPaperEvent(ReceiptPrinter printer) {
		//Notify attendant
		selfCheckoutLogic.attendant.notifyStationNeedsMaintenance();
		selfCheckoutLogic.attendant.notifyDuplicateReceipt();
		//Disable station
		selfCheckoutLogic.disable();
	}

	@Override
	public void reactToOutOfInkEvent(ReceiptPrinter printer) {
		//Notify attendant
		selfCheckoutLogic.attendant.notifyStationNeedsMaintenance();
		selfCheckoutLogic.attendant.notifyDuplicateReceipt();
		//Disable station
		selfCheckoutLogic.disable();
		
	}

	@Override
	public void reactToPaperAddedEvent(ReceiptPrinter printer) {
		selfCheckoutLogic.enable();
		
	}

	@Override
	public void reactToInkAddedEvent(ReceiptPrinter printer) {
		selfCheckoutLogic.enable();
		
	}
	
	@Override
	public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub
		
	}
}
