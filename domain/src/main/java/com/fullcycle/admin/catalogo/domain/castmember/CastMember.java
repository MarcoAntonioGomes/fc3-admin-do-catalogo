package com.fullcycle.admin.catalogo.domain.castmember;

import com.fullcycle.admin.catalogo.domain.AggregateRoot;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import com.fullcycle.admin.catalogo.domain.handler.Notification;
import com.fullcycle.admin.catalogo.domain.utils.InstantUtils;
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler;

import java.time.Instant;

public class CastMember extends AggregateRoot<CastMemberID> {

    private String name;
    private CastMemberType type;
    private Instant createdAt;
    private Instant updatedAt;


    protected CastMember(final CastMemberID anId, final String aName, final CastMemberType aType, final Instant aCreationDate, final Instant aUpdateDate) {
        super(anId);
        this.name = aName;
        this.type = aType;
        this.createdAt = aCreationDate;
        this.updatedAt = aUpdateDate;
        selfValidate();

    }

    private void selfValidate() {
        final var notification = Notification.create();
        validate(notification);

        if (notification.hasErrors()) {
            throw new NotificationException("Failed to create a Aggregate CastMember", notification);
        }

    }

    public CastMember update(final String aName, final CastMemberType aType) {
        this.name = aName;
        this.type = aType;
        this.updatedAt = InstantUtils.now();
        selfValidate();
        return this;
    }

    public static CastMember newMember(final String aName, final CastMemberType aType) {
        final var anId = CastMemberID.unique();
        final var now = InstantUtils.now();
        return new CastMember(anId, aName, aType, now, now);
    }


    public static CastMember with(
            final CastMemberID anId,
            final String aName,
            final CastMemberType aType,
            final Instant createdAt,
            final Instant updatedAt) {
        return new CastMember(anId, aName, aType, createdAt, updatedAt);
    }

    public static CastMember with(
            final CastMember aMember) {
        return new CastMember(aMember.getId(), aMember.getName(), aMember.getType(), aMember.getCreatedAt(), aMember.getUpdatedAt());
    }


    @Override
    public void validate(ValidationHandler handler){
        new CastMemberValidator(this, handler).validate();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CastMemberType getType() {
        return type;
    }

    public void setType(CastMemberType type) {
        this.type = type;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
