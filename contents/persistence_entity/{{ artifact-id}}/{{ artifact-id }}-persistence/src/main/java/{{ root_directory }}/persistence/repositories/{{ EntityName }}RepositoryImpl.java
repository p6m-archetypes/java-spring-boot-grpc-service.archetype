package {{root_package}}.persistence.repositories;

import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import {{root_package}}.persistence.entities.{{ EntityName }}Entity;
import {{root_package}}.persistence.entities.Q{{ EntityName }}Entity;

public class {{ EntityName }}RepositoryImpl implements {{ EntityName }}RepositoryExtensions {

    @Autowired
    {{ EntityName }}Repository {{ entityName }}Repository;

    @Override
    public Page<{{ EntityName }}Entity> findByNameQueryDsl(String name, Pageable pageable) {
        Q{{ EntityName }}Entity qm = Q{{ EntityName }}Entity.{{ entityName }}Entity;
        Predicate predicate = qm.name.eq(name);
        return {{ entityName }}Repository.findAll(predicate, pageable);
    }
}
