package com.fullcycle.admin.catalogo.application.video.retrieve.list;

import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.genre.GenreID;
import com.fullcycle.admin.catalogo.domain.video.Video;
import com.fullcycle.admin.catalogo.domain.video.VideoPreview;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public record VideoListOutput(
        String id,
        String title,
        String description,
        List<String> categories,
        List<String> genres,

        List<String> castMembers,
        Instant createdAt,
        Instant updatedAt
) {



    public static VideoListOutput from(final VideoPreview aVideo ) {
        return new VideoListOutput(
                aVideo.id(),
                aVideo.title(),
                aVideo.description(),
                null,
                null,
                null,
                aVideo.createdAt(),
                aVideo.updatedAt()
        );
    }
    public static VideoListOutput with(  final String id,
                                         final String title,
                                         final String description,
                                         final List<String> categories,
                                         final List<String> genres,
                                         final List<String> castMembers,
                                         final  Instant createdAt,
                                         final  Instant updatedAt){
        return new VideoListOutput(
                id,
                title,
                description,
                categories,
                genres,
                castMembers,
                createdAt,
                updatedAt
        );

    }

}
