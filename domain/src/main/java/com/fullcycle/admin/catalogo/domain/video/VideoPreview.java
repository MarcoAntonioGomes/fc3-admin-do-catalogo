package com.fullcycle.admin.catalogo.domain.video;

import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.genre.GenreID;

import java.time.Instant;
import java.util.Set;

public record VideoPreview(
        String id,
        String title,
        String description,

        Instant createdAt,
        Instant updatedAt
) {
    public VideoPreview(final Video aVideo) {
        this(
                aVideo.getId().getValue(),
                aVideo.getTitle(),
                aVideo.getDescription(),
                aVideo.getCreatedAt(),
                aVideo.getUpdatedAt()
        );
    }

}
