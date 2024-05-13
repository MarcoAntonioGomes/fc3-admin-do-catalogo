package com.fullcycle.admin.catalogo.domain.video;

import com.fullcycle.admin.catalogo.domain.Identifier;
import com.fullcycle.admin.catalogo.domain.utils.IdUtils;


import java.util.Objects;
import java.util.UUID;

public class VideoID extends Identifier {

    private final String value;

    private  VideoID(final String value) {
        Objects.requireNonNull(value, "Video ID is required");
        this.value = value;
    }

    public static VideoID unique(){
        return new VideoID(IdUtils.uuid());
    }

    public static VideoID from(final String anId) {
        return new VideoID(anId.toLowerCase());
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VideoID that)) return false;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
