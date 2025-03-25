package {{ root_package }}.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;

/**
 * Extending from this class will add a 'created' timestamp field that automatically sets the creation date on initial
 * insert of the entity.
 *
 */
@MappedSuperclass
@EntityListeners({DateFieldListener.class})
public class AbstractCreated implements Created {

    @Column(nullable = false, name = "created")
    private Instant createdDate;

    @Override
    public Instant getCreatedAt() {
        return createdDate;
    }

    @Override
    public void setCreatedAt(final Instant instant) {
        this.createdDate = instant;
    }

}
