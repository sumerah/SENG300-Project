package com.autovend;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

	@Override
	public void reactToBarcodeScannedEvent(BarcodeScanner barcodeScanner, Barcode barcode) {
		//Only run if station is enabled
		if (selfCheckoutLogic.systemDisabled) {
			throw new DisabledException();
		}
		
		//Block selfCheckoutStation from further scanning/interaction
		selfCheckoutLogic.disable();
		
		//Grab weight and cost of the product associated with the barcode
		BarcodedProduct item = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
		BigDecimal price = item.getPrice();
		price = price.setScale(2, RoundingMode.HALF_UP);
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
		selfCheckoutLogic.amountDue = result;

		//Signify to Customer I/O to place scanned item in the bagging area
		selfCheckoutLogic.customer.notifyPlaceItemInBaggingArea();
		
		//Unblock the station
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
