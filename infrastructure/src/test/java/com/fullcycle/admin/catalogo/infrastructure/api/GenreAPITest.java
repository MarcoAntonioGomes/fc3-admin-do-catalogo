package com.fullcycle.admin.catalogo.infrastructure.api;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullcycle.admin.catalogo.ControllerTest;
import com.fullcycle.admin.catalogo.application.genre.create.CreateGenreOutput;
import com.fullcycle.admin.catalogo.application.genre.create.CreateGenreUseCase;

import com.fullcycle.admin.catalogo.application.genre.delete.DeleteGenreUseCase;
import com.fullcycle.admin.catalogo.application.genre.retrieve.get.GenreOutput;
import com.fullcycle.admin.catalogo.application.genre.retrieve.get.GetGenreByIdUseCase;
import com.fullcycle.admin.catalogo.application.genre.retrieve.list.GenreListOutput;
import com.fullcycle.admin.catalogo.application.genre.retrieve.list.ListGenreUseCase;
import com.fullcycle.admin.catalogo.application.genre.update.UpdateGenreOutput;
import com.fullcycle.admin.catalogo.application.genre.update.UpdateGenreUseCase;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import com.fullcycle.admin.catalogo.domain.genre.Genre;
import com.fullcycle.admin.catalogo.domain.genre.GenreID;
import com.fullcycle.admin.catalogo.domain.handler.Notification;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.validation.Error;
import com.fullcycle.admin.catalogo.infrastructure.genre.models.CreateGenreRequest;
import com.fullcycle.admin.catalogo.infrastructure.genre.models.UpdateGenreRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Objects;


import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = GenreAPI.class)
public class GenreAPITest {


    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateGenreUseCase createGenreUseCase;

    @MockBean
    private GetGenreByIdUseCase getGenreByIdUseCase;

    @MockBean
    private UpdateGenreUseCase updateGenreUseCase;

    @MockBean
    private DeleteGenreUseCase deleteGenreUseCase;

    @MockBean
    private ListGenreUseCase listGenreUseCase;

