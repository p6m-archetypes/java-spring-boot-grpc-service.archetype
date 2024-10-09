package {{root_package}}.persistence.entities;

import {{ group_id }}.platform.persistence.jpa.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name = "{{ entity_name }}")
public class {{ EntityName }}Entity extends AbstractEntity {

    @Column(name = "name")
    private String name;

    // Hibernate requires a default constructor
    public {{ EntityName }}Entity() {}

    public {{ EntityName }}Entity(@NotNull String name) {
        this.name = name;
    }

    public {{ EntityName }}Entity(@NotNull UUID id, @NotNull String name) {
        setId(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "{{ EntityName }}Entity{" +
            "id='" + getId() + '\'' +
            "name='" + name + '\'' +
            '}';
    }
}