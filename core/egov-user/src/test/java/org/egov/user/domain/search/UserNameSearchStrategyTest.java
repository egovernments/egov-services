package org.egov.user.domain.search;

import org.egov.user.domain.model.UserSearch;
import org.egov.user.domain.search.UserNameSearchStrategy;
import org.egov.user.persistence.entity.User;
import org.egov.user.persistence.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserNameSearchStrategyTest {

    @Mock
    UserRepository userRepository;

    @Test
    public void test_should_match_if_request_has_userName_present() throws Exception {
        UserNameSearchStrategy strategy = new UserNameSearchStrategy(userRepository);
        UserSearch userSearch = UserSearch.builder().userName("userName").build();

        assertThat(strategy.matches(userSearch)).isTrue();
    }

    @Test
    public void test_should_not_match_if_request_has_no_userName_present() throws Exception {
        UserNameSearchStrategy strategy = new UserNameSearchStrategy(userRepository);
        UserSearch userSearch = new UserSearch();

        assertThat(strategy.matches(userSearch)).isFalse();
    }

    @Test
    public void test_should_find_user_by_userName() throws Exception {
        String userName = "userName";
        User expectedUser = User.builder().id(1L).build();
        UserSearch userSearch = UserSearch.builder().userName("userName").build();
        UserNameSearchStrategy userNameSearchStrategy = new UserNameSearchStrategy(userRepository);

        when(userRepository.findByUsername(userName)).thenReturn(expectedUser);

        List<User> actualListOfUsers = userNameSearchStrategy.search(userSearch);

        assertThat(expectedUser).isEqualTo(actualListOfUsers.get(0));
    }
}