package org.egov.tradelicense.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CategoryServiceTest.class, 
	DocumentTypeServiceTest.class,
	FeeMatrixServiceTest.class,
	PenaltyRateServiceTest.class,
	UomServiceTest.class})
public class MasterServiceTestSuite {

}
