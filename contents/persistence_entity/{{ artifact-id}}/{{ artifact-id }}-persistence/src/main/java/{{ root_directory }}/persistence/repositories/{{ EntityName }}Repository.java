package {{root_package}}.persistence.repositories;

import {{root_package}}.persistence.entities.{{ EntityName }}Entity;
import {{root_package}}.persistence.repositories.{{ EntityName }}RepositoryExtensions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.UUID;

/**
 * @author Archetect
 */
@Repository
public interface {{ EntityName }}Repository extends
    JpaRepository<{{ EntityName }}Entity, UUID>,
    QuerydslPredicateExecutor<{{ EntityName }}Entity>,
    {{ EntityName }}RepositoryExtensions
{

}