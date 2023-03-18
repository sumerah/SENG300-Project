//SENG300 Project
//Group 47
//Student Names:
//Sumerah Rowshan (UCID: 30160897)
//Justin Chu (UCID: 30162809)
//Jitaksha Batish (UCID: 30116450)
//Fairooz Shafin (UCID: 30149774)
//AAL Farhan Ali (UCID: 30148704)


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
