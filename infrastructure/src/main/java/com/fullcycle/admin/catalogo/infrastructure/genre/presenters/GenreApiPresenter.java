package com.fullcycle.admin.catalogo.infrastructure.genre.presenters;

import com.fullcycle.admin.catalogo.application.genre.retrieve.get.GenreOutput;
import com.fullcycle.admin.catalogo.application.genre.retrieve.list.GenreListOutput;
import com.fullcycle.admin.catalogo.infrastructure.genre.models.GenreListResponse;
import com.fullcycle.admin.catalogo.infrastructure.genre.models.GenreResponse;

import java.util.function.Function;

public interface GenreApiPresenter {

    Function<GenreOutput, GenreResponse> present = output -> new GenreResponse(
            output.id(),
            output.name(),
            output.categories(),
            output.isActive(),
            output.createdAt(),
            output.updatedAt(),
            output.deletedAt()
    );

    static GenreResponse present(final GenreOutput output){
        return new GenreResponse(
                output.id(),
                output.name(),
                output.categories(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }

    static GenreListResponse present(final GenreListOutput output){
        return new GenreListResponse(
                output.id(),
                output.name(),
                output.isActive(),
                output.createdAt(),
                output.deletedAt()
        );
    }
}
