package com.autovend;

public class AttendantStub {
	public int duplicateReceipt = 0;
	public int stationNeedsMaintenance = 0;
	
	
	public void notifyDuplicateReceipt() {
		duplicateReceipt += 1;
	}
	
	public void notifyStationNeedsMaintenance() {
		stationNeedsMaintenance = 1;
	}
}
