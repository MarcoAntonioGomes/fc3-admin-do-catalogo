package com.fullcycle.admin.catalogo.infrastructure.api;

import com.fullcycle.admin.catalogo.application.genre.retrieve.list.GenreListOutput;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.infrastructure.genre.models.CreateGenreRequest;
import com.fullcycle.admin.catalogo.infrastructure.genre.models.GenreListResponse;
import com.fullcycle.admin.catalogo.infrastructure.genre.models.GenreResponse;
import com.fullcycle.admin.catalogo.infrastructure.genre.models.UpdateGenreRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("genres")
@Tag(name = "Genre")
public interface GenreAPI {


    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new genre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Genry created"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    ResponseEntity<?> create(@RequestBody CreateGenreRequest input);


    @GetMapping
    @Operation(summary = "List all genres paginated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Genres listed successfully"),
            @ApiResponse(responseCode = "422", description = "A invalid parameter was received"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    Pagination<GenreListResponse> list(
           @RequestParam(name =  "search", required = false, defaultValue = "") String search,
           @RequestParam(name =  "page", required = false, defaultValue = "0") int page,
           @RequestParam(name =  "perPage", required = false, defaultValue = "10") int perPage,
           @RequestParam(name =  "sort", required = false, defaultValue = "name") String sort,
           @RequestParam(name =  "dir", required = false, defaultValue = "asc") String direction

    );

    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get a genre by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Genre found successfully"),
            @ApiResponse(responseCode = "422", description = "Genre was not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    GenreResponse getById(@PathVariable(name = "id") String id);

    @PutMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "update a genre by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update Genre successfully"),
            @ApiResponse(responseCode = "422", description = "Genre was not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })

    ResponseEntity<?> updateById(@PathVariable(name = "id") String id, @RequestBody UpdateGenreRequest input);

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "delete a genre by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Genre successfully deleted"),
            @ApiResponse(responseCode = "422", description = "Genre was not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    void deleteById(@PathVariable(name = "id") String id);
}
