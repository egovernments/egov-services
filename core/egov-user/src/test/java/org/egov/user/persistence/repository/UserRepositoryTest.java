package org.egov.user.persistence.repository;

import org.egov.user.domain.model.UserSearch;
import org.egov.user.persistence.entity.User;
import org.egov.user.persistence.specification.UserSearchSpecificationFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserRepositoryTest {

    @Mock
    UserJpaRepository userJpaRepository;

    @Mock
    UserSearchSpecificationFactory userSearchSpecificationFactory;

    private UserRepository userRepository;

    @Before
    public void setUp() {
        userRepository = new UserRepository(userJpaRepository, userSearchSpecificationFactory);
    }

    @Test
    public void test_get_user_by_userName() throws Exception {
        User expectedUser = mock(User.class);

        when(userJpaRepository.findByUsername("userName")).thenReturn(expectedUser);

        User actualUser = userRepository.findByUsername("userName");

        assertThat(expectedUser).isEqualTo(actualUser);
    }

    @Test
    public void test_get_user_by_emailId() throws Exception {
        User expectedUser = mock(User.class);

        when(userJpaRepository.findByEmailId("userName")).thenReturn(expectedUser);

        User actualUser = userRepository.findByEmailId("userName");

        assertThat(expectedUser).isEqualTo(actualUser);
    }

    @Test
    public void test_get_save_user() throws Exception {
        User expectedUser = mock(User.class);

        when(userJpaRepository.save(expectedUser)).thenReturn(expectedUser);

        User actualUser = userRepository.save(expectedUser);

        assertThat(expectedUser).isEqualTo(actualUser);
    }

    @Test
    public void test_search_user() throws Exception {
        List<User> expectedList = mock(List.class);
        UserSearch userSearch = mock(UserSearch.class);
        Specification<User> userSpecification = mock(Specification.class);

        when(userSearchSpecificationFactory.getSpecification(userSearch)).thenReturn(userSpecification);
        when(userJpaRepository.findAll(userSpecification)).thenReturn(expectedList);

        List<User> actualList = userRepository.findAll(userSearch);

        assertThat(expectedList).isEqualTo(actualList);
    }
}