package org.egov.user.domain.search;

import org.egov.user.domain.model.UserSearch;
import org.egov.user.domain.search.UserIdSearchStrategy;
import org.egov.user.persistence.entity.User;
import org.egov.user.persistence.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserIdSearchStrategyTest {

    @Mock
    UserRepository userRepository;

    @Test
    public void test_should_match_if_request_has_id_present() throws Exception {
        UserIdSearchStrategy strategy = new UserIdSearchStrategy(userRepository);
        UserSearch userSearch = UserSearch.builder().id(asList(1L, 2L)).build();

        assertThat(strategy.matches(userSearch)).isTrue();
    }

    @Test
    public void test_should_not_match_if_request_has_no_id_present() throws Exception {
        UserIdSearchStrategy strategy = new UserIdSearchStrategy(userRepository);
        UserSearch userSearch = new UserSearch();

        assertThat(strategy.matches(userSearch)).isFalse();
    }

    @Test
    public void test_should_find_user_by_id() throws Exception {
        List<Long> ids = asList(1L, 2L);
        List<User> expectedListOfUsers = asList(User.builder().id(1L).build(), User.builder().id(2L).build());
        UserSearch userSearch = UserSearch.builder().id(ids).build();
        UserIdSearchStrategy userIdSearchStrategy = new UserIdSearchStrategy(userRepository);

        when(userRepository.findAll(ids)).thenReturn(expectedListOfUsers);

        List<User> actualListOfUsers = userIdSearchStrategy.search(userSearch);

        assertThat(expectedListOfUsers).isEqualTo(actualListOfUsers);
    }
}