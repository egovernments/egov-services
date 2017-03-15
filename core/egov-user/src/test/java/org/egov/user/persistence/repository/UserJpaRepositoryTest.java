package org.egov.user.persistence.repository;


import org.egov.user.domain.model.UserSearch;
import org.egov.user.persistence.entity.User;
import org.egov.user.persistence.entity.enums.BloodGroup;
import org.egov.user.persistence.entity.enums.Gender;
import org.egov.user.persistence.entity.enums.UserType;
import org.egov.user.persistence.specification.FuzzyNameMatchingSpecification;
import org.egov.user.persistence.specification.MultiFieldsMatchingSpecification;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class UserJpaRepositoryTest {

    @Autowired
    UserJpaRepository userJpaRepository;

    @Test
    @Sql(scripts = {
            "/sql/clearUserRoles.sql",
            "/sql/clearAddresses.sql",
            "/sql/clearRoles.sql",
            "/sql/clearUsers.sql",
            "/sql/createUser.sql"
    })
    public void shouldFetchUserByName() {
        User user = userJpaRepository.findByUsername("greenfish424");
        assertThat(user.getId()).isEqualTo(2L);
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
        User user = userJpaRepository.findByEmailId("email3@gmail.com");
        assertThat(user.getId()).isEqualTo(3L);
    }

    @Test
    @Sql(scripts = {
            "/sql/clearUserRoles.sql",
            "/sql/clearAddresses.sql",
            "/sql/clearRoles.sql",
            "/sql/clearUsers.sql",
            "/sql/createUser.sql"
    })
    public void fuzzyNameMatchingQueryTest() {
        UserSearch userSearch = UserSearch.builder()
                .id(asList(1L, 2L))
                .name("Ram")
                .fuzzyLogic(true)
                .active(true)
                .build();
        FuzzyNameMatchingSpecification fuzzyNameMatchingSpecification = new FuzzyNameMatchingSpecification(userSearch);
        List<User> userList = userJpaRepository.findAll(fuzzyNameMatchingSpecification);

        assertThat(userList.get(0).getId()).isEqualTo(3);
        assertThat(userList.get(1).getId()).isEqualTo(4);
        assertThat(userList.get(2).getId()).isEqualTo(5);
    }

    @Test
    @Sql(scripts = {
            "/sql/clearUserRoles.sql",
            "/sql/clearAddresses.sql",
            "/sql/clearRoles.sql",
            "/sql/clearUsers.sql",
            "/sql/createUser.sql"
    })
    public void multiFieldMatchingQueryTest() {
        UserSearch userSearch = UserSearch.builder()
                .name("Sreerama Krishnan")
                .mobileNumber("9731123456")
                .emailId("email5@gmail.com")
                .pan("ABCDE1234F")
                .aadhaarNumber("12346789011")
                .active(true)
                .build();
        MultiFieldsMatchingSpecification multiFieldsMatchingSpecification =
                new MultiFieldsMatchingSpecification(userSearch);

        List<User> userList = userJpaRepository.findAll(multiFieldsMatchingSpecification);

        assertThat(userList.size()).isEqualTo(1);
        assertThat(userList.get(0).getId()).isEqualTo(5);
    }

    @Test
    @Sql(scripts = {
            "/sql/clearUserRoles.sql",
            "/sql/clearAddresses.sql",
            "/sql/clearRoles.sql",
            "/sql/clearUsers.sql",
            "/sql/createUser.sql"
    })
    public void multiFieldMatchingQueryUserIdMatchingTest() {
        UserSearch userSearch = UserSearch.builder()
                .id(asList(1L, 2L)).active(true).build();

        MultiFieldsMatchingSpecification multiFieldsMatchingSpecification =
                new MultiFieldsMatchingSpecification(userSearch);

        List<User> users = userJpaRepository.findAll(multiFieldsMatchingSpecification);

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
    public void multiFieldMatchingNegativeTest() {
        UserSearch userSearch = UserSearch.builder()
                .name("Sreerama Krishnan")
                .mobileNumber("9731123456")
                .emailId("email5@gmail.com")
                .pan("ABCDE1234F")
                .aadhaarNumber("notMatching")
                .active(true)
                .build();
        MultiFieldsMatchingSpecification multiFieldsMatchingSpecification =
                new MultiFieldsMatchingSpecification(userSearch);

        List<User> userList = userJpaRepository.findAll(multiFieldsMatchingSpecification);

        assertThat(userList).isEmpty();
    }

    @Test
    @Sql(scripts = {
            "/sql/clearUserRoles.sql",
            "/sql/clearAddresses.sql",
            "/sql/clearRoles.sql",
            "/sql/clearUsers.sql",
            "/sql/createUser.sql"
    })
    public void multiFieldMatchingEmptyRequestTest() {
        UserSearch userSearch = UserSearch.builder().active(true).build();
        MultiFieldsMatchingSpecification multiFieldsMatchingSpecification =
                new MultiFieldsMatchingSpecification(userSearch);

        List<User> userList = userJpaRepository.findAll(multiFieldsMatchingSpecification);

        assertThat(userList).hasSize(5);
    }

    @Test
    @Sql(scripts = {
            "/sql/clearUserRoles.sql",
            "/sql/clearAddresses.sql",
            "/sql/clearRoles.sql",
            "/sql/clearUsers.sql",
            "/sql/createUser.sql"
    })
    public void multiFieldMatchingUserTypeTest() {
        UserSearch userSearch = UserSearch.builder().active(true).type("EMPLOYEE").build();
        MultiFieldsMatchingSpecification multiFieldsMatchingSpecification =
                new MultiFieldsMatchingSpecification(userSearch);

        List<User> userList = userJpaRepository.findAll(multiFieldsMatchingSpecification);

        assertThat(userList).hasSize(2);
    }
}
