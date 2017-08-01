package org.egov.tradelicense.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ UomServiceTest.class,
	DocumentTypeServiceTest.class,
	CategoryServiceTest.class,
	PenaltyRateServiceTest.class,
	FeeMatrixServiceTest.class
	})
public class MasterServiceTestSuite {

}
