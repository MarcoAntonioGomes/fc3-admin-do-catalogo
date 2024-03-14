package com.fullcycle.admin.catalogo.infrastructure.category.persistence;

import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.MySQLGatewayTest;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;



@MySQLGatewayTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void givenAnInvalidNullName_whenCallsSave_shouldReturnError() {
       final var aCategory = Category.newCategory("Filmes", "Filmes de todos os tipos", true);
       final var expectedProperty = "name";
       final var expectedMessage = "not-null property references a null or transient value : com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJPAEntity.name";

       final var anEntity = CategoryJPAEntity.from(aCategory);
       anEntity.setName(null);

       final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(anEntity));
       final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

       Assertions.assertEquals( expectedProperty, actualCause.getPropertyName());
       Assertions.assertEquals(expectedMessage, actualException.getCause().getMessage());

    }

    @Test
    public void givenAnInvalidNullCreatedAt_whenCallsSave_shouldReturnError() {
        final var aCategory = Category.newCategory("Filmes", "Filmes de todos os tipos", true);
        final var expectedProperty = "createdAt";
        final var expectedMessage = "not-null property references a null or transient value : com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJPAEntity.createdAt";

        final var anEntity = CategoryJPAEntity.from(aCategory);
        anEntity.setCreatedAt(null);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(anEntity));
        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals( expectedProperty, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessage, actualException.getCause().getMessage());

    }

    @Test
    public void givenAnInvalidNullUpdatedAt_whenCallsSave_shouldReturnError() {
        final var aCategory = Category.newCategory("Filmes", "Filmes de todos os tipos", true);
        final var expectedProperty = "updatedAt";
        final var expectedMessage = "not-null property references a null or transient value : com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJPAEntity.updatedAt";

        final var anEntity = CategoryJPAEntity.from(aCategory);
        anEntity.setUpdatedAt(null);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(anEntity));
        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals( expectedProperty, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessage, actualException.getCause().getMessage());

    }


}
