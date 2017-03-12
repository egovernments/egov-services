package org.egov.user.domain.search;


import com.querydsl.core.types.Predicate;
import org.egov.user.domain.model.UserSearch;
import org.egov.user.domain.search.FuzzyNameMatchingSearchStrategy;
import org.egov.user.persistence.entity.User;
import org.egov.user.persistence.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FuzzyNameMatchingSearchStrategyTest {

    @Mock
    UserRepository userRepository;

    @Test
    public void test_should_match_if_fuzzyLogic_is_true_and_name_is_present() throws Exception {
        FuzzyNameMatchingSearchStrategy strategy = new FuzzyNameMatchingSearchStrategy(userRepository);
        UserSearch userSearch = UserSearch.builder().name("name").fuzzyLogic(true).build();

        assertThat(strategy.matches(userSearch)).isTrue();
    }

    @Test
    public void test_should_not_match_if_fuzzyLogic_is_false_or_name_is_not_present() throws Exception {
        FuzzyNameMatchingSearchStrategy strategy = new FuzzyNameMatchingSearchStrategy(userRepository);
        UserSearch userSearch = new UserSearch();

        assertThat(strategy.matches(userSearch)).isFalse();
    }

    @Test
    public void test_should_find_user_by_userName() throws Exception {
        Iterable<User> expectedListOfUsers = asList(User.builder().id(1L).build());
        UserSearch userSearch = UserSearch.builder().name("name").fuzzyLogic(true).build();
        FuzzyNameMatchingSearchStrategy userNameSearchStrategy = new FuzzyNameMatchingSearchStrategy(userRepository);

        when(userRepository.findAll(any(Predicate.class))).thenReturn(expectedListOfUsers);

        List<User> actualListOfUsers = userNameSearchStrategy.search(userSearch);

        assertThat(expectedListOfUsers).isEqualTo(actualListOfUsers);
    }
}