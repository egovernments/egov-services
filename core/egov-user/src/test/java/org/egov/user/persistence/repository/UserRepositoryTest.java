package org.egov.user.persistence.repository;


import org.egov.user.persistence.entity.User;
import org.egov.user.persistence.entity.enums.BloodGroup;
import org.egov.user.persistence.entity.enums.Gender;
import org.egov.user.persistence.entity.enums.UserType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @Sql(scripts = {
            "/sql/clearUserRoles.sql",
            "/sql/clearAddresses.sql",
            "/sql/clearRoles.sql",
            "/sql/clearUsers.sql",
            "/sql/createUser.sql"
    })
    public void shouldFetchUsersById() {
        List<User> users = userRepository.findAll(Arrays.asList(1l, 2L));

        assertThat(users.get(0).getId()).isNotNull();
        assertThat(users.get(0).getGender()).isEqualTo(Gender.FEMALE);
        assertThat(users.get(0).getType()).isEqualTo(UserType.EMPLOYEE);
        assertThat(users.get(0).getBloodGroup()).isEqualTo(BloodGroup.A_POSITIVE);

        assertThat(users.get(1).getId()).isNotNull();
        assertThat(users.get(1).getGender()).isEqualTo(Gender.OTHERS);
        assertThat(users.get(1).getType()).isEqualTo(UserType.CITIZEN);
        assertThat(users.get(1).getBloodGroup()).isEqualTo(BloodGroup.AB_POSITIVE);
    }

    @Test
    @Sql(scripts = {
            "/sql/clearUserRoles.sql",
            "/sql/clearAddresses.sql",
            "/sql/clearRoles.sql",
            "/sql/clearUsers.sql",
            "/sql/createUser.sql"
    })
    public void shouldFetchUserByName() {
        User user = userRepository.findByUsername("userName1");
        assertThat(user.getId()).isEqualTo(1L);
    }

    @Test
    @Sql(scripts = {
            "/sql/clearUserRoles.sql",
            "/sql/clearAddresses.sql",
            "/sql/clearRoles.sql",
            "/sql/clearUsers.sql",
            "/sql/createUser.sql"
    })
    public void shouldFetchUserByEmail() {
        User user = userRepository.findByEmailId("email2@gmail.com");
        assertThat(user.getId()).isEqualTo(2L);
    }
}
