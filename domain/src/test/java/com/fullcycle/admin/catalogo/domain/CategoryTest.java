package com.fullcycle.admin.catalogo.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CategoryTest {

    @Test
    public void testNewCategory() {
        Category category = new Category();
        Assertions.assertNotNull(category);
    }
}
