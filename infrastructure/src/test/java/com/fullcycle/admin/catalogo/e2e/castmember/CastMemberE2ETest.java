package com.fullcycle.admin.catalogo.e2e.castmember;

import com.fullcycle.admin.catalogo.E2ETEST;
import com.fullcycle.admin.catalogo.Fixture;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberType;
import com.fullcycle.admin.catalogo.e2e.MockDsl;
import com.fullcycle.admin.catalogo.infrastructure.castmember.persistence.CastMemberRepository;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static java.lang.Thread.sleep;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@E2ETEST
@Testcontainers
public class CastMemberE2ETest implements MockDsl {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @Container
    private static final MySQLContainer MYSQL_CONTAINER =
            new MySQLContainer("mysql:8.2.0")
                    .withPassword("123456")
                    .withUsername("root")
                    .withDatabaseName("adm_videos");

    @Override
    public MockMvc mvc() {
        return this.mvc;
    }


    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        final var mappedPort = MYSQL_CONTAINER.getMappedPort(3306);
        System.out.printf("Container is running on port %s \n", mappedPort);
        registry.add("mysql.port", () -> mappedPort);

    }

    @Test
    public void asCatalogAdminIShouldBeAbleToCreateACastMemberWithValidValues() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());

        Assertions.assertEquals(0, castMemberRepository.count());

        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var actualMemberId = givenACastMember(expectedName, expectedType);

       final var actualMember = castMemberRepository.findById(actualMemberId.getValue()).get();

       Assertions.assertEquals(expectedName, actualMember.getName());
       Assertions.assertEquals(expectedType, actualMember.getType());
       Assertions.assertNotNull(actualMember.getCreatedAt());
       Assertions.assertNotNull(actualMember.getUpdatedAt());
       Assertions.assertEquals(actualMember.getCreatedAt(), actualMember.getUpdatedAt());


    }


    @Test
    public void asCatalogAdminIShouldBeAbleToSeeATreatedErrorByCreatingACastMemberWithInvalidValues() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());

        final String expectedName = null;
        final var expectedType = Fixture.CastMembers.type();

        final var expectedErrorMessage = "'name' should not be null";

        givenACastMemberResult(expectedName, expectedType)
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));


    }

    @Test
    public void asCatalogAdminIShouldBeAbleToNavigateThruAllMembers() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());

        givenACastMember("Vin Diesel", CastMemberType.ACTOR);
        sleep(100);
        givenACastMember("Quentin Tarantino", CastMemberType.DIRECTOR);
        sleep(100);
        givenACastMember("Jason Momoa", CastMemberType.ACTOR);

        listCastMembers(0, 1)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", equalTo(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", equalTo("Jason Momoa")));

        listCastMembers(1, 1)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", equalTo("Quentin Tarantino")));

        listCastMembers(2, 1)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", equalTo(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", equalTo("Vin Diesel")));

        listCastMembers(3, 1)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", hasSize(0)));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSearchThruAllMembers() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());

        givenACastMember("Vin Diesel", CastMemberType.ACTOR);
        givenACastMember("Quentin Tarantino", CastMemberType.DIRECTOR);
        givenACastMember("Jason Momoa", CastMemberType.ACTOR);

        listCastMembers(0, 1, "vin")
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", equalTo(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", equalTo("Vin Diesel")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSortAllMembersByNameDesc() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());

        givenACastMember("Vin Diesel", CastMemberType.ACTOR);
        givenACastMember("Quentin Tarantino", CastMemberType.DIRECTOR);
        givenACastMember("Jason Momoa", CastMemberType.ACTOR);

        listCastMembers(0, 3, "", "name", "desc")
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", equalTo(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", equalTo("Vin Diesel")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[1].name", equalTo("Quentin Tarantino")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[2].name", equalTo("Jason Momoa")));
    }


    @Test
    public void asACatalogAdminIShouldBeAbleToGetACastMemberByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());

        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        givenACastMember(Fixture.name(), Fixture.CastMembers.type());
        givenACastMember(Fixture.name(), Fixture.CastMembers.type());
        final var actualId = givenACastMember(expectedName, expectedType);

        final var actualMember = retrieveACastMember(actualId);

        Assertions.assertEquals(expectedName, actualMember.name());
        Assertions.assertEquals(expectedType.name(), actualMember.type());
        Assertions.assertNotNull(actualMember.createdAt());
        Assertions.assertNotNull(actualMember.updatedAt());
        Assertions.assertEquals(actualMember.createdAt(), actualMember.updatedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSeeATreatedErrorByGettingANotFoundCastMember() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());

        givenACastMember(Fixture.name(), Fixture.CastMembers.type());
        givenACastMember(Fixture.name(), Fixture.CastMembers.type());

        retrieveACastMemberResult(CastMemberID.from("123"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("CastMember with ID 123 was not found")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToUpdateACastMemberByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());

        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;

        givenACastMember(Fixture.name(), Fixture.CastMembers.type());
        final var actualId = givenACastMember("vin d", CastMemberType.DIRECTOR);

        updateACastMember(actualId, expectedName, expectedType)
                .andExpect(status().isOk());

        final var actualMember = retrieveACastMember(actualId);

        Assertions.assertEquals(expectedName, actualMember.name());
        Assertions.assertEquals(expectedType.name(), actualMember.type());
        Assertions.assertNotNull(actualMember.createdAt());
        Assertions.assertNotNull(actualMember.updatedAt());
        Assertions.assertNotEquals(actualMember.createdAt(), actualMember.updatedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSeeATreatedErrorByUpdatingACastMemberWithInvalidValue() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());

        final var expectedName = "";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorMessage = "'name' should not be empty";

        givenACastMember(Fixture.name(), Fixture.CastMembers.type());
        final var actualId = givenACastMember("vin d", CastMemberType.DIRECTOR);

        updateACastMember(actualId, expectedName, expectedType)
                .andExpect(status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToDeleteACastMemberByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());

        givenACastMember(Fixture.name(), Fixture.CastMembers.type());
        final var actualId = givenACastMember(Fixture.name(), Fixture.CastMembers.type());

        Assertions.assertEquals(2, castMemberRepository.count());

        deleteACastMember(actualId)
                .andExpect(status().isNoContent());

        Assertions.assertEquals(1, castMemberRepository.count());
        Assertions.assertFalse(castMemberRepository.existsById(actualId.getValue()));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToDeleteACastMemberWithInvalidIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());

        givenACastMember(Fixture.name(), Fixture.CastMembers.type());
        givenACastMember(Fixture.name(), Fixture.CastMembers.type());

        Assertions.assertEquals(2, castMemberRepository.count());

        deleteACastMember(CastMemberID.from("123"))
                .andExpect(status().isNoContent());

        Assertions.assertEquals(2, castMemberRepository.count());
    }

}
