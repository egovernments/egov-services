package org.egov.eis.persistance.repository;

import org.egov.common.contract.request.RequestInfo;
import org.egov.eis.TestConfiguration;
import org.egov.eis.model.DepartmentDesignation;
import org.egov.eis.model.Position;
import org.egov.eis.repository.PositionRepository;
import org.egov.eis.web.contract.PositionGetRequest;
import org.egov.eis.web.contract.PositionRequest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@Import(TestConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)

public class PositionRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PositionRepository positionRepository;

    @Test
    @Sql(scripts = {"/sql/clearposition.sql", "/sql/insertposition.sql"})
    public void test_should_find_all_position() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        PositionGetRequest positionGetRequest = new PositionGetRequest();
        positionGetRequest.builder().id(ids).tenantId("default").build();
        List<Position> positions = positionRepository.findForCriteria(positionGetRequest);
        assertEquals(1,positions.size());
    }

    @Test
    @Sql(scripts = {"/sql/clearpositionanddeptdesig.sql", "/sql/insertdeptdesig.sql"})
    public void test_should_create_position() {
        List<Position> positions = new ArrayList<>();
        Position position = new Position();
        position.setName("Executive Engineer Test");
        position.setId(1L);
        position.setActive(true);
        position.setIsPostOutsourced(true);
        position.setTenantId("default");
        DepartmentDesignation departmentDesignation = new DepartmentDesignation();
        departmentDesignation.setId(1L);
        departmentDesignation.setTenantId("default");
        position.setDeptdesig(departmentDesignation);
        positions.add(position);
        PositionRequest positionRequest = new PositionRequest();
        positionRequest.setPosition(positions);
        positionRequest.setRequestInfo(new RequestInfo());
        positionRepository.create(positionRequest);
        PositionGetRequest positionGetRequest = new PositionGetRequest();
        positionGetRequest.builder().name("Executive Engineer Test").tenantId("default").build();
        List<Position> positions1 = positionRepository.findForCriteria(positionGetRequest);
        assertEquals(1,positions1.size());
    }

    @Test
    @Sql(scripts = {"/sql/clearposition.sql", "/sql/insertposition.sql"})
    public void test_should_update_position() {
        List<Position> positions = new ArrayList<>();
        Position position = new Position();
        position.setId(1L);
        position.setActive(false);
        position.setIsPostOutsourced(true);
        position.setTenantId("default");
        positions.add(position);
        PositionRequest positionRequest = new PositionRequest();
        positionRequest.setPosition(positions);
        positionRequest.setRequestInfo(new RequestInfo());
        positionRepository.update(positionRequest);
        PositionGetRequest positionGetRequest = new PositionGetRequest();
        positionGetRequest.builder().active(Boolean.FALSE).tenantId("default").build();
        positionGetRequest.setActive(false);
        List<Position> positions1 = positionRepository.findForCriteria(positionGetRequest);
        assertEquals(Boolean.FALSE,positions1.get(0).getActive());
    }

    @Test
    @Sql(scripts = {"/sql/clearposition.sql", "/sql/insertposition.sql"})
    public void test_should_genrate_name() {
        assertEquals("Executive_Engineer_002",positionRepository.generatePositionNameWithMultiplePosition("Executive_Engineer_",1L,"default",1));
    }

    @Test
    @Sql(scripts = {"/sql/clearposition.sql", "/sql/insertposition.sql"})
    public void test_should_genrate_name_not_equals() {
        assertNotEquals("Executive_Engineer",positionRepository.generatePositionNameWithMultiplePosition("Executive_Engineer_1",1L,"default",1));
    }

    @Test(expected = RuntimeException.class)
    @Sql(scripts = {"/sql/clearpositionanddeptdesig.sql", "/sql/insertdeptdesig.sql"})
    public void test_should_create_position_fail(){
        List<Position> positions = new ArrayList<>();
        Position position = new Position();
        position.setName("Executive Engineer Test");
        position.setActive(true);
        position.setIsPostOutsourced(true);
        position.setTenantId("default");
        DepartmentDesignation departmentDesignation = new DepartmentDesignation();
        departmentDesignation.setId(1L);
        departmentDesignation.setTenantId("default");
        position.setDeptdesig(departmentDesignation);
        positions.add(position);
        PositionRequest positionRequest = new PositionRequest();
        positionRequest.setPosition(positions);
        positionRequest.setRequestInfo(new RequestInfo());
        positionRepository.create(positionRequest);
    }

}
