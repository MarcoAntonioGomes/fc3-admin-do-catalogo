package com.fullcycle.admin.catalogo.infrastructure.category.models;

import com.fullcycle.admin.catalogo.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

@JacksonTest
public class CreateCategoryRequestTest {

    @Autowired
    private JacksonTester<CreateCategoryRequest> json;

    @Test
    public void testUnMarshal() throws Exception {
        final var expectedName = "Filmes";
        final var expectedDescription = "Categoria de filmes";
        final var expectedIsActive = true;

        final var json = """
                {
                    "name": "%s",
                    "description":  "%s",
                    "is_active":  "%s"
                }
                """.formatted(expectedName, expectedDescription, expectedIsActive);

        final var actualJson = this.json.parse(json);

        Assertions.assertThat(actualJson)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("description", expectedDescription)
                .hasFieldOrPropertyWithValue("active", expectedIsActive);

    }

    @Test
    public void testMarshal() throws Exception {
        final var expectedName = "Filmes";
        final var expectedDescription = "Categoria de filmes";
        final var expectedIsActive = true;

        final var request = new CreateCategoryRequest(
                expectedName,
                expectedDescription,
                expectedIsActive
        );
        final var actualJson = this.json.write(request);

        Assertions.assertThat(actualJson)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.description", expectedDescription)
                .hasJsonPathValue("$.is_active", expectedIsActive);

    }

}
