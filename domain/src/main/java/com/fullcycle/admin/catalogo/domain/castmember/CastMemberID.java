package com.fullcycle.admin.catalogo.domain.castmember;

import com.fullcycle.admin.catalogo.domain.Identifier;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;

import java.util.Objects;
import java.util.UUID;

public class CastMemberID extends Identifier {

    private final String value;

    private CastMemberID(final String anId){
        Objects.requireNonNull(anId);
        this.value = anId;
    }

    public static CastMemberID unique(){
        return new CastMemberID(UUID.randomUUID().toString().toLowerCase());
    }

    public static CastMemberID from(final String anId){
        return new CastMemberID(anId);
    }

    public static CastMemberID from(final UUID anId){
        return new CastMemberID(anId.toString().toLowerCase());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CastMemberID that)) return false;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }

    @Override
    public String getValue() {
        return value;
    }
}
