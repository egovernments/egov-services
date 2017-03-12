package org.egov.user.domain.search;

import org.egov.user.domain.model.UserSearch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserSearchStrategyFactoryTest {

    @Mock
    SearchStrategy searchStrategy1;

    @Mock
    SearchStrategy searchStrategy2;

    @Test
    public void should_find_suitable_strategy() throws Exception {
        UserSearch userSearch = new UserSearch();

        when(searchStrategy1.matches(userSearch)).thenReturn(false);
        when(searchStrategy2.matches(userSearch)).thenReturn(true);

        UserSearchStrategyFactory searchStrategyFactory =
                new UserSearchStrategyFactory(asList(searchStrategy1, searchStrategy2));

        SearchStrategy actualSearchStrategy = searchStrategyFactory.getSearchStrategy(userSearch);

        assertThat(searchStrategy2).isEqualTo(actualSearchStrategy);
    }

    @Test(expected = NoSearchStrategyFoundException.class)
    public void should_throw_exception_when_no_suitable_strategy() throws Exception {
        UserSearch userSearch = new UserSearch();

        when(searchStrategy1.matches(userSearch)).thenReturn(false);
        when(searchStrategy2.matches(userSearch)).thenReturn(false);

        UserSearchStrategyFactory searchStrategyFactory =
                new UserSearchStrategyFactory(asList(searchStrategy1, searchStrategy2));

        searchStrategyFactory.getSearchStrategy(userSearch);
    }
}