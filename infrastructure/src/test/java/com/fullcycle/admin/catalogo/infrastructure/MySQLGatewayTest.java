package com.fullcycle.admin.catalogo.infrastructure;

import com.fullcycle.admin.catalogo.infrastructure.category.CategoryMySQLGatewayTest;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.*;
import java.util.Collection;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test")
@DataJpaTest
@ComponentScan(includeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*[MySQLGateway]")})
@ExtendWith({MySQLGatewayTest.cleanUpExtension.class})
public @interface MySQLGatewayTest {

    class cleanUpExtension implements BeforeEachCallback {
        @Override
        public void beforeEach(final ExtensionContext context)  {
            final var repositories = SpringExtension
                    .getApplicationContext(context)
                    .getBeansOfType(CategoryRepository.class)
                    .values();

            cleanUp(repositories);
        }

        private void cleanUp(final Collection<CategoryRepository> repositories) {
            repositories.forEach(CategoryRepository::deleteAll);
        }
    }

}
