package org.egov.tenant.persistence.repository;

import org.egov.tenant.domain.model.City;
import org.egov.tenant.persistence.repository.builder.CityQueryBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CityRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private CityQueryBuilder cityQueryBuilder;

    private CityRepository cityRepository;

    @Before
    public void setUp() throws Exception {
        cityRepository = new CityRepository(jdbcTemplate, cityQueryBuilder);
    }

    @Test
    public void test_should_save_city() {
        when(cityQueryBuilder.getInsertQuery()).thenReturn("insert query");

        List<Object> fields = new ArrayList<Object>() {{
            add("name");
            add("localname");
            add("districtcode");
            add("districtname");
            add("regionname");
            add(37.345);
            add(75.232);
            add("tenantCode");
            add(1L);
            add(1L);
        }};

        when(jdbcTemplate.update(eq("insert query"), argThat(new ListArgumentMatcher(fields)))).thenReturn(1);

        City city = City.builder()
                .name("name")
                .localName("localname")
                .districtCode("districtcode")
                .districtName("districtname")
                .regionName("regionname")
                .longitude(37.345)
                .latitude(75.232)
                .build();

        cityRepository.save(city, "tenantCode");

        verify(jdbcTemplate).update(eq("insert query"), argThat(new ListArgumentMatcher(fields)));
    }

    private class ListArgumentMatcher extends ArgumentMatcher<List<Object>> {

        private List<Object> expectedValue;

        public ListArgumentMatcher(List<Object> expectedValue) {
            this.expectedValue = expectedValue;
        }

        @Override
        public boolean matches(Object o) {
            ArrayList<Object> actualValue = (ArrayList<Object>) o;

            //Check if the dates are not null
            if (actualValue.get(9) == null && actualValue.get(11) == null) {
                return false;
            }

            return expectedValue.get(0).equals(actualValue.get(0)) &&
                    expectedValue.get(1).equals(actualValue.get(1)) &&
                    expectedValue.get(2).equals(actualValue.get(2)) &&
                    expectedValue.get(3).equals(actualValue.get(3)) &&
                    expectedValue.get(4).equals(actualValue.get(4)) &&
                    expectedValue.get(5).equals(actualValue.get(5)) &&
                    expectedValue.get(6).equals(actualValue.get(6)) &&
                    expectedValue.get(7).equals(actualValue.get(7)) &&
                    expectedValue.get(8).equals(actualValue.get(8)) &&
                    expectedValue.get(9).equals(actualValue.get(10));
        }
    }
}