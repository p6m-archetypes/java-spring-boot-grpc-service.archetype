{% import "macros/java" as java %}
package {{ root_package }}.api.v1;

import {{ root_package }}.grpc.v1.*;

import java.util.Iterator;

public interface {{ ProjectPrefix }}{{ ProjectSuffix }} {
{%- for entity_key in model.entities %}
    {{ java.api_interface_methods(entity_key, model.entities[entity_key], model)}}
    {% endfor %}
}
