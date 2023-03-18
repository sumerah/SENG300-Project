//SENG300 Project
//Group 47
//Student Names:
//Sumerah Rowshan (UCID: 30160897)
//Justin Chu (UCID: 30162809)
//Jitaksha Batish (UCID: 30116450)
//Fairooz Shafin (UCID: 30149774)
//AAL Farhan Ali (UCID: 30148704)



package com.autovend.software.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.autovend.PrintReceiptController;
import com.autovend.SelfCheckoutLogic;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
 

public class PrintReceiptTest extends Tests {
	
	private PrintReceiptController controller;
	private SelfCheckoutStation selfCheckoutStation;
	private SelfCheckoutLogic selfCheckoutLogic;

	@Before
	public void setup() throws OverloadException {
		super.generalSetup();
		
		selfCheckoutStation = scs;
		selfCheckoutLogic = scl
		
		
		controller = new PrintReceiptController(scs, selfCheckoutLogic);
	}
	
	
	@Test
	public void test() {
		
	}

}
