package org.egov.workflow.repository.rowmapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.util.List;

import org.egov.workflow.domain.model.PersistRouter;
import org.egov.workflow.persistence.repository.rowmapper.PersistRouteRowMapper;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

public class PersistRouterRowMapperTest {
ResultSet rs;
	
	@InjectMocks
	private PersistRouteRowMapper persistRouteRowMapper ;

	@Test
	public void test_should_map_result_set_to_entity() throws Exception {
		
		Mockito.when(rs.next()).thenReturn(true).thenReturn(false);
		
		
		when(rs.getLong("id")).thenReturn(1l);
		when(rs.getInt("bndryid")).thenReturn(1);
		when(rs.getLong("complainttypeid")).thenReturn(1l);
		when(rs.getInt("position")).thenReturn(1);
		when(rs.getString("tenantid")).thenReturn(null);
		
		
		PersistRouteRowMapper persistRouteRowMapper = new PersistRouteRowMapper();
		
		PersistRouter persistRouters = persistRouteRowMapper.mapRow(rs,0);
		
		PersistRouter persistRouter = getRouter();
		assertThat(persistRouters.equals(persistRouter));

	}
    
	private PersistRouter getRouter() {
		PersistRouter pr = new PersistRouter();
		pr.setId(1l);
		pr.setBoundary(1);
		pr.setPosition(1);
        pr.setService(1l);
        
        return pr;
	}
	
}
