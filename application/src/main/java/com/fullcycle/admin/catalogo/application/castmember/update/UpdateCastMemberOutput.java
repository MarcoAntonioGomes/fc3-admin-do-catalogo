package com.fullcycle.admin.catalogo.application.castmember.update;

import com.fullcycle.admin.catalogo.domain.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberType;

public record UpdateCastMemberOutput(
        String id

) {

    public static UpdateCastMemberOutput from(final CastMember aMember) {
        return from(aMember.getId());
    }

    public static UpdateCastMemberOutput from(CastMemberID anId) {
        return new UpdateCastMemberOutput(anId.getValue());
    }
}
