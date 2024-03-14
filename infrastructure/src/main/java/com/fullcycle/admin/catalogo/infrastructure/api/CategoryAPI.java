package com.fullcycle.admin.catalogo.infrastructure.api;


import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.infrastructure.category.models.CategoryListResponse;
import com.fullcycle.admin.catalogo.infrastructure.category.models.CategoryResponse;
import com.fullcycle.admin.catalogo.infrastructure.category.models.CreateCategoryRequest;
import com.fullcycle.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping(value = "categories")
@Tag(name = "Categories")
public interface CategoryAPI {



    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Category created"),
        @ApiResponse(responseCode = "422", description = "Unprocessable error"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })

    ResponseEntity<?>  createCategory(@RequestBody @Valid CreateCategoryRequest input);


    @GetMapping
    @Operation(summary = "List all categories paginated")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categories listed successfully"),
        @ApiResponse(responseCode = "422", description = "A invalid parameter was received"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    Pagination<CategoryListResponse>  listCategories(
            @RequestParam(name =  "search", required = false, defaultValue = "") String search,
            @RequestParam(name =  "page", required = false, defaultValue = "0") int page,
            @RequestParam(name =  "perPage", required = false, defaultValue = "10") int perPage,
            @RequestParam(name =  "sort", required = false, defaultValue = "name") String sort,
            @RequestParam(name =  "dir", required = false, defaultValue = "asc") String direction
    );

    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get a category by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category found successfully"),
            @ApiResponse(responseCode = "422", description = "Category was not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    CategoryResponse getById(@PathVariable(name = "id") String id);



    @PutMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "update a category by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update Category successfully"),
            @ApiResponse(responseCode = "422", description = "Category was not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    ResponseEntity<?>  updateById(@PathVariable(name = "id") String id, @RequestBody @Valid UpdateCategoryRequest input);


    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "delete a category by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category successfully deleted"),
            @ApiResponse(responseCode = "422", description = "Category was not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
     void deleteById(@PathVariable(name = "id") String id);




}
