package com.fullcycle.admin.catalogo.application;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UseCaseTest {


    @Test
    public void testExecute() {
        UseCase useCase = new UseCase();
        Assertions.assertNotNull(useCase);
        Assertions.assertNotNull(useCase.execute());
    }
}
