
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
			System.out.println(printer.sb);
			
			//Get item price and convert to string
			BigDecimal bdPrice = item.getPrice();
			bdPrice = bdPrice.setScale(2, RoundingMode.HALF_UP);
			String sPrice = bdPrice.toString();
			
			//Print item price to receipt
			for (int i = 0; i < sPrice.length(); i++) {
				printer.print(sPrice.charAt(i));
			}
			printer.print('\n');
		}
		
		//Print "TOTAL:" for total cost of items
		printer.print('T'); printer.print('O'); printer.print('T'); printer.print('A'); printer.print('L'); printer.print(':'); printer.print(' ');
		
		//Get total cost of items and convert to string
		BigDecimal bdTotalCost = selfCheckoutLogic.totalCost;
		bdTotalCost = bdTotalCost.setScale(2, RoundingMode.HALF_UP);
		String sTotalCost = bdTotalCost.toString();
		
		//Print total cost to receipt
		for (int i = 0; i < sTotalCost.length(); i++) {
			printer.print(sTotalCost.charAt(i));
		}
		
		printer.cutPaper();
		String printedReceipt = printer.removeReceipt();
		
		return printedReceipt;
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
	public void reactToOutOfPaperEvent(ReceiptPrinter printer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToOutOfInkEvent(ReceiptPrinter printer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToPaperAddedEvent(ReceiptPrinter printer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToInkAddedEvent(ReceiptPrinter printer) {
		// TODO Auto-generated method stub
		
	}
}
