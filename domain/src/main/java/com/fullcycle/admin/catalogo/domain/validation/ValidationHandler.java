package com.fullcycle.admin.catalogo.domain.validation;

import java.util.List;

public interface ValidationHandler {

    ValidationHandler append(Error anError);
    ValidationHandler append(ValidationHandler anHandler);

    <T> T validate(Validation<T> aValidation);

    default boolean hasErrors() {
        return getErrors() != null && !getErrors().isEmpty();
    }

    List<Error> getErrors();

    default Error firstError() {
      if(getErrors() != null && !getErrors().isEmpty()){
        return getErrors().get(0);
      }
        return null;
    }

    public interface Validation<T> {
        T validate();
    }
}
