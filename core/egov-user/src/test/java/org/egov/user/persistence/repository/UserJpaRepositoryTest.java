package org.egov.user.persistence.repository;


import org.egov.user.domain.model.UserSearch;
import org.egov.user.persistence.entity.User;
import org.egov.user.persistence.enums.BloodGroup;
import org.egov.user.persistence.enums.Gender;
import org.egov.user.persistence.enums.UserType;
import org.egov.user.persistence.specification.FuzzyNameMatchingSpecification;
import org.egov.user.persistence.specification.MultiFieldsMatchingSpecification;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@DataJpaTest
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
    public void test_should_return_id_for_user_with_given_user_name() {
        Long id = userJpaRepository.isUserPresent("bigcat399", 1L);
        assertEquals(Long.valueOf(1), id);;
    }

    @Test
    @Sql(scripts = {
            "/sql/clearUserRoles.sql",
            "/sql/clearAddresses.sql",
            "/sql/clearRoles.sql",
            "/sql/clearUsers.sql",
            "/sql/createUser.sql"
    })
    public void test_should_return_null_when_user_does_not_exist_for_given_user_name() {
        Long id = userJpaRepository.isUserPresent("unknown", 1L);
        assertNull(id);
    }

    @Test
    @Sql(scripts = {
            "/sql/clearUserRoles.sql",
            "/sql/clearAddresses.sql",
            "/sql/clearRoles.sql",
            "/sql/clearUsers.sql",
            "/sql/createUser.sql"
    })
    public void should_fetch_user_by_name() {
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
    public void should_fetch_user_by_email() {
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
    public void fuzzy_name_matching_query_test() {
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
    public void multi_field_matching_query_test() {
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
    public void multi_field_matching_query_user_id_matching_test() {
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
    public void multi_field_matching_negative_test() {
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
    public void multi_field_matching_empty_request_test() {
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
    public void multi_field_matching_user_type_test() {
        UserSearch userSearch = UserSearch.builder().active(true).type("EMPLOYEE").build();
        MultiFieldsMatchingSpecification multiFieldsMatchingSpecification =
                new MultiFieldsMatchingSpecification(userSearch);

        List<User> userList = userJpaRepository.findAll(multiFieldsMatchingSpecification);

        assertThat(userList).hasSize(2);
    }
}
