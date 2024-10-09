package {{root_package}}.persistence.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import {{root_package}}.persistence.entities.{{ EntityName }}Entity;

/**
 * Extensions to the {@link {{ EntityName }}Repository}.  This allows for concrete implementation in the
 * {@link {{ EntityName }}RepositoryImpl} using QueryDSL or with a native EntityManager.
 */
public interface {{ EntityName }}RepositoryExtensions {

    Page<{{ EntityName }}Entity> findByNameQueryDsl(String name, Pageable pageable);
}
