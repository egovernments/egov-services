package org.egov.user.domain.search;

import org.egov.user.domain.model.UserSearch;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserSearchStrategyFactory {

    private List<SearchStrategy> searchStrategies;

    public UserSearchStrategyFactory(List<SearchStrategy> searchStrategies) {
        this.searchStrategies = searchStrategies;
    }

    public SearchStrategy getSearchStrategy(UserSearch userSearch) {
        Optional<SearchStrategy> result = searchStrategies.stream()
                .filter(searchStrategy -> searchStrategy.matches(userSearch))
                .findFirst();

        if( result.isPresent() ) {
            return result.get();
        }
        else {
            throw new NoSearchStrategyFoundException();
        }
    }
}
