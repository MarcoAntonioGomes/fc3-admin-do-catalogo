package com.fullcycle.admin.catalogo.application.video.create;

import com.fullcycle.admin.catalogo.domain.resource.Resource;

import java.util.Optional;
import java.util.Set;

public record CreateVideoCommand(
        String title,
        String description,
        Integer launchedAt,
        Double duration,
        boolean opened,
        boolean published,
        String rating,

        Set<String> categories,
        Set<String> genres,
        Set<String> members,
        Resource video,
        Resource trailer,
        Resource banner,
        Resource thumbnail,
        Resource thumbnailHalf


) {

    public static CreateVideoCommand with(
            final String title,
            final String description,
            final Integer launchedAt,
            final Double duration,
            final Boolean opened,
            final Boolean published,
            final String rating,
            final Set<String> categories,
            final Set<String> genres,
            final Set<String> members,
            final Resource video,
            final Resource trailer,
            final Resource banner,
            final Resource thumbnail,
            final Resource thumbnailHalf
    ) {
        return new CreateVideoCommand(
                title,
                description,
                launchedAt,
                duration,
                opened == null ? false : opened,
                published == null ? false : published,
                rating,
                categories,
                genres,
                members,
                video,
                trailer,
                banner,
                thumbnail,
                thumbnailHalf
        );
    }

    public static CreateVideoCommand with(
            final String title,
            final String description,
            final Integer launchedAt,
            final Double duration,
            final Boolean opened,
            final Boolean published,
            final String rating,
            final Set<String> categories,
            final Set<String> genres,
            final Set<String> members
    ) {
        return new CreateVideoCommand(
                title,
                description,
                launchedAt,
                duration,
                opened == null ? false : opened,
                published == null ? false : published,
                rating,
                categories,
                genres,
                members,
                null,
                null,
                null,
                null,
                null
        );
    }

    public Optional<Resource> getVideo() {
        return Optional.ofNullable(video);
    }

    public Optional<Resource> getTrailer() {
        return Optional.ofNullable(trailer);
    }

    public Optional<Resource> getBanner() {
        return Optional.ofNullable(banner);
    }

    public Optional<Resource> getThumbnail() {
        return Optional.ofNullable(thumbnail);
    }

    public Optional<Resource> getThumbnailHalf() {
        return Optional.ofNullable(thumbnailHalf);
    }
}
