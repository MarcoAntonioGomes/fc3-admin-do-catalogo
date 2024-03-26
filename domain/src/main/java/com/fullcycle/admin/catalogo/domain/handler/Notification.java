package com.fullcycle.admin.catalogo.domain.handler;

import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import com.fullcycle.admin.catalogo.domain.validation.Error;
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler;

import java.util.ArrayList;
import java.util.List;

public class Notification implements ValidationHandler {

    private final List<Error> errors;

    private Notification(final List<Error> anErrors) {
        this.errors = anErrors;
    }

    public static Notification create() {
        return new Notification(new ArrayList<>());
    }

    public static Notification create(final Error anError) {
        return new Notification(new ArrayList<>()).append(anError);
    }

    public static Notification create(final Throwable t){
       return create(new Error(t.getMessage()));
    }

    @Override
    public Notification append(final Error anError) {
        this.errors.add(anError);
        return this;
    }

    @Override
    public Notification append(ValidationHandler anHandler) {
        this.errors.addAll(anHandler.getErrors());
        return this;
    }



    @Override
    public <T> T validate(final Validation<T> aValidation) {
       try{
          return aValidation.validate();
       } catch (final DomainException ex) {
           this.errors.addAll(ex.getErrors());
       }catch (final Throwable t){
           this.errors.add(new Error(t.getMessage()));
       }
       return null;
    }

    @Override
    public List<Error> getErrors() {
        return this.errors;
    }
}
