package org.egov.lams.repository;

import org.egov.lams.model.RentIncrementType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RentIncrementRepositoryTest {

    @Mock
    JdbcTemplate jdbcTemplate;

    @Mock
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @InjectMocks
    private RentIncrementRepository rentIncrementRepository;

    @Test
    public void getRentIncrementsTest() {
        List<RentIncrementType> rentIncrements = new ArrayList<>();
        RentIncrementType rentIncrementType = new RentIncrementType();
        rentIncrementType.setId(1l);
        rentIncrementType.setTenantId("1");
        rentIncrements.add(rentIncrementType);

        when(namedParameterJdbcTemplate.query(any(String.class),any(Map.class), Matchers.<BeanPropertyRowMapper>any())).thenReturn(rentIncrements);
        assertTrue(rentIncrements.equals(rentIncrementRepository.getRentIncrements(rentIncrementType.getTenantId(), null)));
    }

    @Test
    public void getRentIncrementByIdTest() {

        List<RentIncrementType> rentIncrements = new ArrayList<>();
        RentIncrementType rentIncrementType = new RentIncrementType();
        rentIncrementType.setId(1l);
        Long rentId = 1l;
        rentIncrements.add(rentIncrementType);

        when(namedParameterJdbcTemplate.query(any(String.class), any(Map.class), Matchers.<BeanPropertyRowMapper>any()))
                .thenReturn(rentIncrements);

        assertTrue(rentIncrements.equals(rentIncrementRepository.getRentIncrementById(rentId)));
    }
}
