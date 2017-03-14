package org.egov.user.persistence.specification;

import org.egov.user.domain.model.UserSearch;
import org.junit.Test;
import org.springframework.data.jpa.domain.Specification;

import static org.assertj.core.api.Assertions.assertThat;

public class UserSearchSpecificationFactoryTest {

    @Test
    public void test_should_return_fuzzy_specification_if_fuzzyLogic_is_true() throws Exception {
        UserSearch userSearch = UserSearch.builder().name("name").fuzzyLogic(true).build();

        UserSearchSpecificationFactory userSearchSpecificationFactory = new UserSearchSpecificationFactory();

        Specification specification = userSearchSpecificationFactory.getSpecification(userSearch);
        assertThat(specification).isInstanceOf(FuzzyNameMatchingSpecification.class);
    }

    @Test
    public void test_should_return_multiFieldSpecification_if_not_fuzzyLogic() throws Exception {
        UserSearch userSearch = UserSearch.builder().fuzzyLogic(false).build();

        UserSearchSpecificationFactory userSearchSpecificationFactory = new UserSearchSpecificationFactory();

        Specification specification = userSearchSpecificationFactory.getSpecification(userSearch);
        assertThat(specification).isInstanceOf(MultiFieldsMatchingSpecification.class);
    }
}