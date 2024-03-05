package com.fullcycle.admin.catalogo.application.category.update;

import com.fullcycle.admin.catalogo.application.category.create.CreateCategoryCommand;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import com.fullcycle.admin.catalogo.domain.handler.Notification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseTest {

    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void clean() {
        Mockito.reset(categoryGateway);
    }

    @Test
    public void givenAValidCommand_whenCallsUpdatedCategory_shouldReturnCategoryId() {
        final var aCategory = Category.newCategory("Film", null, true);
        final var expectedName = "Filmes";
        final var expectedDescription = "Categoria de filmes";
        final var expectedIsActive = true;
        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(aCategory.clone()));

        when(categoryGateway.update(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        verify(categoryGateway, times(1)).findById(eq(expectedId));

        verify(categoryGateway, times(1))
                .update(argThat(aCategoryUpdated -> {
                            return Objects.equals(aCategoryUpdated.getName(), expectedName)
                                    && Objects.equals(aCategoryUpdated.getDescription(), expectedDescription)
                                    && Objects.equals(aCategoryUpdated.isActive(), expectedIsActive)
                                    && Objects.equals(aCategoryUpdated.getId(), expectedId)
                                    && Objects.equals(aCategoryUpdated.getCreatedAt(), aCategory.getCreatedAt())
                                    && aCategory.getUpdatedAt().isBefore(aCategoryUpdated.getUpdatedAt())
                                    && Objects.isNull(aCategoryUpdated.getDeletedAt());
                        }

                ));
    }

    @Test
    public void givenAInvalidName_whenCallUpdateCategory_thenShouldReturnDomainException() {
        final var aCategory = Category.newCategory("Film", null, true);
        final String expectedName = null;
        final var expectedDescription = "Categoria de filmes";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;
        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(aCategory.clone()));


        Notification notification = useCase.execute(aCommand).getLeft();
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());


        Mockito.verify(categoryGateway, Mockito.never()).update(Mockito.any());

    }

    @Test
    public void givenAValidInactivateCommand_whenCallUpdateCategory_thenShouldReturnInactiveCatedoryId() {
        final var aCategory = Category.newCategory("Film", null, true);

        Assertions.assertTrue(aCategory.isActive());
        Assertions.assertNull(aCategory.getDeletedAt());

        final var expectedName = "Filmes";
        final var expectedDescription = "Categoria de filmes";
        final var expectedIsActive = false;
        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );


        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(aCategory.clone()));

        when(categoryGateway.update(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        verify(categoryGateway, times(1)).findById(eq(expectedId));

        verify(categoryGateway, times(1))
                .update(argThat(aCategoryUpdated -> {
                            return Objects.equals(aCategoryUpdated.getName(), expectedName)
                                    && Objects.equals(aCategoryUpdated.getDescription(), expectedDescription)
                                    && Objects.equals(aCategoryUpdated.isActive(), expectedIsActive)
                                    && Objects.equals(aCategoryUpdated.getId(), expectedId)
                                    && Objects.equals(aCategoryUpdated.getCreatedAt(), aCategory.getCreatedAt())
                                    && aCategory.getUpdatedAt().isBefore(aCategoryUpdated.getUpdatedAt())
                                    && Objects.nonNull(aCategoryUpdated.getDeletedAt());
                        }

                ));
    }

    @Test
    public void givenACommandWithInvalidId_whenCallUpdateCategory_thenShouldReturnNotFoundException() {
        final var aCategory = Category.newCategory("Film", null, true);

        Assertions.assertTrue(aCategory.isActive());
        Assertions.assertNull(aCategory.getDeletedAt());

        final var expectedName = "Filmes";
        final var expectedDescription = "Categoria de filmes";
        final var expectedIsActive = false;
        final var expectedId = "123";
        final var expectedErrorMessage = "Category with ID 123 was not found";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCategoryCommand.with(
                expectedId,
                expectedName,
                expectedDescription,
                expectedIsActive
        );


        when(categoryGateway.findById(eq(CategoryID.from(expectedId)))).thenReturn(Optional.empty());


        final var actualException = Assertions.assertThrows(DomainException.class, () -> useCase.execute(aCommand));


        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        verify(categoryGateway, times(1)).findById(CategoryID.from(expectedId));

        verify(categoryGateway, times(0)).update(any());
    }

}
