package com.autovend;

/**
 * Temporary Attendant Stub for testing purposes
 * @author Justin Chu, 30162809
 * @author Jitaksha Batish, 30116450
 * @author Sumerah Rowshan, 30160897
 * @author Fairooz Shafin, 30149774
 * @author AAL Farhan Ali, 30148704
 */
public class AttendantStub {
	public boolean duplicateReceipt = false;
	public boolean stationNeedsMaintenance = false;
	
	
	public void notifyDuplicateReceipt() {
		duplicateReceipt =true;
	}
	
	public void notifyStationNeedsMaintenance() {
		stationNeedsMaintenance = true;
	}
}
