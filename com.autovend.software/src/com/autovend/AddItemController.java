package com.autovend;
import java.math.BigDecimal;
import java.util.ArrayList;

import com.autovend.*;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.devices.*;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BarcodeScannerObserver;
import com.autovend.devices.observers.ElectronicScaleObserver;

public class AddItemController implements BarcodeScannerObserver, ElectronicScaleObserver{
	private SelfCheckoutStation selfCheckoutStation;
	public SelfCheckoutLogic selfCheckoutLogic;
	
	public AddItemController (SelfCheckoutStation scs, SelfCheckoutLogic scl) {
		selfCheckoutStation = scs;
		selfCheckoutLogic = scl;
		
		selfCheckoutStation.mainScanner.register(this);
		selfCheckoutStation.handheldScanner.register(this);
	}
	
	public void blockSystem() {
		selfCheckoutStation.mainScanner.disable();
		selfCheckoutStation.handheldScanner.disable();
	}
	
	public void unblockSystem() {
		selfCheckoutStation.mainScanner.enable();
		selfCheckoutStation.handheldScanner.enable();
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
	public void reactToBarcodeScannedEvent(BarcodeScanner barcodeScanner, Barcode barcode) {
		//Block selfCheckoutStation from further scanning/interaction
		blockSystem();
		
		//Grab weight and cost of the product associated with the barcode
		BarcodedProduct item = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
		BigDecimal price = item.getPrice();
		double weight = item.getExpectedWeight();
		
		//Update expectedWeight for the baggingArea
		selfCheckoutLogic.baggingAreaExpectedWeight += weight;
		
		//Create new BarcodedUnit to add to baggingArea
		BarcodedUnit unit = new BarcodedUnit(barcode, weight);
		selfCheckoutStation.baggingArea.add(unit);
		
		//Keep track of scanned items and total cost
		selfCheckoutLogic.scannedItems.add(item);
		BigDecimal result = selfCheckoutLogic.totalCost.add(price);
		selfCheckoutLogic.totalCost = result;

		//TODO signify to Customer I/O to place scanned item in the bagging area
		
		//Unblock the station
		unblockSystem();
		
	}


	@Override
	public void reactToWeightChangedEvent(ElectronicScale scale, double weightInGrams) {
		
	}


	@Override
	public void reactToOverloadEvent(ElectronicScale scale) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void reactToOutOfOverloadEvent(ElectronicScale scale) {
		// TODO Auto-generated method stub
		
	}

}
