{% import "macros/java" as java %}
package {{ root_package }}.core;
{% if feature-sns %}
import io.awspring.cloud.messaging.core.NotificationMessagingTemplate;{% endif %}{% if feature-sqs %}
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;{% endif %}{% if persistence != 'None' %}
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;{% endif %}
import {{ root_package }}.api.v1.{{ ProjectPrefix }}{{ ProjectSuffix }};{% if persistence != 'None' %}
import {{ root_package }}.core.support.Converters;{% endif %}
import {{ root_package }}.grpc.v1.*;{% if persistence != 'None' %}
{%- for entity_key in model.entities %}
{{ java.core_persistence_imports(entity_key, model.entities[entity_key], model)}} {% endfor %}
{% endif %}{% if feature-sns %}
import {{ group_id }}.platform.aws.SnsProperties;{% endif %}{% if feature-sqs %}
import {{ group_id }}.platform.aws.SqsProperties;{% endif %}
{% if persistence != 'None' %}
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static {{ root_package }}.core.support.Converters.convert;{% endif %}
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
{%- set features_fields = [] -%}
{%- set features_cstr_args = [] -%}
{%- set features_cstr_fields = [] -%}

{%- if feature-sns -%}
{% set features_cstr_args = features_cstr_args
| extend(["NotificationMessagingTemplate notificationMessagingTemplate", "SnsProperties snsProperties"]) %}
{% set features_cstr_fields = features_cstr_fields
| extend("this.notificationMessagingTemplate = notificationMessagingTemplate;")
| extend("this.snsProperties = snsProperties;") %}
{%- endif -%}

{%- if feature-sqs -%}
{% set features_cstr_args = features_cstr_args
| extend("QueueMessagingTemplate queueMessagingTemplate")
| extend("SqsProperties sqsProperties") %}
{% set features_cstr_fields = features_cstr_fields
| extend("this.queueMessagingTemplate = queueMessagingTemplate;")
| extend("this.sqsProperties = sqsProperties;") %}
{%- endif %}

@Service
public class {{ ProjectPrefix }}{{ ProjectSuffix }}Core implements {{ ProjectPrefix }}{{ ProjectSuffix }} {{'{'}}
    {{ java.core_persistence_fields() }}
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public {{ ProjectPrefix }}{{ ProjectSuffix }}Core({{ java.core_persistence_constructor_args() }}
        ) {{'{'}}{{ java.core_persistence_constructor_assignments() }}
    }

    {%- for entity_key in model.entities %}
    {{ java.core_implementation_methods(entity_key, model.entities[entity_key], model) }}
    {% endfor %}
}