    @Test
    public void givenAValidCommand_whenCallsCreateGenre_shouldReturnGenreId() throws Exception {

        final var expectedName = "Ação";
        final var expectedCategories = List.of("1", "2");
        final var expectedActive = true;
        final var expectedId = "1";
        final var aInput = new CreateGenreRequest(expectedName, expectedCategories, expectedActive);

        when(createGenreUseCase.execute(any())).thenReturn(CreateGenreOutput.from(expectedId));

        final var aRequest = post("/genres", aInput)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));


        final var aResponse = this.mvc.perform(aRequest)
                .andDo(print());

        aResponse.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/genres/" + expectedId))
                .andExpect(header().string("Content-type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)));


        verify(createGenreUseCase).execute(argThat(cmd ->
                Objects.equals(cmd.name(), expectedName)
                        && Objects.equals(cmd.isActive(), expectedActive)
                        && Objects.equals(cmd.categories(), expectedCategories)


        ));

    }


    @Test
    public void givenAnInvalidName_whenCallsCreateGenre_shouldReturnNotification() throws Exception {
        final String expectedName = null;
        final var expectedCategories = List.of("1", "2");
        final var expectedActive = true;
        final var aInput = new CreateGenreRequest(expectedName, expectedCategories, expectedActive);
        final var expectedErrorMessage = "'name' should not be null";

        when(createGenreUseCase.execute(any())).thenThrow(new NotificationException("Error", Notification.create(new Error(expectedErrorMessage))));


        final var aRequest = post("/genres", aInput)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        final var aResponse = this.mvc.perform(aRequest)
                .andDo(print());

        aResponse.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(createGenreUseCase).execute(argThat(cmd ->
                Objects.equals(cmd.name(), expectedName)
                        && Objects.equals(cmd.isActive(), expectedActive)
                        && Objects.equals(cmd.categories(), expectedCategories)


        ));
    }

    @Test
    public void givenAValidId_whenCallsGetGenreById_shouldReturnGenre() throws Exception {

        final var expectedName = "Ação";
        final var expectedCategories = List.of("1", "2");
        final var expectedActive = false;


        final var aGenre = Genre.newGenre(expectedName,  expectedActive)
                .addCategories(expectedCategories.stream().map(CategoryID::from).toList());


        final var expectedId = aGenre.getId().getValue();

        when(getGenreByIdUseCase.execute(any())).thenReturn(GenreOutput.from(aGenre));

        final var aRequest = get("/genres/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var aResponse = this.mvc.perform(aRequest);

        aResponse.andExpect(status().isOk())
                .andExpect(header().string("Content-type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.name", equalTo(expectedName)))
                .andExpect(jsonPath("$.is_active", equalTo(expectedActive)))
                .andExpect(jsonPath("$.categories_id", equalTo(expectedCategories)))
                .andExpect(jsonPath("$.created_at", equalTo(aGenre.getCreatedAt().toString())))
                .andExpect(jsonPath("$.deleted_at", equalTo(aGenre.getDeletedAt().toString())))
                .andExpect(jsonPath("$.updated_at", equalTo(aGenre.getUpdatedAt().toString())));


        verify(getGenreByIdUseCase).execute(eq(expectedId));
    }

    @Test
    public void givenAnInvalidId_whenCallsGetGenreById_shouldReturnNotFound() throws Exception {


        final var expectedErrorMessage = "Genre with ID 123 was not found";
        final var expectedId = GenreID.from("123");


        when(getGenreByIdUseCase.execute(any())).thenThrow(NotFoundException.with(Genre.class, expectedId));

        final var aRequest = get("/genres/{id}", expectedId.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var aResponse = this.mvc.perform(aRequest);

        aResponse.andExpect(status().isNotFound())
                .andExpect(header().string("Content-type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(getGenreByIdUseCase).execute(eq(expectedId.getValue()));
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateGenre_shouldReturnGenreId() throws Exception {

        final var expectedName = "Ação";
        final var expectedCategories = List.of("1", "2");
        final var expectedActive = true;


        final var aGenre = Genre.newGenre(expectedName,  expectedActive);
        final var expectedId = aGenre.getId().getValue();


        final var aInput = new UpdateGenreRequest(expectedName, expectedCategories, expectedActive);


        when(updateGenreUseCase.execute(any())).thenReturn(UpdateGenreOutput.from(aGenre));

        final var aRequest = put("/genres/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));


        final var aResponse = this.mvc.perform(aRequest)
                .andDo(print());

        aResponse.andExpect(status().isOk())
                .andExpect(header().string("Content-type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)));


        verify(updateGenreUseCase).execute(argThat(cmd ->
                Objects.equals(cmd.name(), expectedName)
                        && Objects.equals(cmd.isActive(), expectedActive)
                        && Objects.equals(cmd.categories(), expectedCategories)


        ));

    }


    @Test
    public void givenAnInvalidName_whenCallsUpdateGenre_shouldReturnNotification() throws Exception {
        final String expectedName = null;
        final var expectedCategories = List.of("1", "2");
        final var expectedActive = true;
        final var expectedErrorMessage = "'name' should not be null";


        final var aGenre = Genre.newGenre("Ação",  expectedActive);
        final var expectedId = aGenre.getId().getValue();


        final var aInput = new UpdateGenreRequest(expectedName, expectedCategories, expectedActive);


        when(updateGenreUseCase.execute(any())).thenThrow(new NotificationException("Error", Notification.create(new Error(expectedErrorMessage))));


        final var aRequest = put("/genres/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        final var aResponse = this.mvc.perform(aRequest)
                .andDo(print());

        aResponse.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(updateGenreUseCase).execute(argThat(cmd ->
                Objects.equals(cmd.name(), expectedName)
                        && Objects.equals(cmd.isActive(), expectedActive)
                        && Objects.equals(cmd.categories(), expectedCategories)


        ));
    }

    @Test
    public void givenAValidId_whenCallsDeleteGenre_shouldBeOk() throws Exception {

        final var expectedId = "1";

        doNothing().when(deleteGenreUseCase).execute(any());

        final var aRequest = delete("/genres/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON);

        final var aResponse = this.mvc.perform(aRequest);

        aResponse.andExpect(status().isNoContent());

        verify(deleteGenreUseCase).execute(eq(expectedId));
    }


    @Test
    public void givenValidParams_whenCallsListGenres_shouldReturnGenres() throws Exception {

        final var aGenre = Genre.newGenre("Ação", false);


        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTerms = "ac";
        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(GenreListOutput.from(aGenre));

        when(listGenreUseCase.execute(any())).thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

       final var aRequest = get("/genres")
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms)
                .accept(MediaType.APPLICATION_JSON);

      final var response =  this.mvc.perform(aRequest);

      response.andExpect(status().isOk())
              .andExpect(header().string("Content-type", MediaType.APPLICATION_JSON_VALUE))
              .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
              .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
              .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
              .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
              .andExpect(jsonPath("$.items[0].id", equalTo(aGenre.getId().getValue())))
              .andExpect(jsonPath("$.items[0].name", equalTo(aGenre.getName())))
              .andExpect(jsonPath("$.items[0].is_active", equalTo(aGenre.isActive())))
              .andExpect(jsonPath("$.items[0].created_at", equalTo(aGenre.getCreatedAt().toString())))
              .andExpect(jsonPath("$.items[0].deleted_at", equalTo(aGenre.getDeletedAt().toString())));

        verify(listGenreUseCase).execute(argThat(cmd ->
                Objects.equals(cmd.page(), expectedPage)
                        && Objects.equals(cmd.perPage(), expectedPerPage)
                        && Objects.equals(cmd.sort(), expectedSort)
                        && Objects.equals(cmd.direction(), expectedDirection)
                        && Objects.equals(cmd.terms(), expectedTerms)
        ));

    }

}
