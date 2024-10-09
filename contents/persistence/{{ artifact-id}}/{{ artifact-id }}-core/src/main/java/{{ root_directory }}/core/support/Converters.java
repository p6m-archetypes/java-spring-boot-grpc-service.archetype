{% import "macros/java" as java %}
package {{ root_package }}.core.support;

import com.google.protobuf.StringValue;{% for entity_key in model.entities %}
import {{ root_package }}.grpc.v1.{{ entity_key | pascal_case }}Dto;
import {{ root_package }}.persistence.entities.{{ entity_key | pascal_case }}Entity;{% endfor %}

import java.util.UUID;

public class Converters {
{%- for entity_key in model.entities %}
    public static {{ entity_key | pascal_case }}Dto convert({{ entity_key | pascal_case }}Entity {{ entity_key | camel_case }}Entity) {
        {{ entity_key | pascal_case }}Dto.Builder builder = {{ entity_key | pascal_case }}Dto.newBuilder()
                .setName({{ entity_key | camel_case }}Entity.getName());
        if ({{ entity_key | camel_case }}Entity.getId() != null) {
            builder.setId(StringValue.of({{ entity_key | camel_case }}Entity.getId().toString()));
        }
        return builder.build();
    }

    public static {{ entity_key | pascal_case }}Entity convert({{ entity_key | pascal_case }}Dto {{ entity_key | camel_case }}) {
        {{ entity_key | pascal_case }}Entity entity;
        if ({{ entity_key | camel_case }}.hasId()) {
            entity = new {{ entity_key | pascal_case }}Entity(UUID.fromString({{ entity_key | camel_case }}.getId().getValue()), {{ entity_key | camel_case }}.getName());
        } else {
            entity = new {{ entity_key | pascal_case }}Entity({{ entity_key | camel_case }}.getName());
        }
        return entity;
    }
{%- endfor %}
}
