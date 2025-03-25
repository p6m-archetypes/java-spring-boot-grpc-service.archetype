package {{ root_package }}.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

/**
 * Extending from this abstract MappedSuperclass will enable automatic optimistic locking.
 */
@MappedSuperclass
public class AbstractVersioned implements Versioned {

    @Version
    @Column(name = "version")
    private Short version;

    @Override
    public Short getVersion() {
        return version;
    }

    @Override
    public void setVersion(final Short version) {
        this.version = version;
    }
}
