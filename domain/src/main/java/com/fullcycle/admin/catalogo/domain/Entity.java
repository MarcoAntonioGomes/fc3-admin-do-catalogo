package com.fullcycle.admin.catalogo.domain;

import java.util.Objects;

public abstract class Entity<ID extends Identifier> {

    protected  final ID id;

    public Entity(final ID id) {
        Objects.requireNonNull(id, "Id cannot be null");
        this.id = id;
    }

    public ID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity<?> entity)) return false;
        return Objects.equals(getId(), entity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
