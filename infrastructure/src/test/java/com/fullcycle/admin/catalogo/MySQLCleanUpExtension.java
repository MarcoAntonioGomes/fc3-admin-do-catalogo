package com.fullcycle.admin.catalogo;

import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;

public class MySQLCleanUpExtension implements BeforeEachCallback {
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


