package com.autovend;
import java.math.BigDecimal;
import java.math.RoundingMode;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.devices.*;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BarcodeScannerObserver;
import com.autovend.devices.observers.ElectronicScaleObserver;

/**
 * Controls the logic when items are added to the baggingArea
 * @author Justin Chu, 30162809
 * @author Jitaksha Batish, 30116450
 * @author Sumerah Rowshan, 30160897
 * @author Fairooz Shafin, 30149774
 * @author AAL Farhan Ali, 30148704
 */
public class AddItemController implements BarcodeScannerObserver, ElectronicScaleObserver{
	private SelfCheckoutStation selfCheckoutStation;
	public SelfCheckoutLogic selfCheckoutLogic;
	
	/**
	 * Basic Constructor
	 * 
	 * @param scs
	 * 				The self-checkout station
	 * @param scl
	 * 				The self-checkout logic
	 */
	public AddItemController (SelfCheckoutStation scs, SelfCheckoutLogic scl) {
		selfCheckoutStation = scs;
		selfCheckoutLogic = scl;
		
		//register the station's scanners and listen to events
		selfCheckoutStation.mainScanner.register(this);
		selfCheckoutStation.handheldScanner.register(this);
	}

	/**
	 * Reaction for when a product is scanned
	 * 1. Blocks the station from further customer interaction
	 * 2. Determine the expected weight and price of the product (retrieve from BARCODED_PRODUCT_DATABASE)
	 * 3. Update expected weight of the baggingArea
	 * 4. Notifies the customer to place the product in the baggingArea
	 * 5. Hardware signifies to the system that the weight has changed
	 * 6. Unblocks the station for further scanning
	 */
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
