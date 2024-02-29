package com.fullcycle.admin.catalogo.domain.category;

import com.fullcycle.admin.catalogo.domain.validation.Error;
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler;
import com.fullcycle.admin.catalogo.domain.validation.Validator;

public class CategoryValidator extends Validator {

    public static final int NAME_MIN_LENGTH = 3;
    public static final int NAME_MAX_LENGTH = 255;
    private final Category category;

    public CategoryValidator(final Category aCategory, final ValidationHandler aHandler) {
        super(aHandler);
        this.category = aCategory;
    }

        @Override
        public void validate() {
            checkNameConstraints();
        }

    private void checkNameConstraints() {
       final var name = category.getName();

        if(name == null){
            validationHandler().append(new Error("'name' should not be null"));
            return;
        }

        if(name.isBlank()){
            validationHandler().append(new Error("'name' should not be empty"));
            return;
        }

        int length = name.trim().length();
        if(length < NAME_MIN_LENGTH){
            validationHandler().append(new Error("'name' must have at least 3 characters"));
            return;
        }

        if(length > NAME_MAX_LENGTH){
            validationHandler().append(new Error("'name' must have at most 255 characters"));
        }

    }
}
