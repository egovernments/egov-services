package org.egov;

import org.egov.property.PtPropertyApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes={PtPropertyApplication.class})
public class PtPropertyApplicationTests {

	@Test
	public void contextLoads() {
	}

}
