package com.fullcycle.admin.catalogo.application.category.create;


import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

@ExtendWith(MockitoExtension.class)
public class CreateCategoryUseCaseTest {

    @InjectMocks
    private DefaultCreateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidCommand_whenCallCreateCategory_shouldReturnCategoryId() {
        final var expectedName = "Filmes";
        final var expectedDescription = "Categoria de filmes";
        final var expectedIsActive = true;

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);



        Mockito.when(categoryGateway.create(Mockito.any())).thenAnswer(returnsFirstArg());


        final var actualOutput = useCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway, Mockito.times(1))
                .create(Mockito.argThat(aCategory -> {
                            return Objects.equals(aCategory.getName(), expectedName)
                                    && Objects.equals(aCategory.getDescription(), expectedDescription)
                                    && Objects.equals(aCategory.isActive(), expectedIsActive)
                                    && Objects.nonNull(aCategory.getCreatedAt())
                                    && Objects.nonNull(aCategory.getUpdatedAt())
                                    && Objects.isNull(aCategory.getDeletedAt())
                                    && Objects.nonNull(aCategory.getId());
                        }

                ));
    }

    @Test
    public void givenAInvalidName_whenCallCreateCategory_thenShouldReturnDomainException() {
        final String expectedName = null;
        final var expectedDescription = "Categoria de filmes";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);


       final var actualException = Assertions.assertThrows(DomainException.class, () -> {
            useCase.execute(aCommand);
        });


       Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
       Mockito.verify(categoryGateway, Mockito.never()).create(Mockito.any());

    }

    @Test
    public void givenAValidCommandWithInactiveCategory_whenCallCreateCategory_thenShouldReturnInactiveCatedoryId() {
        final String expectedName = "Filmes";
        final var expectedDescription = "Categoria de filmes";
        final var expectedIsActive = false;


        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);


        Mockito.when(categoryGateway.create(Mockito.any())).thenAnswer(returnsFirstArg());


        final var actualOutput = useCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway, Mockito.times(1))
                .create(Mockito.argThat(aCategory -> {
                            return Objects.equals(aCategory.getName(), expectedName)
                                    && Objects.equals(aCategory.getDescription(), expectedDescription)
                                    && Objects.equals(aCategory.isActive(), expectedIsActive)
                                    && Objects.nonNull(aCategory.getCreatedAt())
                                    && Objects.nonNull(aCategory.getUpdatedAt())
                                    && Objects.nonNull(aCategory.getDeletedAt())
                                    && Objects.nonNull(aCategory.getId());
                        }

                ));

    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException() {
        final var expectedName = "Filmes";
        final var expectedDescription = "Categoria de filmes";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Gateway error";
        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);



        Mockito.when(categoryGateway.create(Mockito.any())).thenThrow(new IllegalStateException(expectedErrorMessage));


        final var actualException = Assertions.assertThrows(IllegalStateException.class, () -> {
            useCase.execute(aCommand);
        });



        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(categoryGateway, Mockito.times(1))
                .create(Mockito.argThat(aCategory -> {
                            return Objects.equals(aCategory.getName(), expectedName)
                                    && Objects.equals(aCategory.getDescription(), expectedDescription)
                                    && Objects.equals(aCategory.isActive(), expectedIsActive)
                                    && Objects.nonNull(aCategory.getCreatedAt())
                                    && Objects.nonNull(aCategory.getUpdatedAt())
                                    && Objects.isNull(aCategory.getDeletedAt())
                                    && Objects.nonNull(aCategory.getId());
                        }

                ));
    }

}
