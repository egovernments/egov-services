package org.egov.user.domain.search;

import org.egov.user.domain.model.UserSearch;
import org.egov.user.domain.search.MultiFieldsMatchingStrategy;
import org.egov.user.persistence.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class MultiFieldsMatchingStrategyTest {

    @Mock
    UserRepository userRepository;

    @Test
    public void test_should_match_if_request_has_name_present() throws Exception {
        MultiFieldsMatchingStrategy strategy = new MultiFieldsMatchingStrategy(userRepository);
        UserSearch userSearch = UserSearch.builder().name("name").build();

        assertThat(strategy.matches(userSearch)).isTrue();
    }

    @Test
    public void test_should_match_if_request_has_mobileNumber_present() throws Exception {
        MultiFieldsMatchingStrategy strategy = new MultiFieldsMatchingStrategy(userRepository);
        UserSearch userSearch = UserSearch.builder().mobileNumber("mobileNumber").build();

        assertThat(strategy.matches(userSearch)).isTrue();
    }

    @Test
    public void test_should_match_if_request_has_emailId_present() throws Exception {
        MultiFieldsMatchingStrategy strategy = new MultiFieldsMatchingStrategy(userRepository);
        UserSearch userSearch = UserSearch.builder().emailId("emailId").build();

        assertThat(strategy.matches(userSearch)).isTrue();
    }

    @Test
    public void test_should_match_if_request_has_pan_present() throws Exception {
        MultiFieldsMatchingStrategy strategy = new MultiFieldsMatchingStrategy(userRepository);
        UserSearch userSearch = UserSearch.builder().pan("pan").build();

        assertThat(strategy.matches(userSearch)).isTrue();
    }

    @Test
    public void test_should_match_if_request_has_aadhaarNumber_present() throws Exception {
        MultiFieldsMatchingStrategy strategy = new MultiFieldsMatchingStrategy(userRepository);
        UserSearch userSearch = UserSearch.builder().aadhaarNumber("aadharNumber").build();

        assertThat(strategy.matches(userSearch)).isTrue();
    }
}