
package com.autovend;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import com.autovend.*;
import com.autovend.devices.*;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.ReceiptPrinterObserver;
import com.autovend.products.BarcodedProduct;

/**
 * Controls the logic when for receipt related things
 * @author Justin Chu, 30162809
 * @author Jitaksha Batish, 30116450
 * @author Sumerah Rowshan, 30160897
 * @author Fairooz Shafin, 30149774
 * @author AAL Farhan Ali, 30148704
 */
public class PrintReceiptController implements ReceiptPrinterObserver {
	private ReceiptPrinter printer;
	private ArrayList<BarcodedProduct> scannedItems;
	private SelfCheckoutStation selfCheckoutStation;
	private SelfCheckoutLogic selfCheckoutLogic;

	/**
	 * Basic Constructor
	 * 
	 * @param scs
	 * 				The self-checkout station
	 * @param scl
	 * 				The self-checkout logic
	 */
	public PrintReceiptController(SelfCheckoutStation scs, SelfCheckoutLogic scl) {
		scannedItems = scl.scannedItems;
		selfCheckoutStation = scs;
		selfCheckoutLogic = scl;
		printer = scs.printer;
		
		//register ReceiptPrinter and listen for events
		printer.register(this);
	}
	
	/**
	 * Method for building and returning the receipt
	 * 1. Pulls the details of the transaction from scannedItems
	 * 2. Prints to the receipt each item description and price
	 * 3. Notifies the customer that their session is complete
	 * 4. Thanks the customer and notifies that the station is ready for a new customer session
	 * Receipt Format
	 * "ITEMS:
	 * item1: $X.XX
	 * item2: $X.XX
	 * ...
	 * itemx: $X.XX
	 * TOTAL: $X.XX"
	 * @return String
	 * 					returns a string that contains all the details of the payment
	 * @throws OverloadException
	 * 					when current line is too long (needs to be broken up by a \n)
	 * @throws EmptyException
	 * 					when there is either no more ink or paper in the station (or both)
	 */
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
