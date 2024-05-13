package com.fullcycle.admin.catalogo.domain.category;

import com.fullcycle.admin.catalogo.domain.Identifier;
import com.fullcycle.admin.catalogo.domain.utils.IdUtils;

import java.util.Objects;
import java.util.UUID;

public class CategoryID extends Identifier {

   private final String value;

    private CategoryID(final String value) {
         Objects.requireNonNull(value, "Category ID is required");
         this.value = value;
    }

    public static CategoryID unique(){
        return new CategoryID(IdUtils.uuid());
    }

    public static CategoryID from(final String anId){
        return new CategoryID(anId);
    }


    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryID that)) return false;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
